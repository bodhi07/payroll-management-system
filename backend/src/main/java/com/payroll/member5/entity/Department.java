package com.payroll.member5.entity;

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
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * ============================================================================
 * Member 05: Department Entity (`departments` table)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Enterprise JPA entity mapping company departments (e.g. IT, HR, Finance).
 * Provides organizational grouping for employees and aggregated salary reporting.
 * 
 * Database Table: `departments`
 * 
 * OOP Concepts Used:
 * --------------------
 * - Encapsulation: Private entity fields with getters/setters.
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
@Table(name = "departments")
@EntityListeners(AuditingEntityListener.class)
public class Department {

    /** Primary Key Department ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Department Name (e.g., Information Technology, Finance). */
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    /** Unique Department Code (e.g., DEPT-IT, DEPT-HR). */
    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    /** Detailed department description. */
    @Column(name = "description", length = 500)
    private String description;

    /** Record creation timestamp. */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Record modification timestamp. */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
