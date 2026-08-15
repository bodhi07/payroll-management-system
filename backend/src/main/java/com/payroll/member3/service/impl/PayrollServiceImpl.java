package com.payroll.member3.service.impl;

import com.payroll.exception.DuplicateResourceException;
import com.payroll.exception.ResourceNotFoundException;
import com.payroll.member1.entity.Employee;
import com.payroll.member1.repository.EmployeeRepository;
import com.payroll.member3.dto.GeneratePayrollDTO;
import com.payroll.member3.dto.PayrollDTO;
import com.payroll.member3.dto.PayslipDTO;
import com.payroll.member3.entity.Payroll;
import com.payroll.member3.entity.SalaryDetails;
import com.payroll.member3.mapper.PayrollMapper;
import com.payroll.member3.repository.PayrollRepository;
import com.payroll.member3.service.PayrollService;
import com.payroll.response.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ============================================================================
 * Member 03: Payroll Service Implementation
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Enterprise salary calculation engine. Implements precise calculations for:
 * - Gross Salary = Basic Salary + Allowance + Bonus
 * - EPF Employee = Basic Salary * 8% (0.08)
 * - EPF Employer = Basic Salary * 12% (0.12)
 * - ETF Employer = Basic Salary * 3% (0.03)
 * - Income Tax = Dynamic rate based on income brackets (e.g. 6% if Gross > 100k)
 * - Total Deductions = Tax + EPF Employee + Loan Deduction + Advance Deduction
 * - Net Salary = Gross Salary - Total Deductions
 * - Payslip Reference Generation
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Service
public class PayrollServiceImpl implements PayrollService {

    private static final BigDecimal EPF_EMPLOYEE_RATE = new BigDecimal("0.08"); // 8%
    private static final BigDecimal EPF_EMPLOYER_RATE = new BigDecimal("0.12"); // 12%
    private static final BigDecimal ETF_EMPLOYER_RATE = new BigDecimal("0.03"); // 3%
    private static final BigDecimal DEFAULT_TAX_RATE = new BigDecimal("0.06"); // 6% default tax bracket for high earners
    private static final BigDecimal TAX_THRESHOLD = new BigDecimal("100000.00");

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final PayrollMapper payrollMapper;

    public PayrollServiceImpl(final PayrollRepository payrollRepository,
                              final EmployeeRepository employeeRepository,
                              final PayrollMapper payrollMapper) {
        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
        this.payrollMapper = payrollMapper;
    }

    @Override
    @Transactional
    public PayrollDTO generatePayroll(final GeneratePayrollDTO request) {
        if (payrollRepository.existsByEmployeeEmployeeIdAndPayMonthAndPayYear(request.getEmployeeId(), request.getPayMonth(), request.getPayYear())) {
            throw new DuplicateResourceException("Payroll", "month/year", request.getPayMonth() + "/" + request.getPayYear());
        }

        final Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", request.getEmployeeId()));

        final BigDecimal basicSalary = employee.getBasicSalary();
        final BigDecimal allowance = request.getAllowance() != null ? request.getAllowance() : BigDecimal.ZERO;
        final BigDecimal bonus = request.getBonus() != null ? request.getBonus() : BigDecimal.ZERO;
        final BigDecimal loanDeduction = request.getLoanDeduction() != null ? request.getLoanDeduction() : BigDecimal.ZERO;
        final BigDecimal advanceDeduction = request.getAdvanceDeduction() != null ? request.getAdvanceDeduction() : BigDecimal.ZERO;

        // Gross Salary Calculation
        final BigDecimal grossSalary = basicSalary.add(allowance).add(bonus);

        // EPF & ETF Computations based on Basic Salary
        final BigDecimal epfEmployee = basicSalary.multiply(EPF_EMPLOYEE_RATE).setScale(2, RoundingMode.HALF_UP);
        final BigDecimal epfEmployer = basicSalary.multiply(EPF_EMPLOYER_RATE).setScale(2, RoundingMode.HALF_UP);
        final BigDecimal etfEmployer = basicSalary.multiply(ETF_EMPLOYER_RATE).setScale(2, RoundingMode.HALF_UP);

        // Income Tax Computation
        BigDecimal taxRate = DEFAULT_TAX_RATE;
        if (request.getCustomTaxPercentage() != null) {
            taxRate = BigDecimal.valueOf(request.getCustomTaxPercentage() / 100.0);
        } else if (grossSalary.compareTo(TAX_THRESHOLD) <= 0) {
            taxRate = BigDecimal.ZERO; // Exempt from income tax if below threshold
        }
        final BigDecimal tax = grossSalary.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);

        // Total Deductions Computation
        final BigDecimal totalDeduction = tax.add(epfEmployee).add(loanDeduction).add(advanceDeduction);

        // Net Salary Computation
        final BigDecimal netSalary = grossSalary.subtract(totalDeduction).setScale(2, RoundingMode.HALF_UP);

