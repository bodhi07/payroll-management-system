package com.payroll.member6.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ============================================================================
 * Member 06: Audit Log DTO
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Data Transfer Object for presenting audit trail records.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Encapsulation.
 * 
 * Design Patterns Used:
 * --------------------
 * - DTO Pattern.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDTO {

    private Long id;
    private String username;
    private String action;
    private String entityName;
    private String details;
    private String ipAddress;
    private LocalDateTime createdAt;
}
