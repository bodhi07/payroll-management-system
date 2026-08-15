package com.payroll.member2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * ============================================================================
 * Member 02: Attendance Data Transfer Object
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Data Transfer Object for transferring attendance details to clients.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {

    private Long id;
    private Long employeeId;
    private String employeeName;
    private String employeeNumber;
    private LocalDate date;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private Double workingHours;
    private Double lateHours;
    private Double overtimeHours;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
