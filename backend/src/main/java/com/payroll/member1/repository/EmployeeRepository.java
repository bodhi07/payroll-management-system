package com.payroll.member1.repository;

import com.payroll.member1.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * Member 01: Employee JPA Repository
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Data access layer for {@link Employee} persistence, filtering by status/department,
 * and custom search queries.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Interface Realization: Extends {@link JpaRepository}.
 * 
 * Design Patterns Used:
 * --------------------
 * - Repository Pattern.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeNumber(String employeeNumber);

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByNic(String nic);

    boolean existsByEmployeeNumber(String employeeNumber);

    boolean existsByEmail(String email);

    boolean existsByNic(String nic);

    Page<Employee> findByDepartmentId(Long departmentId, Pageable pageable);

    Page<Employee> findByStatus(String status, Pageable pageable);

    /**
     * Search employees by query string matching first name, last name, email, NIC, or employee number.
     */
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.nic) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.employeeNumber) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Employee> searchEmployees(@Param("query") String query, Pageable pageable);

    /**
     * Fetch all employees belonging to a department.
     */
    List<Employee> findByDepartmentId(Long departmentId);
}
