package HooYah.Yacht.conf;

import HooYah.Yacht.conf.security.TokenFilter;
import HooYah.Yacht.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (필요에 따라 활성화 가능)
                .formLogin(form -> form.disable()) // 폼 로그인 비활성화
                .httpBasic(httpBasic -> httpBasic.disable()) // 기본 로그인 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안함

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/api/**").access((authentication, context) -> {
                            if (authentication.get() instanceof AnonymousAuthenticationToken)
                                return new AuthorizationDecision(false);
                            return new AuthorizationDecision(true);
                        }) // AnonymousAuthentication제외 모두 허용
                        .anyRequest().permitAll()
                )

                .addFilterBefore(new TokenFilter(userRepository), LogoutFilter.class)
        ;

        return http.build();
    }

}
