package com.payroll.member3.entity;

import com.payroll.member1.entity.Employee;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
 * Member 03: Payroll Entity (`payroll` table)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Core domain entity storing calculated monthly salary records, allowances, bonuses,
 * Tax, EPF (8% Employee / 12% Employer), ETF (3% Employer), and Net Salary computation.
 * 
 * Database Relationships:
 * ----------------------
 * - ManyToOne: Payroll -> Employee (FetchType.LAZY)
 * - OneToOne: Payroll <-> SalaryDetails (mappedBy / cascade ALL)
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payroll")
@EntityListeners(AuditingEntityListener.class)
public class Payroll {

    /** Primary Key Payroll ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Associated Employee. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /** Month of salary calculation (1 - 12). */
    @Column(name = "pay_month", nullable = false)
    private Integer payMonth;

    /** Year of salary calculation. */
    @Column(name = "pay_year", nullable = false)
    private Integer payYear;

    /** Basic monthly salary. */
    @Column(name = "basic_salary", nullable = false, precision = 12, scale = 2)
    private BigDecimal basicSalary;

    /** Total allowances. */
    @Column(name = "allowance", nullable = false, precision = 12, scale = 2)
    private BigDecimal allowance;

    /** Total bonuses. */
    @Column(name = "bonus", nullable = false, precision = 12, scale = 2)
    private BigDecimal bonus;

    /** Gross Salary = Basic + Allowance + Bonus. */
    @Column(name = "gross_salary", nullable = false, precision = 12, scale = 2)
    private BigDecimal grossSalary;

    /** Total deductions (Tax + EPF Employee + Loan + Advance + Other). */
    @Column(name = "total_deduction", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalDeduction;

    /** Income Tax deduction. */
    @Column(name = "tax", nullable = false, precision = 12, scale = 2)
    private BigDecimal tax;

    /** EPF Employee Contribution (8%). */
    @Column(name = "epf_employee", nullable = false, precision = 12, scale = 2)
    private BigDecimal epfEmployee;

    /** EPF Employer Contribution (12%). */
    @Column(name = "epf_employer", nullable = false, precision = 12, scale = 2)
    private BigDecimal epfEmployer;

    /** ETF Employer Contribution (3%). */
    @Column(name = "etf_employer", nullable = false, precision = 12, scale = 2)
    private BigDecimal etfEmployer;

    /** Loan installment deduction. */
    @Column(name = "loan_deduction", nullable = false, precision = 12, scale = 2)
    private BigDecimal loanDeduction;

    /** Salary advance deduction. */
    @Column(name = "advance_deduction", nullable = false, precision = 12, scale = 2)
    private BigDecimal advanceDeduction;

    /** Net Salary = Gross Salary - Total Deduction. */
    @Column(name = "net_salary", nullable = false, precision = 12, scale = 2)
    private BigDecimal netSalary;

    /** Status (GENERATED, PAID). */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /** Date of salary payment. */
    @Column(name = "paid_date")
    private LocalDate paidDate;

    /** OneToOne relationship with SalaryDetails itemization. */
    @OneToOne(mappedBy = "payroll", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private SalaryDetails salaryDetails;

    /** Record creation timestamp. */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Record last update timestamp. */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
