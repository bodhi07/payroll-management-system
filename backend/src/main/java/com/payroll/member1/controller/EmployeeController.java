package com.payroll.member1.controller;

import com.payroll.member1.dto.EmployeeDTO;
import com.payroll.member1.service.EmployeeService;
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
 * Member 01: Employee Management REST Controller
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * REST controller exposing endpoints (`/api/v1/employees`) for employee profile CRUD,
 * pagination, department filtering, status filtering, and linear search.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@RestController
@RequestMapping({"/api/v1/employees", "/api/v1/employee"})
@Tag(name = "Employee Management", description = "Endpoints for employee CRUD, profile management, and search.")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(final EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Create Employee", description = "Creates a new employee record.")
    public ResponseEntity<ApiResponse<EmployeeDTO>> createEmployee(@Valid @RequestBody final EmployeeDTO dto) {
        final EmployeeDTO created = employeeService.createEmployee(dto);
        return new ResponseEntity<>(ApiResponse.success(HttpStatus.CREATED.value(), "Employee created successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Update Employee", description = "Updates an existing employee profile.")
    public ResponseEntity<ApiResponse<EmployeeDTO>> updateEmployee(@PathVariable("id") final Long id,
                                                                      @Valid @RequestBody final EmployeeDTO dto) {
        final EmployeeDTO updated = employeeService.updateEmployee(id, dto);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Employee updated successfully", updated));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @Operation(summary = "Get Employee By ID", description = "Fetches employee details by primary key ID.")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeById(@PathVariable("id") final Long id) {
        final EmployeeDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Employee fetched successfully", employee));
    }

    @GetMapping("/number/{employeeNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @Operation(summary = "Get Employee By Number", description = "Fetches employee details by unique employee number.")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeByNumber(@PathVariable("employeeNumber") final String employeeNumber) {
        final EmployeeDTO employee = employeeService.getEmployeeByNumber(employeeNumber);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Employee fetched successfully", employee));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Get All Employees Paginated", description = "Retrieves paginated employee records.")
    public ResponseEntity<ApiResponse<PagedResponse<EmployeeDTO>>> getAllEmployees(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) final int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) final int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "employeeId", required = false) final String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) final String sortDir) {

        final PagedResponse<EmployeeDTO> response = employeeService.getAllEmployees(pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Employees fetched successfully", response));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Search Employees", description = "Searches employees by name, email, NIC, or number.")
    public ResponseEntity<ApiResponse<PagedResponse<EmployeeDTO>>> searchEmployees(
            @RequestParam("query") final String query,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) final int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) final int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "employeeId", required = false) final String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) final String sortDir) {

        final PagedResponse<EmployeeDTO> response = employeeService.searchEmployees(query, pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Employee search completed", response));
    }

    @GetMapping("/filter/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Filter Employees By Department", description = "Fetches employees belonging to a department.")
    public ResponseEntity<ApiResponse<PagedResponse<EmployeeDTO>>> filterByDepartment(
            @PathVariable("departmentId") final Long departmentId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) final int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) final int pageSize) {

        final PagedResponse<EmployeeDTO> response = employeeService.filterEmployeesByDepartment(departmentId, pageNo, pageSize);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Employees filtered by department", response));
    }

    @GetMapping("/filter/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Filter Employees By Status", description = "Fetches employees by status (ACTIVE, INACTIVE, TERMINATED).")
    public ResponseEntity<ApiResponse<PagedResponse<EmployeeDTO>>> filterByStatus(
            @PathVariable("status") final String status,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) final int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) final int pageSize) {

        final PagedResponse<EmployeeDTO> response = employeeService.filterEmployeesByStatus(status, pageNo, pageSize);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Employees filtered by status", response));
    }

    @GetMapping("/linear-search")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Linear Search Employees", description = "Executes custom Linear Search algorithm over employee list.")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> linearSearch(@RequestParam("keyword") final String keyword) {
        final List<EmployeeDTO> results = employeeService.linearSearchEmployeesByName(keyword);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Linear search completed successfully", results));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Employee", description = "Deletes an employee record by ID.")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable("id") final Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Employee deleted successfully"));
    }
}
