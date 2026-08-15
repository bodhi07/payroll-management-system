package com.payroll.member5.controller;

import com.payroll.member5.dto.DepartmentDTO;
import com.payroll.member5.dto.DepartmentReportDTO;
import com.payroll.member5.service.DepartmentService;
import com.payroll.response.ApiResponse;
import com.payroll.response.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ============================================================================
 * Member 05: Department Management REST Controller
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Exposes REST API endpoints (`/api/v1/departments`) for department CRUD operations,
 * employee count metrics, and department salary budget aggregation reports.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@RestController
@RequestMapping({"/api/v1/departments", "/api/v1/department"})
@Tag(name = "Department Management", description = "Endpoints for department CRUD and budget/headcount reports.")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(final DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create Department", description = "Creates a new department record.")
    public ResponseEntity<ApiResponse<DepartmentDTO>> createDepartment(@Valid @RequestBody final DepartmentDTO dto) {
        final DepartmentDTO created = departmentService.createDepartment(dto);
        return new ResponseEntity<>(ApiResponse.success(HttpStatus.CREATED.value(), "Department created successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update Department", description = "Updates an existing department.")
    public ResponseEntity<ApiResponse<DepartmentDTO>> updateDepartment(@PathVariable("id") final Long id,
                                                                          @Valid @RequestBody final DepartmentDTO dto) {
        final DepartmentDTO updated = departmentService.updateDepartment(id, dto);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Department updated successfully", updated));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Get Department By ID", description = "Fetches department details by ID.")
    public ResponseEntity<ApiResponse<DepartmentDTO>> getDepartmentById(@PathVariable("id") final Long id) {
        final DepartmentDTO department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Department fetched successfully", department));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @Operation(summary = "Get All Departments Paginated", description = "Retrieves paginated list of departments.")
    public ResponseEntity<ApiResponse<PagedResponse<DepartmentDTO>>> getAllDepartments(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) final int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) final int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) final String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) final String sortDir) {

        final PagedResponse<DepartmentDTO> response = departmentService.getAllDepartments(pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Departments fetched successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Department", description = "Deletes a department by ID.")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable("id") final Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Department deleted successfully"));
    }

    @GetMapping("/{id}/report")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Get Department Report", description = "Calculates employee count and total department basic salary budget.")
    public ResponseEntity<ApiResponse<DepartmentReportDTO>> getDepartmentReport(@PathVariable("id") final Long id) {
        final DepartmentReportDTO report = departmentService.getDepartmentReport(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Department report generated successfully", report));
    }

    @GetMapping("/reports")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Get All Department Reports", description = "Returns aggregated reports for all departments.")
    public ResponseEntity<ApiResponse<List<DepartmentReportDTO>>> getAllDepartmentReports() {
        final List<DepartmentReportDTO> reports = departmentService.getAllDepartmentReports();
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "All department reports generated successfully", reports));
    }
}
