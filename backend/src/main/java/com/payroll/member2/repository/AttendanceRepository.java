package com.payroll.member2.repository;

import com.payroll.member2.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * Member 02: Attendance JPA Repository
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Data access layer for {@link Attendance} record query processing, date-range filtering,
 * and monthly overtime totals.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByEmployeeEmployeeIdAndDate(Long employeeId, LocalDate date);

    boolean existsByEmployeeEmployeeIdAndDate(Long employeeId, LocalDate date);

    Page<Attendance> findByEmployeeEmployeeId(Long employeeId, Pageable pageable);

    List<Attendance> findByEmployeeEmployeeIdAndDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);

    Page<Attendance> findByDate(LocalDate date, Pageable pageable);

    @Query("SELECT SUM(a.workingHours) FROM Attendance a WHERE a.employee.employeeId = :empId AND a.date BETWEEN :start AND :end")
    Double sumWorkingHoursByEmployeeAndDateBetween(@Param("empId") Long empId, @Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT SUM(a.overtimeHours) FROM Attendance a WHERE a.employee.employeeId = :empId AND a.date BETWEEN :start AND :end")
    Double sumOvertimeHoursByEmployeeAndDateBetween(@Param("empId") Long empId, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
