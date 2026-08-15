package com.payroll.member4.service.impl;

import com.payroll.exception.ResourceNotFoundException;
import com.payroll.exception.ValidationException;
import com.payroll.member1.entity.Employee;
import com.payroll.member1.repository.EmployeeRepository;
import com.payroll.member4.dto.ApplyLeaveDTO;
import com.payroll.member4.dto.LeaveActionDTO;
import com.payroll.member4.dto.LeaveBalanceDTO;
import com.payroll.member4.dto.LeaveRequestDTO;
import com.payroll.member4.entity.LeaveRequest;
import com.payroll.member4.mapper.LeaveMapper;
import com.payroll.member4.repository.LeaveRepository;
import com.payroll.member4.service.LeaveService;
import com.payroll.response.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ============================================================================
 * Member 04: Leave Service Implementation
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Implements leave request submission, overlapping dates detection, manager approval,
 * and quota calculation.
 * 
 * Quota Rules:
 * ------------
 * - ANNUAL: 14 Days
 * - CASUAL: 7 Days
 * - MEDICAL: 14 Days
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Service
public class LeaveServiceImpl implements LeaveService {

    private static final int ANNUAL_QUOTA = 14;
    private static final int CASUAL_QUOTA = 7;
    private static final int MEDICAL_QUOTA = 14;

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveMapper leaveMapper;

    public LeaveServiceImpl(final LeaveRepository leaveRepository,
                            final EmployeeRepository employeeRepository,
                            final LeaveMapper leaveMapper) {
        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
        this.leaveMapper = leaveMapper;
    }

    @Override
    @Transactional
    public LeaveRequestDTO applyLeave(final ApplyLeaveDTO applyLeaveDTO) {
        if (applyLeaveDTO.getEndDate().isBefore(applyLeaveDTO.getStartDate())) {
            throw new ValidationException("Leave end date cannot be earlier than start date.");
        }

        final Employee employee = employeeRepository.findById(applyLeaveDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", applyLeaveDTO.getEmployeeId()));

        // Check for overlapping pending or approved leave requests
        if (leaveRepository.existsOverlappingLeave(applyLeaveDTO.getEmployeeId(), applyLeaveDTO.getStartDate(), applyLeaveDTO.getEndDate())) {
            throw new ValidationException("An overlapping leave request already exists for the selected date range.");
        }

        // Compute business days excluding weekends
        final int totalDays = calculateBusinessDays(applyLeaveDTO.getStartDate(), applyLeaveDTO.getEndDate());

        final LeaveRequest leaveRequest = LeaveRequest.builder()
                .employee(employee)
                .leaveType(applyLeaveDTO.getLeaveType().toUpperCase())
                .startDate(applyLeaveDTO.getStartDate())
                .endDate(applyLeaveDTO.getEndDate())
                .totalDays(totalDays)
                .reason(applyLeaveDTO.getReason())
                .status("PENDING")
                .build();

        final LeaveRequest saved = leaveRepository.save(leaveRequest);
        return leaveMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public LeaveRequestDTO approveLeave(final Long leaveId, final LeaveActionDTO actionDTO, final String reviewerUsername) {
        final LeaveRequest leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("LeaveRequest", "id", leaveId));

        if (!"PENDING".equalsIgnoreCase(leave.getStatus())) {
            throw new ValidationException("Leave request has already been processed (Current status: " + leave.getStatus() + ").");
        }

        leave.setStatus("APPROVED");
        leave.setActionReason(actionDTO != null ? actionDTO.getActionReason() : "Approved by HR/Admin");
        leave.setApprovedBy(reviewerUsername);

        final LeaveRequest updated = leaveRepository.save(leave);
        return leaveMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public LeaveRequestDTO rejectLeave(final Long leaveId, final LeaveActionDTO actionDTO, final String reviewerUsername) {
        final LeaveRequest leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("LeaveRequest", "id", leaveId));

        if (!"PENDING".equalsIgnoreCase(leave.getStatus())) {
            throw new ValidationException("Leave request has already been processed (Current status: " + leave.getStatus() + ").");
        }

        leave.setStatus("REJECTED");
        leave.setActionReason(actionDTO != null ? actionDTO.getActionReason() : "Rejected by HR/Admin");
        leave.setApprovedBy(reviewerUsername);

        final LeaveRequest updated = leaveRepository.save(leave);
        return leaveMapper.toDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public LeaveRequestDTO getLeaveById(final Long leaveId) {
        final LeaveRequest leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("LeaveRequest", "id", leaveId));
        return leaveMapper.toDTO(leave);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<LeaveRequestDTO> getEmployeeLeaveHistory(final Long employeeId, final int pageNo, final int pageSize) {
        final Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("startDate").descending());
        final Page<LeaveRequest> page = leaveRepository.findByEmployeeEmployeeId(employeeId, pageable);
        final List<LeaveRequestDTO> content = page.getContent().stream().map(leaveMapper::toDTO).collect(Collectors.toList());

        return PagedResponse.<LeaveRequestDTO>builder()
                .content(content)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<LeaveRequestDTO> getLeaveRequestsByStatus(final String status, final int pageNo, final int pageSize) {
        final Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").ascending());
        final Page<LeaveRequest> page = leaveRepository.findByStatus(status, pageable);
        final List<LeaveRequestDTO> content = page.getContent().stream().map(leaveMapper::toDTO).collect(Collectors.toList());

        return PagedResponse.<LeaveRequestDTO>builder()
                .content(content)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public LeaveBalanceDTO getEmployeeLeaveBalance(final Long employeeId, final int year) {
        final Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        final Integer annualTaken = leaveRepository.sumApprovedLeaveDaysByTypeAndYear(employeeId, "ANNUAL", year);
        final Integer casualTaken = leaveRepository.sumApprovedLeaveDaysByTypeAndYear(employeeId, "CASUAL", year);
        final Integer medicalTaken = leaveRepository.sumApprovedLeaveDaysByTypeAndYear(employeeId, "MEDICAL", year);

        final int annualUsed = annualTaken != null ? annualTaken : 0;
        final int casualUsed = casualTaken != null ? casualTaken : 0;
        final int medicalUsed = medicalTaken != null ? medicalTaken : 0;

        return LeaveBalanceDTO.builder()
                .employeeId(employee.getEmployeeId())
                .employeeName(employee.getFirstName() + " " + employee.getLastName())
                .year(year)
                .annualQuota(ANNUAL_QUOTA)
                .annualTaken(annualUsed)
                .annualBalance(Math.max(0, ANNUAL_QUOTA - annualUsed))
                .casualQuota(CASUAL_QUOTA)
                .casualTaken(casualUsed)
                .casualBalance(Math.max(0, CASUAL_QUOTA - casualUsed))
                .medicalQuota(MEDICAL_QUOTA)
                .medicalTaken(medicalUsed)
                .medicalBalance(Math.max(0, MEDICAL_QUOTA - medicalUsed))
                .build();
    }

    /**
     * Calculates total business days excluding Saturday & Sunday.
     */
    private int calculateBusinessDays(final LocalDate start, final LocalDate end) {
        int businessDays = 0;
        LocalDate current = start;
        while (!current.isAfter(end)) {
            if (current.getDayOfWeek() != DayOfWeek.SATURDAY && current.getDayOfWeek() != DayOfWeek.SUNDAY) {
                businessDays++;
            }
            current = current.plusDays(1);
        }
        return Math.max(1, businessDays);
    }
}
