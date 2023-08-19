package io.bayrktlihn.payment.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bayrktlihn.payment.security.filter.BearerTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${base64-encoded-jwt-secret-key}")
    private String base64EncodedJwtSecretKey;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;


    BearerTokenFilter bearerTokenFilter() {
        OrRequestMatcher orRequestMatcher = getPermitAllRequestMatchers();

        return new BearerTokenFilter(base64EncodedJwtSecretKey, orRequestMatcher, userDetailsService, objectMapper);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        OrRequestMatcher permitAllRequestMatchers = getPermitAllRequestMatchers();

        return http
                .cors(httpSecurityCorsConfigurer -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(List.of("*"));
                    corsConfiguration.setAllowedMethods(List.of("*"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);

                    UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
                    configurationSource.registerCorsConfiguration("/**", corsConfiguration);
                    httpSecurityCorsConfigurer.configurationSource(configurationSource);
                })
                .sessionManagement(httpSecuritySessionManagementConfigurer -> {
                    httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry.requestMatchers(permitAllRequestMatchers).permitAll()
                            .anyRequest().authenticated();
                })
                .addFilterBefore(bearerTokenFilter(), BasicAuthenticationFilter.class)
                .build();
    }

    private static OrRequestMatcher getPermitAllRequestMatchers() {
        OrRequestMatcher permitAllRequestMatchers = new OrRequestMatcher(
                new AntPathRequestMatcher("/api/v1/users/login"),
                new AntPathRequestMatcher("/api/v1/users/status")
        );
        return permitAllRequestMatchers;
    }

}
