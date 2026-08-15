package com.payroll.member2.entity;

import com.payroll.member1.entity.Employee;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * ============================================================================
 * Member 02: Attendance Entity (`attendance` table)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Enterprise JPA entity mapping daily employee attendance logs.
 * Computes check-in/out timestamps, working hours, late hours, and overtime hours.
 * 
 * Database Relationships:
 * ----------------------
 * - ManyToOne: Attendance -> Employee (FetchType.LAZY).
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attendance")
@EntityListeners(AuditingEntityListener.class)
public class Attendance {

    /** Primary key attendance ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Associated employee. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /** Date of attendance entry. */
    @Column(name = "date", nullable = false)
    private LocalDate date;

    /** Time of check-in. */
    @Column(name = "check_in_time")
    private LocalTime checkInTime;

    /** Time of check-out. */
    @Column(name = "check_out_time")
    private LocalTime checkOutTime;

    /** Calculated total working hours. */
    @Column(name = "working_hours")
    private Double workingHours;

    /** Calculated late hours (relative to 08:30 AM standard shift). */
    @Column(name = "late_hours")
    private Double lateHours;

    /** Calculated overtime hours (hours worked beyond 8.0 standard shift). */
    @Column(name = "overtime_hours")
    private Double overtimeHours;

    /** Attendance status (PRESENT, LATE, HALF_DAY, ABSENT). */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /** Record creation timestamp. */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Record update timestamp. */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
