package HooYah.Yacht.conf.security;

import HooYah.Yacht.user.JWTUtil;
import HooYah.Yacht.user.domain.User;
import HooYah.Yacht.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Optional<String> token = getTokenFromCookies("token", request);
        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        Long userId = JWTUtil.decodeToken(token.get());
        Optional<User> userOpt = userRepository.findById(userId);

        if(userOpt.isPresent())
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userOpt.get(), null));

        filterChain.doFilter(request, response);
    }

    public Optional<String> getTokenFromCookies(String tokenName, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> tokenCookie = Arrays.stream(cookies)
                    .filter(cookie -> tokenName.equals(cookie.getName()))
                    .findFirst();

            if (tokenCookie.isPresent()) {
                return Optional.of(tokenCookie.get().getValue());
            }
        }
        return Optional.empty();
    }
}
