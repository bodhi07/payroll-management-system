package com.payroll.member5.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * ============================================================================
 * Member 05: Department Analytics Report DTO
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Transports summary report metrics for a department including assigned employee count
 * and total basic salary budget allocation.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentReportDTO {

    private Long departmentId;
    private String departmentName;
    private String departmentCode;
    private long totalEmployeeCount;
    private BigDecimal totalDepartmentSalary;
}
