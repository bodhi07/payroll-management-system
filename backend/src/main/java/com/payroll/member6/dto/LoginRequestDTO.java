package com.payroll.member6.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * Member 06: Login Request DTO
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Data Transfer Object encapsulating credentials (username/email and password)
 * for JWT authentication login request.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Encapsulation: Private credentials fields with Jakarta validation annotations.
 * 
 * Design Patterns Used:
 * --------------------
 * - Data Transfer Object (DTO) Pattern.
 * - Builder Pattern.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    /** Username or email address for login authentication. */
    @NotBlank(message = "Username or Email is required")
    private String usernameOrEmail;

    /** Plaintext password to authenticate. */
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
