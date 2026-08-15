package com.payroll.member6.repository;

import com.payroll.member6.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ============================================================================
 * Member 06: AuditLog JPA Repository
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Provides database persistence queries for audit event logs. Supports pagination
 * and filtering by username or action.
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
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    /**
     * Finds paginated audit logs for a specific username actor.
     *
     * @param username Target actor username
     * @param pageable Pagination settings
     * @return Page of AuditLog entries
     */
    Page<AuditLog> findByUsername(String username, Pageable pageable);
}
