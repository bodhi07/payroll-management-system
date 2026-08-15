package com.payroll.member6.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * ============================================================================
 * Member 06: User Entity (`users` table)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Represents application user credentials, password hash, and Security Roles mapping.
 * Serves as the primary user entity for authentication and auditing.
 * 
 * Database Relationships:
 * ----------------------
 * - ManyToMany: User <-> Role (via user_roles join table, FetchType.EAGER for security authorities loading).
 * 
 * OOP Concepts Used:
 * --------------------
 * - Encapsulation: Private entity fields accessed via Lombok getters/setters.
 * 
 * Design Patterns Used:
 * --------------------
 * - Entity Pattern (JPA Domain Model).
 * - Builder Pattern.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    /** Primary key ID for user. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Unique login username. */
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    /** Unique email address. */
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    /** BCrypt hashed password. */
    @Column(name = "password", nullable = false)
    private String password;

    /** Active account boolean flag. */
    @Builder.Default
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    /** Security roles associated with user (ManyToMany). */
    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

    /** Timestamp of record creation. */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Timestamp of last modification. */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
