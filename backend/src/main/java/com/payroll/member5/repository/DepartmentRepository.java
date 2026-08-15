package com.payroll.member5.repository;

import com.payroll.member5.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ============================================================================
 * Member 05: Department JPA Repository
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Data access abstraction layer for {@link Department} database interactions.
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
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByCode(String code);

    Optional<Department> findByName(String name);

    boolean existsByCode(String code);

    boolean existsByName(String name);
}
