package com.payroll.member3.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * ============================================================================
 * Member 03: Generate Payroll Request DTO
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Captures request input parameters for calculating employee monthly payroll.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneratePayrollDTO {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Pay month is required")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private Integer payMonth;

    @NotNull(message = "Pay year is required")
    @Min(value = 2000, message = "Invalid year")
    private Integer payYear;

    private BigDecimal allowance;
    private BigDecimal bonus;
    private BigDecimal loanDeduction;
    private BigDecimal advanceDeduction;
    private Double customTaxPercentage;

    private String allowanceBreakdown;
    private String bonusDescription;
    private String deductionReason;
}
