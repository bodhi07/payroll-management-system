package com.payroll.member2.mapper;

import com.payroll.member2.dto.AttendanceDTO;
import com.payroll.member2.entity.Attendance;
import org.springframework.stereotype.Component;

/**
 * ============================================================================
 * Member 02: Attendance Mapper Component
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Converts between Attendance Entity and AttendanceDTO.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Component
public class AttendanceMapper {

    public AttendanceDTO toDTO(final Attendance attendance) {
        if (attendance == null) return null;
        return AttendanceDTO.builder()
                .id(attendance.getId())
                .employeeId(attendance.getEmployee() != null ? attendance.getEmployee().getEmployeeId() : null)
                .employeeName(attendance.getEmployee() != null ? attendance.getEmployee().getFirstName() + " " + attendance.getEmployee().getLastName() : null)
                .employeeNumber(attendance.getEmployee() != null ? attendance.getEmployee().getEmployeeNumber() : null)
                .date(attendance.getDate())
                .checkInTime(attendance.getCheckInTime())
                .checkOutTime(attendance.getCheckOutTime())
                .workingHours(attendance.getWorkingHours())
                .lateHours(attendance.getLateHours())
                .overtimeHours(attendance.getOvertimeHours())
                .status(attendance.getStatus())
                .createdAt(attendance.getCreatedAt())
                .updatedAt(attendance.getUpdatedAt())
                .build();
    }
}
