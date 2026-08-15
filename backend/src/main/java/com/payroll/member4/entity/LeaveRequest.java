package com.payroll.member4.entity;

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

/**
 * ============================================================================
 * Member 04: Leave Request Entity (`leave_requests` table)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * JPA entity mapping employee leave applications and manager approval decisions.
 * 
 * Database Table: `leave_requests`
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "leave_requests")
@EntityListeners(AuditingEntityListener.class)
public class LeaveRequest {

    /** Primary Key Leave Request ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Associated Employee applying for leave. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /** Category of leave (ANNUAL, CASUAL, MEDICAL, UNPAID). */
    @Column(name = "leave_type", nullable = false, length = 30)
    private String leaveType;

    /** Leave starting date. */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /** Leave ending date. */
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    /** Total leave days count. */
    @Column(name = "total_days", nullable = false)
    private Integer totalDays;

    /** Reason for leave application. */
    @Column(name = "reason", length = 500)
    private String reason;

    /** Current request approval status (PENDING, APPROVED, REJECTED). */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /** Reason provided by HR/Admin for approval or rejection. */
    @Column(name = "action_reason", length = 500)
    private String actionReason;

    /** Username of HR/Admin who approved or rejected the request. */
    @Column(name = "approved_by", length = 50)
    private String approvedBy;

    /** Record creation timestamp. */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Record last update timestamp. */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
