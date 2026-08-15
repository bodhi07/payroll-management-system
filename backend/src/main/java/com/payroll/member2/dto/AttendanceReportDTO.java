package com.payroll.member2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * ============================================================================
 * Member 02: Attendance Summary Report DTO
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Aggregates monthly attendance metrics for an employee (total present days,
 * total working hours, total late hours, and total overtime hours).
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceReportDTO {

    private Long employeeId;
    private String employeeName;
    private String employeeNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private long totalPresentDays;
    private long totalLateDays;
    private double totalWorkingHours;
    private double totalLateHours;
    private double totalOvertimeHours;
}