        // Generate Unique Payslip Tracking Number
        final String payslipNumber = String.format("PAYSLIP-%d-%02d-%d-%s",
                request.getPayYear(),
                request.getPayMonth(),
                employee.getEmployeeId(),
                UUID.randomUUID().toString().substring(0, 6).toUpperCase());

        final Payroll payroll = Payroll.builder()
                .employee(employee)
                .payMonth(request.getPayMonth())
                .payYear(request.getPayYear())
                .basicSalary(basicSalary)
                .allowance(allowance)
                .bonus(bonus)
                .grossSalary(grossSalary)
                .totalDeduction(totalDeduction)
                .tax(tax)
                .epfEmployee(epfEmployee)
                .epfEmployer(epfEmployer)
                .etfEmployer(etfEmployer)
                .loanDeduction(loanDeduction)
                .advanceDeduction(advanceDeduction)
                .netSalary(netSalary)
                .status("GENERATED")
                .build();

        final SalaryDetails details = SalaryDetails.builder()
                .payroll(payroll)
                .allowanceBreakdown(request.getAllowanceBreakdown())
                .bonusDescription(request.getBonusDescription())
                .deductionReason(request.getDeductionReason())
                .taxPercentage(taxRate.doubleValue() * 100.0)
                .payslipNumber(payslipNumber)
                .build();

        payroll.setSalaryDetails(details);

        final Payroll saved = payrollRepository.save(payroll);
        return payrollMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PayrollDTO getPayrollById(final Long payrollId) {
        final Payroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll", "id", payrollId));
        return payrollMapper.toDTO(payroll);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<PayrollDTO> getPayrollByEmployee(final Long employeeId, final int pageNo, final int pageSize) {
        final Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("payYear").descending().and(Sort.by("payMonth").descending()));
        final Page<Payroll> page = payrollRepository.findByEmployeeEmployeeId(employeeId, pageable);
        final List<PayrollDTO> content = page.getContent().stream().map(payrollMapper::toDTO).collect(Collectors.toList());

        return PagedResponse.<PayrollDTO>builder()
                .content(content)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<PayrollDTO> getPayrollByMonthAndYear(final Integer month, final Integer year, final int pageNo, final int pageSize) {
        final Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id").ascending());
        final Page<Payroll> page = payrollRepository.findByPayMonthAndPayYear(month, year, pageable);
        final List<PayrollDTO> content = page.getContent().stream().map(payrollMapper::toDTO).collect(Collectors.toList());

        return PagedResponse.<PayrollDTO>builder()
                .content(content)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    @Transactional
    public PayrollDTO markAsPaid(final Long payrollId) {
        final Payroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll", "id", payrollId));

        payroll.setStatus("PAID");
        payroll.setPaidDate(LocalDate.now());

        final Payroll updated = payrollRepository.save(payroll);
        return payrollMapper.toDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public PayslipDTO generatePayslip(final Long payrollId) {
        final Payroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll", "id", payrollId));

        final Employee emp = payroll.getEmployee();

        return PayslipDTO.builder()
                .payslipNumber(payroll.getSalaryDetails() != null ? payroll.getSalaryDetails().getPayslipNumber() : "N/A")
                .companyName("Enterprise Payroll Corp Inc.")
                .monthYear(String.format("%02d/%d", payroll.getPayMonth(), payroll.getPayYear()))
                .employeeId(emp.getEmployeeId())
                .employeeName(emp.getFirstName() + " " + emp.getLastName())
                .employeeNumber(emp.getEmployeeNumber())
                .designation(emp.getDesignation())
                .departmentName(emp.getDepartment() != null ? emp.getDepartment().getName() : "N/A")
                .nic(emp.getNic())
                .basicSalary(payroll.getBasicSalary())
                .allowance(payroll.getAllowance())
                .bonus(payroll.getBonus())
                .grossSalary(payroll.getGrossSalary())
                .tax(payroll.getTax())
                .epfEmployee(payroll.getEpfEmployee())
                .loanDeduction(payroll.getLoanDeduction())
                .advanceDeduction(payroll.getAdvanceDeduction())
                .totalDeductions(payroll.getTotalDeduction())
                .epfEmployer(payroll.getEpfEmployer())
                .etfEmployer(payroll.getEtfEmployer())
                .netSalary(payroll.getNetSalary())
                .generatedDate(LocalDate.now())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalNetSalaryBudgetByMonthAndYear(final Integer month, final Integer year) {
        final BigDecimal total = payrollRepository.sumNetSalaryByMonthAndYear(month, year);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    @Transactional
    public void deletePayroll(final Long payrollId) {
        if (!payrollRepository.existsById(payrollId)) {
            throw new ResourceNotFoundException("Payroll", "id", payrollId);
        }
        payrollRepository.deleteById(payrollId);
    }
}
