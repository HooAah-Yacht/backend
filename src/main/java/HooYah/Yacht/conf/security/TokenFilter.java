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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class TokenFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Optional<String> token = getTokenFromBearer(request);

        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        Long userId = JWTUtil.decodeToken(token.get());
        Optional<User> userOpt = userRepository.findById(userId);

        if(userOpt.isPresent()) {
            log.info(String.format("token success [user id : %d]", userOpt.get().getId()));
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userOpt.get(), null));
        }

        filterChain.doFilter(request, response);
    }

    public Optional<String> getTokenFromBearer(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Optional.empty();
        }

        String token = authHeader.substring(7);
        return Optional.of(token);
    }
}
