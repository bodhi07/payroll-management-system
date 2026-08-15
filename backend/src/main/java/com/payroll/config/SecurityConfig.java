package com.payroll.config;

import com.payroll.jwt.JwtAuthenticationEntryPoint;
import com.payroll.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * ============================================================================
 * Spring Security 3.5+ Enterprise Architecture Configuration
 * ============================================================================
 * 
 * Why This Class Exists:
 * --------------------
 * Central security configuration class. Enables web security, method-level authorization,
 * BCrypt password hashing, stateless JWT session policy, CORS policy, and configures
 * route-level role authorization.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Polymorphism & Interface Realization: Configures SecurityFilterChain beans.
 * - Dependency Injection: Constructor injection for JWT entry point and request filter.
 * 
 * Design Patterns Used:
 * --------------------
 * - Chain of Responsibility Pattern: Security Filter Chain pipeline.
 * - Factory Bean Pattern: {@code @Bean} factory methods for PasswordEncoder and AuthenticationManager.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtEntryPoint;
    private final JwtAuthenticationFilter jwtAuthFilter;

    /**
     * Constructor Injection (Strict adherence to SOLID & Clean Code standards).
     *
     * @param jwtEntryPoint JWT unauthorized entry point handler
     * @param jwtAuthFilter JwtAuthenticationFilter filter bean
     */
    public SecurityConfig(final JwtAuthenticationEntryPoint jwtEntryPoint,
                          final JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtEntryPoint = jwtEntryPoint;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Bean definition for BCrypt Password Encoder.
     *
     * @return {@link BCryptPasswordEncoder} instance with strength 10
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean definition for Spring Security AuthenticationManager.
     *
     * @param authenticationConfiguration Spring Auth configuration context
     * @return Built {@link AuthenticationManager}
     * @throws Exception if manager build fails
     */
    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures the main Spring Security Filter Chain.
     *
     * @param http HttpSecurity configuration object
     * @return Built {@link SecurityFilterChain}
     * @throws Exception if security build fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
                // Disable CSRF for stateless REST APIs using JWT
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                
                // Configure custom exception entry point for unauthenticated requests
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtEntryPoint))
                
                // Set session creation policy to STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                // Configure request path authorization permissions
                .authorizeHttpRequests(auth -> auth
                        // Public Endpoints (Authentication & Swagger Documentation UI)
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/api-docs/**"
                        ).permitAll()
                        
                        // All other API requests require JWT authentication
                        .anyRequest().authenticated()
                );

        // Add JwtAuthenticationFilter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures CORS rules for cross-origin frontend requests.
     *
     * @return {@link CorsConfigurationSource} bean
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setExposedHeaders(List.of("Authorization"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
