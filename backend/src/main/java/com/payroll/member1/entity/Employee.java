package com.payroll.member1.entity;

import com.payroll.member5.entity.Department;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ============================================================================
 * Member 01: Employee Entity (`employees` table)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Core domain entity mapping employee record in the `employees` database table.
 * Stores personal profile details, NIC, salary details, and Department relationship.
 * 
 * Database Relationships:
 * ----------------------
 * - ManyToOne: Employee -> Department (FetchType.LAZY to prevent N+1 select performance degradation).
 * 
 * OOP Concepts Used:
 * --------------------
 * - Encapsulation: Private attributes with getters/setters via Lombok.
 * - Association / Composition: References associated {@link Department} entity.
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
@Table(name = "employees")
@EntityListeners(AuditingEntityListener.class)
public class Employee {

    /** Primary Key Employee ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long employeeId;

    /** Unique Company Employee Number (e.g., EMP-2026-001). */
    @Column(name = "employee_number", nullable = false, unique = true, length = 30)
    private String employeeNumber;

    /** Employee first name. */
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    /** Employee last name. */
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    /** Corporate email address. */
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    /** Contact phone number. */
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    /** National Identity Card (NIC) / SSN number. */
    @Column(name = "nic", nullable = false, unique = true, length = 20)
    private String nic;

    /** Gender string (MALE, FEMALE, OTHER). */
    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    /** Residential address. */
    @Column(name = "address", length = 255)
    private String address;

    /** Associated Department (ManyToOne association, FetchType.LAZY). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    /** Employment designation title (e.g., Software Engineer, Senior Architect). */
    @Column(name = "designation", nullable = false, length = 100)
    private String designation;

    /** Monthly basic salary allocation. */
    @Column(name = "basic_salary", nullable = false, precision = 12, scale = 2)
    private BigDecimal basicSalary;

    /** Date of joining the enterprise. */
    @Column(name = "join_date", nullable = false)
    private LocalDate joinDate;

    /** Employment status (ACTIVE, INACTIVE, TERMINATED, ON_LEAVE). */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /** Record creation timestamp. */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Record last update timestamp. */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
