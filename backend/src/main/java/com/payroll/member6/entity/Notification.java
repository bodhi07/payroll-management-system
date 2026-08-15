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
 * Member 06: Notification Entity (`notifications` table)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Stores in-app user notifications (e.g., leave request decision, payslip generated).
 * 
 * OOP Concepts Used:
 * --------------------
 * - Encapsulation: Private entity fields with getters/setters.
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
@Table(name = "notifications")
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    /** Primary key ID for notification. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Target user ID receiving notification. */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** Notification header title. */
    @Column(name = "title", nullable = false, length = 150)
    private String title;

    /** Main message body. */
    @Column(name = "message", nullable = false, length = 1000)
    private String message;

    /** Read status flag. */
    @Builder.Default
    @Column(name = "is_read", nullable = false)
    private boolean read = false;

    /** Creation timestamp. */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
