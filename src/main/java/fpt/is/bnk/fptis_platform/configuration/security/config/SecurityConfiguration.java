package fpt.is.bnk.fptis_platform.configuration.security.config;

import fpt.is.bnk.fptis_platform.configuration.security.entrypoint.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Admin 11/25/2025
 *
 **/
@Configuration
public class SecurityConfiguration {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    private static final String[] WHITELIST = {
            // Authentication
            "/api/users/login",
            "/api/users/logout",
            "/api/users/register",
            "/api/users/refresh",

            "/api/internal/**",

            // API DOCS
            "/swagger-ui/**",
            "/v3/api-docs/**",

            // Health check
            "/actuator/health",
            "/actuator/health/**",

            "/execute/**",
    };

    @Bean
    public SecurityFilterChain appSecurityFilterChain(
            HttpSecurity http,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
            SkipPathBearerTokenResolver skipPathBearerTokenResolver,
            JwtAuthenticationConverter jwtAuthenticationConverter
    ) throws Exception {

        http
                // ==========================================================================================
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // ==========================================================================================
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .requestMatchers(WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                // ==========================================================================================
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .bearerTokenResolver(skipPathBearerTokenResolver)
                );


        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration config =
                new org.springframework.web.cors.CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        config.addExposedHeader("Set-Cookie");
        source.registerCorsConfiguration("/**", config);
        return source;
    }


}
