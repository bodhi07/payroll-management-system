package com.payroll.member3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ============================================================================
 * Member 03: Payslip Print / View DTO
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Transports formatted payslip layout fields for PDF rendering or client view.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayslipDTO {

    private String payslipNumber;
    private String companyName;
    private String monthYear;

    // Employee Profile
    private Long employeeId;
    private String employeeName;
    private String employeeNumber;
    private String designation;
    private String departmentName;
    private String nic;

    // Earnings
    private BigDecimal basicSalary;
    private BigDecimal allowance;
    private BigDecimal bonus;
    private BigDecimal grossSalary;

    // Deductions
    private BigDecimal tax;
    private BigDecimal epfEmployee; // 8%
    private BigDecimal loanDeduction;
    private BigDecimal advanceDeduction;
    private BigDecimal totalDeductions;

    // Contributions (Employer)
    private BigDecimal epfEmployer; // 12%
    private BigDecimal etfEmployer; // 3%

    // Net Payable
    private BigDecimal netSalary;
    private LocalDate generatedDate;
}
