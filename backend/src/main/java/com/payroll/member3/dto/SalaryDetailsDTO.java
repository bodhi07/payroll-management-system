package com.payroll.member3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ============================================================================
 * Member 03: Salary Details DTO
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Transports itemized salary detail explanations and payslip tracking metadata.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryDetailsDTO {

    private Long id;
    private String allowanceBreakdown;
    private String bonusDescription;
    private String deductionReason;
    private Double taxPercentage;
    private String payslipNumber;
    private LocalDateTime createdAt;
}
