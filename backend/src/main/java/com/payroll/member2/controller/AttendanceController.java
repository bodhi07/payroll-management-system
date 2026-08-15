package com.payroll.member2.controller;

import com.payroll.member2.dto.AttendanceDTO;
import com.payroll.member2.dto.AttendanceReportDTO;
import com.payroll.member2.dto.CheckInDTO;
import com.payroll.member2.dto.CheckOutDTO;
import com.payroll.member2.service.AttendanceService;
import com.payroll.response.ApiResponse;
import com.payroll.response.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * ============================================================================
 * Member 02: Attendance REST Controller
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * REST controller exposing endpoints (`/api/v1/attendance`) for employee check-in,
 * check-out, working hours logging, and attendance reports.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/attendance")
@Tag(name = "Attendance Management", description = "Endpoints for employee check-in, check-out, and attendance reports.")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(final AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/check-in")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @Operation(summary = "Employee Check-In", description = "Logs check-in timestamp and computes late hours if after 08:30 AM.")
    public ResponseEntity<ApiResponse<AttendanceDTO>> checkIn(@Valid @RequestBody final CheckInDTO checkInDTO) {
        final AttendanceDTO dto = attendanceService.checkIn(checkInDTO);
        return new ResponseEntity<>(ApiResponse.success(HttpStatus.CREATED.value(), "Check-in successful", dto), HttpStatus.CREATED);
    }

    @PostMapping("/check-out")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @Operation(summary = "Employee Check-Out", description = "Logs check-out timestamp and computes working & overtime hours.")
    public ResponseEntity<ApiResponse<AttendanceDTO>> checkOut(@Valid @RequestBody final CheckOutDTO checkOutDTO) {
        final AttendanceDTO dto = attendanceService.checkOut(checkOutDTO);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Check-out successful", dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Get Attendance By ID", description = "Retrieves attendance record by ID.")
    public ResponseEntity<ApiResponse<AttendanceDTO>> getAttendanceById(@PathVariable("id") final Long id) {
        final AttendanceDTO dto = attendanceService.getAttendanceById(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Attendance record fetched", dto));
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @Operation(summary = "Get Employee Attendance Logs", description = "Fetches paginated attendance history for an employee.")
    public ResponseEntity<ApiResponse<PagedResponse<AttendanceDTO>>> getAttendanceByEmployee(
            @PathVariable("employeeId") final Long employeeId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) final int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) final int pageSize) {

        final PagedResponse<AttendanceDTO> response = attendanceService.getAttendanceByEmployee(employeeId, pageNo, pageSize);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Employee attendance logs fetched", response));
    }

    @GetMapping("/report")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @Operation(summary = "Get Attendance Summary Report", description = "Aggregates total present days, working hours, late hours, and overtime.")
    public ResponseEntity<ApiResponse<AttendanceReportDTO>> getAttendanceReport(
            @RequestParam("employeeId") final Long employeeId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate endDate) {

        final AttendanceReportDTO report = attendanceService.getAttendanceReport(employeeId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Attendance report generated successfully", report));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @Operation(summary = "Get Attendance Records By Date", description = "Retrieves attendance records for a specific date (defaults to today).")
    public ResponseEntity<ApiResponse<PagedResponse<AttendanceDTO>>> getAttendanceByDate(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate date,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) final int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "20", required = false) final int pageSize) {
        final LocalDate targetDate = date != null ? date : LocalDate.now();
        final PagedResponse<AttendanceDTO> response = attendanceService.getAttendanceByDate(targetDate, pageNo, pageSize);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Attendance records fetched for date: " + targetDate, response));
    }

    @GetMapping("/date/{date}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @Operation(summary = "Get Attendance By Path Date", description = "Retrieves attendance records for date in path.")
    public ResponseEntity<ApiResponse<PagedResponse<AttendanceDTO>>> getAttendanceByPathDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate date,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) final int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "20", required = false) final int pageSize) {
        final PagedResponse<AttendanceDTO> response = attendanceService.getAttendanceByDate(date, pageNo, pageSize);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Attendance records fetched for date: " + date, response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Attendance Record", description = "Deletes an attendance entry by ID.")
    public ResponseEntity<ApiResponse<Void>> deleteAttendance(@PathVariable("id") final Long id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Attendance record deleted"));
    }
}
