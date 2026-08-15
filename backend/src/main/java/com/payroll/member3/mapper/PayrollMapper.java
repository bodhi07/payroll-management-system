package com.payroll.member3.mapper;

import com.payroll.member3.dto.PayrollDTO;
import com.payroll.member3.dto.SalaryDetailsDTO;
import com.payroll.member3.entity.Payroll;
import com.payroll.member3.entity.SalaryDetails;
import org.springframework.stereotype.Component;

/**
 * ============================================================================
 * Member 03: Payroll Mapper Component
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Converts between Payroll/SalaryDetails Entities and DTOs.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Component
public class PayrollMapper {

    public PayrollDTO toDTO(final Payroll payroll) {
        if (payroll == null) return null;
        return PayrollDTO.builder()
                .id(payroll.getId())
                .employeeId(payroll.getEmployee() != null ? payroll.getEmployee().getEmployeeId() : null)
                .employeeName(payroll.getEmployee() != null ? payroll.getEmployee().getFirstName() + " " + payroll.getEmployee().getLastName() : null)
                .employeeNumber(payroll.getEmployee() != null ? payroll.getEmployee().getEmployeeNumber() : null)
                .departmentName(payroll.getEmployee() != null && payroll.getEmployee().getDepartment() != null ? payroll.getEmployee().getDepartment().getName() : null)
                .payMonth(payroll.getPayMonth())
                .payYear(payroll.getPayYear())
                .basicSalary(payroll.getBasicSalary())
                .allowance(payroll.getAllowance())
                .bonus(payroll.getBonus())
                .grossSalary(payroll.getGrossSalary())
                .totalDeduction(payroll.getTotalDeduction())
                .tax(payroll.getTax())
                .epfEmployee(payroll.getEpfEmployee())
                .epfEmployer(payroll.getEpfEmployer())
                .etfEmployer(payroll.getEtfEmployer())
                .loanDeduction(payroll.getLoanDeduction())
                .advanceDeduction(payroll.getAdvanceDeduction())
                .netSalary(payroll.getNetSalary())
                .status(payroll.getStatus())
                .paidDate(payroll.getPaidDate())
                .salaryDetails(toDetailsDTO(payroll.getSalaryDetails()))
                .createdAt(payroll.getCreatedAt())
                .updatedAt(payroll.getUpdatedAt())
                .build();
    }

    public SalaryDetailsDTO toDetailsDTO(final SalaryDetails details) {
        if (details == null) return null;
        return SalaryDetailsDTO.builder()
                .id(details.getId())
                .allowanceBreakdown(details.getAllowanceBreakdown())
                .bonusDescription(details.getBonusDescription())
                .deductionReason(details.getDeductionReason())
                .taxPercentage(details.getTaxPercentage())
                .payslipNumber(details.getPayslipNumber())
                .createdAt(details.getCreatedAt())
                .build();
    }
}
