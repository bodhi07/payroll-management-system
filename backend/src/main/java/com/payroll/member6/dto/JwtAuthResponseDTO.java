package com.payroll.member6.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * ============================================================================
 * Member 06: JWT Authentication Response DTO
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Data Transfer Object returned upon successful user authentication.
 * Contains JWT access token, refresh token, token type ("Bearer"), username, and user roles.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Encapsulation: Protects fields and exposes builder method.
 * 
 * Design Patterns Used:
 * --------------------
 * - DTO Pattern.
 * - Builder Pattern.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponseDTO {

    /** Formatted JWT access token string. */
    private String accessToken;

    /** Long-lived refresh token string. */
    private String refreshToken;

    /** Bearer authorization token header type. */
    @Builder.Default
    private String tokenType = "Bearer";

    /** Authenticated user's primary key ID. */
    private Long id;

    /** Authenticated username. */
    private String username;

    /** Authenticated email address. */
    private String email;

    /** Set of granted security role names. */
    private Set<String> roles;
}
