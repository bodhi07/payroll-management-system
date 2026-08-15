package com.payroll.member4.mapper;

import com.payroll.member4.dto.LeaveRequestDTO;
import com.payroll.member4.entity.LeaveRequest;
import org.springframework.stereotype.Component;

/**
 * ============================================================================
 * Member 04: Leave Mapper Component
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Converts between LeaveRequest Entity and LeaveRequestDTO.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Component
public class LeaveMapper {

    public LeaveRequestDTO toDTO(final LeaveRequest leaveRequest) {
        if (leaveRequest == null) return null;
        return LeaveRequestDTO.builder()
                .id(leaveRequest.getId())
                .employeeId(leaveRequest.getEmployee() != null ? leaveRequest.getEmployee().getEmployeeId() : null)
                .employeeName(leaveRequest.getEmployee() != null ? leaveRequest.getEmployee().getFirstName() + " " + leaveRequest.getEmployee().getLastName() : null)
                .employeeNumber(leaveRequest.getEmployee() != null ? leaveRequest.getEmployee().getEmployeeNumber() : null)
                .leaveType(leaveRequest.getLeaveType())
                .startDate(leaveRequest.getStartDate())
                .endDate(leaveRequest.getEndDate())
                .totalDays(leaveRequest.getTotalDays())
                .reason(leaveRequest.getReason())
                .status(leaveRequest.getStatus())
                .actionReason(leaveRequest.getActionReason())
                .approvedBy(leaveRequest.getApprovedBy())
                .createdAt(leaveRequest.getCreatedAt())
                .updatedAt(leaveRequest.getUpdatedAt())
                .build();
    }
}
