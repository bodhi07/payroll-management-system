package com.payroll.member3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ============================================================================
 * Member 03: Payroll Data Transfer Object
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Data Transfer Object encapsulating calculated payroll details.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayrollDTO {

    private Long id;
    private Long employeeId;
    private String employeeName;
    private String employeeNumber;
    private String departmentName;
    private Integer payMonth;
    private Integer payYear;
    private BigDecimal basicSalary;
    private BigDecimal allowance;
    private BigDecimal bonus;
    private BigDecimal grossSalary;
    private BigDecimal totalDeduction;
    private BigDecimal tax;
    private BigDecimal epfEmployee;
    private BigDecimal epfEmployer;
    private BigDecimal etfEmployer;
    private BigDecimal loanDeduction;
    private BigDecimal advanceDeduction;
    private BigDecimal netSalary;
    private String status;
    private LocalDate paidDate;
    private SalaryDetailsDTO salaryDetails;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
