package hu.projects.expense_tracker.middlewares;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
public class AuthMiddleware {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthMiddleware jwtAuthMiddleware;

    @Autowired
    public AuthMiddleware(AuthenticationProvider authenticationProvider, JwtAuthMiddleware jwtAuthMiddleware) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthMiddleware = jwtAuthMiddleware;
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                                .requestMatchers(
                                        "/scalar", "/scalar/**",
                                        "/v3/api-docs", "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()
                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthMiddleware, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}