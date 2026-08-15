package com.payroll.member3.service;

import com.payroll.member3.dto.GeneratePayrollDTO;
import com.payroll.member3.dto.PayrollDTO;
import com.payroll.member3.dto.PayslipDTO;
import com.payroll.response.PagedResponse;

import java.math.BigDecimal;

/**
 * ============================================================================
 * Member 03: Payroll Service Interface
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Contract for monthly salary calculation, EPF/ETF contributions, Tax deductions,
 * payslip generation, and payroll processing.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public interface PayrollService {

    PayrollDTO generatePayroll(GeneratePayrollDTO requestDTO);

    PayrollDTO getPayrollById(Long payrollId);

    PagedResponse<PayrollDTO> getPayrollByEmployee(Long employeeId, int pageNo, int pageSize);

    PagedResponse<PayrollDTO> getPayrollByMonthAndYear(Integer month, Integer year, int pageNo, int pageSize);

    PayrollDTO markAsPaid(Long payrollId);

    PayslipDTO generatePayslip(Long payrollId);

    BigDecimal getTotalNetSalaryBudgetByMonthAndYear(Integer month, Integer year);

    void deletePayroll(Long payrollId);
}
