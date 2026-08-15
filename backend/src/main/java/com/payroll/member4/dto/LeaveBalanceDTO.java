package com.payroll.member4.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * Member 04: Employee Leave Balance Summary DTO
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Transports an employee's annual leave quota allowances, taken days, and remaining balance.
 * Standard Entitlements: Annual = 14, Casual = 7, Medical = 14.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceDTO {

    private Long employeeId;
    private String employeeName;
    private int year;

    // Annual Leave
    private int annualQuota;
    private int annualTaken;
    private int annualBalance;

    // Casual Leave
    private int casualQuota;
    private int casualTaken;
    private int casualBalance;

    // Medical Leave
    private int medicalQuota;
    private int medicalTaken;
    private int medicalBalance;
}
