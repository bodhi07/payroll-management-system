package com.payroll.member6.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * Member 06: Role Entity (`roles` table)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Database entity mapping user security roles (e.g. ROLE_ADMIN, ROLE_HR, ROLE_EMPLOYEE).
 * Used by Spring Security for Role-Based Access Control (RBAC).
 * 
 * OOP Concepts Used:
 * --------------------
 * - Encapsulation: Private entity attributes with Lombok getters/setters.
 * 
 * Design Patterns Used:
 * --------------------
 * - Entity Pattern (JPA/Hibernate Data Model).
 * - Builder Pattern: Constructed using {@code @Builder}.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role {

    /** Primary key ID for role. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Unique role name (e.g. ROLE_ADMIN, ROLE_HR, ROLE_EMPLOYEE). */
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    /** Description of role permissions. */
    @Column(name = "description")
    private String description;
}
