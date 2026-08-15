package com.payroll.member3.repository;

import com.payroll.member3.entity.Payroll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * Member 03: Payroll JPA Repository
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Data access abstraction layer for {@link Payroll} entity persistence and total budget queries.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    Optional<Payroll> findByEmployeeEmployeeIdAndPayMonthAndPayYear(Long employeeId, Integer payMonth, Integer payYear);

    boolean existsByEmployeeEmployeeIdAndPayMonthAndPayYear(Long employeeId, Integer payMonth, Integer payYear);

    Page<Payroll> findByEmployeeEmployeeId(Long employeeId, Pageable pageable);

    Page<Payroll> findByPayMonthAndPayYear(Integer payMonth, Integer payYear, Pageable pageable);

    List<Payroll> findByPayMonthAndPayYear(Integer payMonth, Integer payYear);

    @Query("SELECT SUM(p.netSalary) FROM Payroll p WHERE p.payMonth = :month AND p.payYear = :year")
    BigDecimal sumNetSalaryByMonthAndYear(@Param("month") Integer month, @Param("year") Integer year);
}
