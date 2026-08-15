package com.payroll.member5.service;

import com.payroll.member5.dto.DepartmentDTO;
import com.payroll.member5.dto.DepartmentReportDTO;
import com.payroll.response.PagedResponse;

import java.util.List;

/**
 * ============================================================================
 * Member 05: Department Service Interface
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Contract defining department CRUD and department analytics reporting operations.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public interface DepartmentService {

    DepartmentDTO createDepartment(DepartmentDTO dto);

    DepartmentDTO updateDepartment(Long id, DepartmentDTO dto);

    DepartmentDTO getDepartmentById(Long id);

    DepartmentDTO getDepartmentByCode(String code);

    PagedResponse<DepartmentDTO> getAllDepartments(int pageNo, int pageSize, String sortBy, String sortDir);

    void deleteDepartment(Long id);

    DepartmentReportDTO getDepartmentReport(Long departmentId);

    List<DepartmentReportDTO> getAllDepartmentReports();
}
