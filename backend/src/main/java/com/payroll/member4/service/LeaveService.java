package com.payroll.member4.service;

import com.payroll.member4.dto.ApplyLeaveDTO;
import com.payroll.member4.dto.LeaveActionDTO;
import com.payroll.member4.dto.LeaveBalanceDTO;
import com.payroll.member4.dto.LeaveRequestDTO;
import com.payroll.response.PagedResponse;

/**
 * ============================================================================
 * Member 04: Leave Service Interface
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Service contract for leave application, approval, rejection, and balance calculation.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public interface LeaveService {

    LeaveRequestDTO applyLeave(ApplyLeaveDTO applyLeaveDTO);

    LeaveRequestDTO approveLeave(Long leaveId, LeaveActionDTO actionDTO, String reviewerUsername);

    LeaveRequestDTO rejectLeave(Long leaveId, LeaveActionDTO actionDTO, String reviewerUsername);

    LeaveRequestDTO getLeaveById(Long leaveId);

    PagedResponse<LeaveRequestDTO> getEmployeeLeaveHistory(Long employeeId, int pageNo, int pageSize);

    PagedResponse<LeaveRequestDTO> getLeaveRequestsByStatus(String status, int pageNo, int pageSize);

    LeaveBalanceDTO getEmployeeLeaveBalance(Long employeeId, int year);
}
