package com.payroll.member4.repository;

import com.payroll.member4.entity.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * ============================================================================
 * Member 04: Leave JPA Repository
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Data access abstraction layer for {@link LeaveRequest} persistence and balance calculations.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Repository
public interface LeaveRepository extends JpaRepository<LeaveRequest, Long> {

    Page<LeaveRequest> findByEmployeeEmployeeId(Long employeeId, Pageable pageable);

    Page<LeaveRequest> findByStatus(String status, Pageable pageable);

    List<LeaveRequest> findByEmployeeEmployeeIdAndStatus(Long employeeId, String status);

    @Query("SELECT SUM(l.totalDays) FROM LeaveRequest l WHERE l.employee.employeeId = :empId AND l.leaveType = :leaveType AND l.status = 'APPROVED' AND YEAR(l.startDate) = :year")
    Integer sumApprovedLeaveDaysByTypeAndYear(@Param("empId") Long empId, @Param("leaveType") String leaveType, @Param("year") int year);

    @Query("SELECT COUNT(l) > 0 FROM LeaveRequest l WHERE l.employee.employeeId = :empId AND l.status IN ('PENDING', 'APPROVED') AND ((:start BETWEEN l.startDate AND l.endDate) OR (:end BETWEEN l.startDate AND l.endDate) OR (l.startDate BETWEEN :start AND :end))")
    boolean existsOverlappingLeave(@Param("empId") Long empId, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
