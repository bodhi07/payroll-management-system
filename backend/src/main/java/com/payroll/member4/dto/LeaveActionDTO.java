package com.payroll.member4.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * Member 04: Leave Approval / Rejection Action DTO
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Transports manager action notes when approving or rejecting a leave request.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveActionDTO {

    private String actionReason;
}
