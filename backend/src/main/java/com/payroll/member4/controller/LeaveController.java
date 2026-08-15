package com.payroll.member4.controller;

import com.payroll.member4.dto.ApplyLeaveDTO;
import com.payroll.member4.dto.LeaveActionDTO;
import com.payroll.member4.dto.LeaveBalanceDTO;
import com.payroll.member4.dto.LeaveRequestDTO;
import com.payroll.member4.service.LeaveService;
import com.payroll.response.ApiResponse;
import com.payroll.response.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * ============================================================================
 * Member 04: Leave Management REST Controller
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * REST controller exposing endpoints (`/api/v1/leaves`) for applying leave,
 * approving/rejecting leave applications, checking leave balances, and viewing leave history.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@RestController
@RequestMapping({"/api/v1/leaves", "/api/v1/leave"})
@Tag(name = "Leave Management", description = "Endpoints for applying leave, approving/rejecting, and checking leave balances.")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(final LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("/apply")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @Operation(summary = "Apply For Leave", description = "Submits a new leave request application.")
    public ResponseEntity<ApiResponse<LeaveRequestDTO>> applyLeave(@Valid @RequestBody final ApplyLeaveDTO applyLeaveDTO) {
        final LeaveRequestDTO dto = leaveService.applyLeave(applyLeaveDTO);
        return new ResponseEntity<>(ApiResponse.success(HttpStatus.CREATED.value(), "Leave application submitted successfully", dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Approve Leave Request", description = "Approves a pending leave request.")
    public ResponseEntity<ApiResponse<LeaveRequestDTO>> approveLeave(@PathVariable("id") final Long id,
                                                                      @RequestBody(required = false) final LeaveActionDTO actionDTO,
                                                                      final Authentication authentication) {
        final String reviewerUsername = authentication != null ? authentication.getName() : "ADMIN";
        final LeaveRequestDTO dto = leaveService.approveLeave(id, actionDTO, reviewerUsername);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Leave request approved", dto));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Reject Leave Request", description = "Rejects a pending leave request.")
    public ResponseEntity<ApiResponse<LeaveRequestDTO>> rejectLeave(@PathVariable("id") final Long id,
                                                                     @RequestBody(required = false) final LeaveActionDTO actionDTO,
                                                                     final Authentication authentication) {
        final String reviewerUsername = authentication != null ? authentication.getName() : "ADMIN";
        final LeaveRequestDTO dto = leaveService.rejectLeave(id, actionDTO, reviewerUsername);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Leave request rejected", dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @Operation(summary = "Get Leave Request By ID", description = "Retrieves leave request details by ID.")
    public ResponseEntity<ApiResponse<LeaveRequestDTO>> getLeaveById(@PathVariable("id") final Long id) {
        final LeaveRequestDTO dto = leaveService.getLeaveById(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Leave details fetched", dto));
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @Operation(summary = "Get Employee Leave History", description = "Fetches paginated leave history for an employee.")
    public ResponseEntity<ApiResponse<PagedResponse<LeaveRequestDTO>>> getEmployeeLeaveHistory(
            @PathVariable("employeeId") final Long employeeId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) final int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) final int pageSize) {

        final PagedResponse<LeaveRequestDTO> response = leaveService.getEmployeeLeaveHistory(employeeId, pageNo, pageSize);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Employee leave history fetched", response));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Get Leave Requests By Status", description = "Fetches leave requests by status (PENDING, APPROVED, REJECTED).")
    public ResponseEntity<ApiResponse<PagedResponse<LeaveRequestDTO>>> getLeaveRequestsByStatus(
            @PathVariable("status") final String status,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) final int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) final int pageSize) {

        final PagedResponse<LeaveRequestDTO> response = leaveService.getLeaveRequestsByStatus(status, pageNo, pageSize);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Leave requests fetched by status", response));
    }

    @GetMapping("/balance/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @Operation(summary = "Get Employee Leave Balance", description = "Calculates remaining annual, casual, and medical leave balances.")
    public ResponseEntity<ApiResponse<LeaveBalanceDTO>> getLeaveBalance(
            @PathVariable("employeeId") final Long employeeId,
            @RequestParam(value = "year", required = false) final Integer year) {

        final int targetYear = year != null ? year : LocalDate.now().getYear();
        final LeaveBalanceDTO balance = leaveService.getEmployeeLeaveBalance(employeeId, targetYear);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Leave balance retrieved successfully", balance));
    }
}
