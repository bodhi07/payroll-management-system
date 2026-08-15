package com.payroll.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payroll.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * ============================================================================
 * Security JWT Authentication Entry Point
 * ============================================================================
 * 
 * Why This Class Exists:
 * --------------------
 * Handles unauthenticated HTTP access attempts. When an unauthenticated user
 * attempts to access a protected REST endpoint, this entry point intercepts the
 * error and writes a standardized JSON HTTP 401 {@link ApiResponse}.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Interface Realization (Polymorphism): Implements {@link AuthenticationEntryPoint}.
 * 
 * Design Patterns Used:
 * --------------------
 * - Interceptor / Strategy Pattern: Plugs into Spring Security filter pipeline.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();


    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException, ServletException {
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final ApiResponse<Object> apiResponse = ApiResponse.error(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized: Authentication token is missing, invalid, or expired. " + authException.getMessage()
        );

        objectMapper.writeValue(response.getOutputStream(), apiResponse);
    }
}
