package com.payroll.member6.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * ============================================================================
 * Member 06: AuditLog Entity (`audit_logs` table)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Records security and business event audit trails (e.g. employee created,
 * salary calculated, leave approved, user logged in) for enterprise auditing compliance.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Encapsulation: Private audit log fields with getters/setters.
 * 
 * Design Patterns Used:
 * --------------------
 * - Entity Pattern.
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
@Table(name = "audit_logs")
@EntityListeners(AuditingEntityListener.class)
public class AuditLog {

    /** Primary key ID for audit log record. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Username of performing actor. */
    @Column(name = "username", nullable = false)
    private String username;

    /** Action identifier (e.g., CREATE_EMPLOYEE, APPROVE_LEAVE). */
    @Column(name = "action", nullable = false)
    private String action;

    /** Affected entity target name (e.g., Employee, LeaveRequest). */
    @Column(name = "entity_name")
    private String entityName;

    /** Detail description or JSON payload change log. */
    @Column(name = "details", length = 1000)
    private String details;

    /** IP address of origin request. */
    @Column(name = "ip_address")
    private String ipAddress;

    /** Timestamp of audit event. */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
