package com.payroll.member6.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * ============================================================================
 * Member 06: User Registration Request DTO
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Data Transfer Object capturing user registration payload (username, email,
 * password, security roles).
 * 
 * OOP Concepts Used:
 * --------------------
 * - Encapsulation: Private input fields with validation rules.
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
public class RegisterRequestDTO {

    /** Preferred login username. */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    /** Unique email address. */
    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;

    /** Plaintext account password. */
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    /** Set of roles assigned to user (e.g. ROLE_EMPLOYEE, ROLE_HR, ROLE_ADMIN). */
    private Set<String> roles;
}
