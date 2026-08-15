package com.payroll.member3.controller;

import com.payroll.member3.dto.GeneratePayrollDTO;
import com.payroll.member3.dto.PayrollDTO;
import com.payroll.member3.dto.PayslipDTO;
import com.payroll.member3.service.PayrollService;
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

import java.math.BigDecimal;

/**
 * ============================================================================
 * Member 03: Payroll Management REST Controller
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Exposes REST endpoints (`/api/v1/payroll`) for monthly salary generation, payslip creation,
 * mark-as-paid status updates, and monthly budget aggregation queries.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/payroll")
@Tag(name = "Payroll Management", description = "Endpoints for monthly salary calculation, payslip generation, and EPF/ETF contributions.")
public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(final PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @PostMapping("/generate")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Calculate & Generate Monthly Payroll", description = "Calculates Gross Salary, EPF (8%/12%), ETF (3%), Tax, and Net Salary.")
    public ResponseEntity<ApiResponse<PayrollDTO>> generatePayroll(@Valid @RequestBody final GeneratePayrollDTO requestDTO) {
        final PayrollDTO payroll = payrollService.generatePayroll(requestDTO);
        return new ResponseEntity<>(ApiResponse.success(HttpStatus.CREATED.value(), "Payroll generated successfully", payroll), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @Operation(summary = "Get Payroll Record By ID", description = "Retrieves payroll summary by ID.")
    public ResponseEntity<ApiResponse<PayrollDTO>> getPayrollById(@PathVariable("id") final Long id) {
        final PayrollDTO payroll = payrollService.getPayrollById(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Payroll record retrieved", payroll));
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @Operation(summary = "Get Employee Payroll History", description = "Retrieves paginated salary history for an employee.")
    public ResponseEntity<ApiResponse<PagedResponse<PayrollDTO>>> getPayrollByEmployee(
            @PathVariable("employeeId") final Long employeeId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) final int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) final int pageSize) {

        final PagedResponse<PayrollDTO> response = payrollService.getPayrollByEmployee(employeeId, pageNo, pageSize);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Employee payroll history retrieved", response));
    }

    @GetMapping("/month-year")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Get Payroll By Month & Year", description = "Retrieves paginated payroll records for a specific month and year.")
    public ResponseEntity<ApiResponse<PagedResponse<PayrollDTO>>> getPayrollByMonthAndYear(
            @RequestParam("month") final Integer month,
            @RequestParam("year") final Integer year,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) final int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) final int pageSize) {

        final PagedResponse<PayrollDTO> response = payrollService.getPayrollByMonthAndYear(month, year, pageNo, pageSize);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Monthly payroll records retrieved", response));
    }

    @PutMapping("/{id}/pay")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Mark Payroll As Paid", description = "Updates payroll status to PAID and sets payment date.")
    public ResponseEntity<ApiResponse<PayrollDTO>> markAsPaid(@PathVariable("id") final Long id) {
        final PayrollDTO updated = payrollService.markAsPaid(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Payroll marked as PAID", updated));
    }

    @GetMapping("/{id}/payslip")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @Operation(summary = "Generate Payslip", description = "Generates formatted itemized payslip breakdown for print/PDF.")
    public ResponseEntity<ApiResponse<PayslipDTO>> generatePayslip(@PathVariable("id") final Long id) {
        final PayslipDTO payslip = payrollService.generatePayslip(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Payslip generated successfully", payslip));
    }

    @GetMapping("/total-budget")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Get Total Monthly Net Salary Budget", description = "Aggregates total net salary expenditure for a month and year.")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalBudget(@RequestParam("month") final Integer month,
                                                                   @RequestParam("year") final Integer year) {
        final BigDecimal totalBudget = payrollService.getTotalNetSalaryBudgetByMonthAndYear(month, year);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Total monthly net salary budget calculated", totalBudget));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Payroll Record", description = "Deletes a payroll record by ID.")
    public ResponseEntity<ApiResponse<Void>> deletePayroll(@PathVariable("id") final Long id) {
        payrollService.deletePayroll(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Payroll record deleted successfully"));
    }
}
