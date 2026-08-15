package com.payroll.member6.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ============================================================================
 * Member 06: Notification DTO
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Data Transfer Object for returning notification payloads.
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
public class NotificationDTO {

    private Long id;
    private Long userId;
    private String title;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;
}
