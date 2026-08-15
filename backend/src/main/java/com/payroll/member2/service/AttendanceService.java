package com.payroll.member2.service;

import com.payroll.member2.dto.AttendanceDTO;
import com.payroll.member2.dto.AttendanceReportDTO;
import com.payroll.member2.dto.CheckInDTO;
import com.payroll.member2.dto.CheckOutDTO;
import com.payroll.response.PagedResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * ============================================================================
 * Member 02: Attendance Service Interface
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Contract defining check-in, check-out, attendance CRUD, and attendance reporting logic.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public interface AttendanceService {

    AttendanceDTO checkIn(CheckInDTO checkInDTO);

    AttendanceDTO checkOut(CheckOutDTO checkOutDTO);

    AttendanceDTO getAttendanceById(Long id);

    PagedResponse<AttendanceDTO> getAttendanceByEmployee(Long employeeId, int pageNo, int pageSize);

    PagedResponse<AttendanceDTO> getAttendanceByDate(LocalDate date, int pageNo, int pageSize);

    AttendanceReportDTO getAttendanceReport(Long employeeId, LocalDate startDate, LocalDate endDate);

    void deleteAttendance(Long id);
}
