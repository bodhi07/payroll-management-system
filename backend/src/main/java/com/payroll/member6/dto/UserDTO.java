package com.payroll.member6.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * ============================================================================
 * Member 06: User View DTO
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Represents public user detail state sent back to clients, omitting confidential
 * password hashes.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Encapsulation & Data Abstraction: Omits hashed credentials.
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
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private boolean enabled;
    private Set<String> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
