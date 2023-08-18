package io.bayrktlihn.payment.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bayrktlihn.payment.security.filter.BearerTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${base64-encoded-jwt-secret-key}")
    private String base64EncodedJwtSecretKey;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Bean
    BearerTokenFilter bearerTokenFilter() {
        return new BearerTokenFilter(base64EncodedJwtSecretKey, new AntPathRequestMatcher("/api/v1/users/login"), userDetailsService, objectMapper);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry.requestMatchers(new AntPathRequestMatcher("/api/v1/users/login")).permitAll()
                            .anyRequest().authenticated();
                })
                .addFilterBefore(bearerTokenFilter(), BasicAuthenticationFilter.class)
                .build();
    }

}
