package com.payroll.member6.repository;

import com.payroll.member6.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ============================================================================
 * Member 06: User JPA Repository
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Data access abstraction layer for {@link User} database persistence.
 * Provides user retrieval by username, email, and existence check queries.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Interface Realization: Extends {@link JpaRepository}.
 * 
 * Design Patterns Used:
 * --------------------
 * - Repository Pattern.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds user entity by unique username.
     *
     * @param username Search username
     * @return Optional containing User if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds user entity by unique email address.
     *
     * @param email Search email
     * @return Optional containing User if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if username exists in database.
     *
     * @param username Target username
     * @return True if exists
     */
    boolean existsByUsername(String username);

    /**
     * Checks if email exists in database.
     *
     * @param email Target email
     * @return True if exists
     */
    boolean existsByEmail(String email);
}
