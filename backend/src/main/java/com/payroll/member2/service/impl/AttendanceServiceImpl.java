package com.payroll.member2.service.impl;

import com.payroll.exception.DuplicateResourceException;
import com.payroll.exception.ResourceNotFoundException;
import com.payroll.exception.ValidationException;
import com.payroll.member1.entity.Employee;
import com.payroll.member1.repository.EmployeeRepository;
import com.payroll.member2.dto.AttendanceDTO;
import com.payroll.member2.dto.AttendanceReportDTO;
import com.payroll.member2.dto.CheckInDTO;
import com.payroll.member2.dto.CheckOutDTO;
import com.payroll.member2.entity.Attendance;
import com.payroll.member2.mapper.AttendanceMapper;
import com.payroll.member2.repository.AttendanceRepository;
import com.payroll.member2.service.AttendanceService;
import com.payroll.response.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ============================================================================
 * Member 02: Attendance Service Implementation
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Implements check-in, check-out, working hour calculations, late arrival tracking,
 * and overtime logic.
 * 
 * Shift Rules:
 * -----------
 * - Standard Start Time: 08:30:00 AM
 * - Standard Work Day: 8.0 Hours
 * - Overtime = Max(0, Working Hours - 8.0)
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Service
public class AttendanceServiceImpl implements AttendanceService {

    private static final LocalTime STANDARD_START_TIME = LocalTime.of(8, 30);
    private static final double STANDARD_WORK_HOURS = 8.0;

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceMapper attendanceMapper;

    public AttendanceServiceImpl(final AttendanceRepository attendanceRepository,
                                 final EmployeeRepository employeeRepository,
                                 final AttendanceMapper attendanceMapper) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
        this.attendanceMapper = attendanceMapper;
    }

    @Override
    @Transactional
    public AttendanceDTO checkIn(final CheckInDTO checkInDTO) {
        final LocalDate targetDate = checkInDTO.getDate() != null ? checkInDTO.getDate() : LocalDate.now();
        final LocalTime checkInTime = checkInDTO.getCheckInTime() != null ? checkInDTO.getCheckInTime() : LocalTime.now();

        if (attendanceRepository.existsByEmployeeEmployeeIdAndDate(checkInDTO.getEmployeeId(), targetDate)) {
            throw new DuplicateResourceException("Attendance", "date", targetDate);
        }

        final Employee employee = employeeRepository.findById(checkInDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", checkInDTO.getEmployeeId()));

        // Calculate late hours if check-in is after standard 08:30 AM
        double lateHours = 0.0;
        String status = "PRESENT";
        if (checkInTime.isAfter(STANDARD_START_TIME)) {
            final long lateMinutes = Duration.between(STANDARD_START_TIME, checkInTime).toMinutes();
            lateHours = Math.round((lateMinutes / 60.0) * 100.0) / 100.0;
            status = "LATE";
        }

        final Attendance attendance = Attendance.builder()
                .employee(employee)
                .date(targetDate)
                .checkInTime(checkInTime)
                .workingHours(0.0)
                .lateHours(lateHours)
                .overtimeHours(0.0)
                .status(status)
                .build();

        final Attendance saved = attendanceRepository.save(attendance);
        return attendanceMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public AttendanceDTO checkOut(final CheckOutDTO checkOutDTO) {
        final LocalDate targetDate = checkOutDTO.getDate() != null ? checkOutDTO.getDate() : LocalDate.now();
        final LocalTime checkOutTime = checkOutDTO.getCheckOutTime() != null ? checkOutDTO.getCheckOutTime() : LocalTime.now();

        final Attendance attendance = attendanceRepository.findByEmployeeEmployeeIdAndDate(checkOutDTO.getEmployeeId(), targetDate)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found for check-out on date: " + targetDate));

        if (attendance.getCheckInTime() == null) {
            throw new ValidationException("Cannot check out without a valid check-in entry.");
        }

        if (checkOutTime.isBefore(attendance.getCheckInTime())) {
            throw new ValidationException("Check-out time cannot be earlier than check-in time.");
        }

        // Calculate total working hours
        final long minutesWorked = Duration.between(attendance.getCheckInTime(), checkOutTime).toMinutes();
        final double workingHours = Math.round((minutesWorked / 60.0) * 100.0) / 100.0;

        // Calculate overtime hours worked beyond 8.0 standard shift
        double overtimeHours = 0.0;
        if (workingHours > STANDARD_WORK_HOURS) {
            overtimeHours = Math.round((workingHours - STANDARD_WORK_HOURS) * 100.0) / 100.0;
        }

        attendance.setCheckOutTime(checkOutTime);
        attendance.setWorkingHours(workingHours);
        attendance.setOvertimeHours(overtimeHours);

        final Attendance updated = attendanceRepository.save(attendance);
        return attendanceMapper.toDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceDTO getAttendanceById(final Long id) {
        final Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance", "id", id));
        return attendanceMapper.toDTO(attendance);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<AttendanceDTO> getAttendanceByEmployee(final Long employeeId, final int pageNo, final int pageSize) {
        final Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("date").descending());
        final Page<Attendance> page = attendanceRepository.findByEmployeeEmployeeId(employeeId, pageable);
        final List<AttendanceDTO> content = page.getContent().stream().map(attendanceMapper::toDTO).collect(Collectors.toList());

        return PagedResponse.<AttendanceDTO>builder()
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
    public PagedResponse<AttendanceDTO> getAttendanceByDate(final LocalDate date, final int pageNo, final int pageSize) {
        final Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id").ascending());
        final Page<Attendance> page = attendanceRepository.findByDate(date, pageable);
        final List<AttendanceDTO> content = page.getContent().stream().map(attendanceMapper::toDTO).collect(Collectors.toList());

        return PagedResponse.<AttendanceDTO>builder()
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
    public AttendanceReportDTO getAttendanceReport(final Long employeeId, final LocalDate startDate, final LocalDate endDate) {
        final Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        final List<Attendance> records = attendanceRepository.findByEmployeeEmployeeIdAndDateBetween(employeeId, startDate, endDate);

        long presentCount = 0;
        long lateCount = 0;
        double totalWorkHours = 0.0;
        double totalLateHours = 0.0;
        double totalOTHours = 0.0;

        for (final Attendance att : records) {
            presentCount++;
            if ("LATE".equalsIgnoreCase(att.getStatus())) {
                lateCount++;
            }
            if (att.getWorkingHours() != null) totalWorkHours += att.getWorkingHours();
            if (att.getLateHours() != null) totalLateHours += att.getLateHours();
            if (att.getOvertimeHours() != null) totalOTHours += att.getOvertimeHours();
        }

        return AttendanceReportDTO.builder()
                .employeeId(employee.getEmployeeId())
                .employeeName(employee.getFirstName() + " " + employee.getLastName())
                .employeeNumber(employee.getEmployeeNumber())
                .startDate(startDate)
                .endDate(endDate)
                .totalPresentDays(presentCount)
                .totalLateDays(lateCount)
                .totalWorkingHours(Math.round(totalWorkHours * 100.0) / 100.0)
                .totalLateHours(Math.round(totalLateHours * 100.0) / 100.0)
                .totalOvertimeHours(Math.round(totalOTHours * 100.0) / 100.0)
                .build();
    }

    @Override
    @Transactional
    public void deleteAttendance(final Long id) {
        if (!attendanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Attendance", "id", id);
        }
        attendanceRepository.deleteById(id);
    }
}
