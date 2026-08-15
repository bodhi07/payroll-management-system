package com.payroll.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * ============================================================================
 * Security JWT Request Filter
 * ============================================================================
 * 
 * Why This Class Exists:
 * --------------------
 * Intercepts every incoming HTTP request (extending {@link OncePerRequestFilter}),
 * extracts the `Authorization: Bearer <token>` header, validates the JWT token,
 * loads the authenticated UserDetails, and populates the Spring SecurityContextHolder.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Inheritance: Extends {@link OncePerRequestFilter}.
 * - Dependency Injection: Constructor Injection for {@link JwtTokenProvider} and {@link UserDetailsService}.
 * - Encapsulation: Hides token extraction and context population steps.
 * 
 * Design Patterns Used:
 * --------------------
 * - Filter Pattern / Interceptor Pattern: Intercepts request pipeline execution.
 * - Dependency Injection Pattern.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor Injection (Never use Field Injection).
     *
     * @param tokenProvider      JWT token provider utility
     * @param userDetailsService Spring Security UserDetailsService
     */
    public JwtAuthenticationFilter(final JwtTokenProvider tokenProvider, final UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        try {
            // Extract Bearer token string from HTTP request header
            final String jwt = parseJwt(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                final String username = tokenProvider.getUsernameFromToken(jwt);

                // Load custom user details from database
                final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                final UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Populate security context with authenticated principal
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Cannot set user authentication in SecurityContext: {}", ex);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extract token string from Bearer header scheme.
     *
     * @param request HTTP request
     * @return Raw JWT token or null
     */
    private String parseJwt(final HttpServletRequest request) {
        final String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
