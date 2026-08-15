package com.payroll.member6.repository;

import com.payroll.member6.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ============================================================================
 * Member 06: Role JPA Repository
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Data access abstraction layer for {@link Role} database persistence.
 * Provides lookup methods for security roles by name.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Polymorphism & Interface Realization: Extends {@link JpaRepository}.
 * 
 * Design Patterns Used:
 * --------------------
 * - Repository Pattern: Encapsulates database access queries.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds security role by name.
     *
     * @param name Role string (e.g., ROLE_ADMIN)
     * @return Optional containing Role entity if present
     */
    Optional<Role> findByName(String name);
}
