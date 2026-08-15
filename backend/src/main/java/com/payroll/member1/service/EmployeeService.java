package com.payroll.member1.service;

import com.payroll.member1.dto.EmployeeDTO;
import com.payroll.response.PagedResponse;

import java.util.List;

/**
 * ============================================================================
 * Member 01: Employee Service Interface
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Contract defining employee CRUD, profile retrieval, search, and filtering options.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public interface EmployeeService {

    EmployeeDTO createEmployee(EmployeeDTO dto);

    EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO dto);

    EmployeeDTO getEmployeeById(Long employeeId);

    EmployeeDTO getEmployeeByNumber(String employeeNumber);

    PagedResponse<EmployeeDTO> getAllEmployees(int pageNo, int pageSize, String sortBy, String sortDir);

    PagedResponse<EmployeeDTO> searchEmployees(String query, int pageNo, int pageSize, String sortBy, String sortDir);

    PagedResponse<EmployeeDTO> filterEmployeesByDepartment(Long departmentId, int pageNo, int pageSize);

    PagedResponse<EmployeeDTO> filterEmployeesByStatus(String status, int pageNo, int pageSize);

    void deleteEmployee(Long employeeId);

    List<EmployeeDTO> linearSearchEmployeesByName(String keyword);
}
