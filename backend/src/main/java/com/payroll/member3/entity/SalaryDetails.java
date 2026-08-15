package com.payroll.member3.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
 * Member 03: Salary Details Breakdown Entity (`salary_details` table)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Stores itemized breakdown notes, tax percentages, and payslip reference tracking numbers.
 * 
 * Database Relationships:
 * ----------------------
 * - OneToOne: SalaryDetails -> Payroll
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "salary_details")
@EntityListeners(AuditingEntityListener.class)
public class SalaryDetails {

    /** Primary Key Salary Details ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Parent Payroll entity reference. */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payroll_id", nullable = false, unique = true)
    private Payroll payroll;

    /** Breakdown explanation of allowances. */
    @Column(name = "allowance_breakdown", length = 500)
    private String allowanceBreakdown;

    /** Description of performance bonuses. */
    @Column(name = "bonus_description", length = 500)
    private String bonusDescription;

    /** Reasons for custom deductions. */
    @Column(name = "deduction_reason", length = 500)
    private String deductionReason;

    /** Applied tax percentage rate. */
    @Column(name = "tax_percentage")
    private Double taxPercentage;

    /** Unique Generated Payslip Reference Number (e.g. PAYSLIP-2026-07-001). */
    @Column(name = "payslip_number", nullable = false, unique = true, length = 50)
    private String payslipNumber;

    /** Creation timestamp. */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
