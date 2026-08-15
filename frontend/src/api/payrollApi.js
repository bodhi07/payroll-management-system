import axiosInstance from './axiosConfig';
import { ENDPOINTS } from '../constants/apiEndpoints';

/**
 * Payroll API Service
 *
 * Belongs to Member Module 3: Payroll Management (CRUD).
 * Connects with Spring Boot `/api/v1/payroll` endpoints to generate monthly payroll,
 * fetch itemized payslips, mark salaries as paid, and calculate budgets.
 */

export const payrollApi = {
  /**
   * Fetch details for month and year payroll run
   */
  getPayrollRun: async (month, year) => {
    const targetMonth = month || (new Date().getMonth() + 1);
    const targetYear = year || new Date().getFullYear();

    const response = await axiosInstance.get(ENDPOINTS.PAYROLL.BY_MONTH_YEAR, {
      params: { month: targetMonth, year: targetYear, pageNo: 0, pageSize: 100 },
    });

    const resData = response.data?.data || response.data;
    const content = resData?.content || (Array.isArray(resData) ? resData : []);

    const grossPay = content.reduce((sum, item) => sum + Number(item.grossSalary || 0), 0);
    const totalDeductions = content.reduce((sum, item) => sum + Number(item.totalDeduction || 0), 0);
    const netPayroll = content.reduce((sum, item) => sum + Number(item.netSalary || 0), 0);
    const taxWithholdings = content.reduce((sum, item) => sum + Number(item.tax || 0), 0);

    const employees = content.map((item) => {
      const basic = Number(item.basicSalary || 0);
      const allowance = Number(item.allowance || 0);
      const bonus = Number(item.bonus || 0);
      const gross = Number(item.grossSalary || (basic + allowance + bonus));
      const epfEmp = Number(item.epfEmployee || (basic * 0.08));
      const epfEmployer = Number(item.epfEmployer || (basic * 0.12));
      const etfEmployer = Number(item.etfEmployer || (basic * 0.03));
      const tax = Number(item.tax || 0);
      const deductions = Number(item.totalDeduction || (epfEmp + tax));
      const net = Number(item.netSalary || (gross - deductions));

      return {
        id: item.payrollId || item.id,
        payrollId: item.payrollId || item.id,
        employeeId: item.employeeNumber || `EMP-${item.employeeId}`,
        rawEmployeeId: item.employeeId,
        name: item.employeeName || `Employee ${item.employeeId}`,
        initials: (item.employeeName || 'EM').split(' ').map((n) => n[0]).join('').slice(0, 2).toUpperCase(),
        designation: item.designation || 'Staff',
        department: item.departmentName || 'General',
        baseSalary: basic,
        basicSalary: basic,
        allowance: allowance,
        bonus: bonus,
        grossSalary: gross,
        totalDeductions: deductions,
        deductions: -deductions,
        netPayable: net,
        netSalary: net,
        status: item.status || 'UNPAID',
        paidDate: item.paidDate,
        month: item.payMonth,
        year: item.payYear,
        payslipNumber: item.payslipNumber,
        payslip: {
          earnings: [
            { label: 'Basic Salary', amount: basic },
            { label: 'Allowances', amount: allowance },
            { label: 'Bonuses / Performance', amount: bonus },
          ],
          grossEarnings: gross,
          deductions: [
            { label: 'EPF Employee (8%)', amount: -epfEmp },
            { label: 'Tax (PAYE)', amount: -tax },
            { label: 'Loan / Advance', amount: -Number(item.loanDeduction || 0) },
          ],
          contributions: [
            { label: 'EPF Employer (12%)', amount: epfEmployer },
            { label: 'ETF Employer (3%)', amount: etfEmployer },
          ],
          totalDeductions: -deductions,
          netPayable: net,
        },
      };
    });

    return {
      month: targetMonth,
      year: targetYear,
      cycle: `Month ${String(targetMonth).padStart(2, '0')}/${targetYear} Payroll Cycle`,
      period: `Period: ${String(targetMonth).padStart(2, '0')}/01/${targetYear} - ${String(targetMonth).padStart(2, '0')}/30/${targetYear}`,
      metrics: {
        grossPay,
        grossChangePercent: 4.2,
        totalDeductions,
        netPayroll,
        taxWithholdings,
      },
      employees,
    };
  },

  /**
   * Calculate & generate payroll entry for an employee
   */
  generatePayroll: async (payload) => {
    const dto = {
      employeeId: Number(payload.employeeId),
      month: Number(payload.month || (new Date().getMonth() + 1)),
      year: Number(payload.year || new Date().getFullYear()),
      allowance: Number(payload.allowance || 0),
      bonus: Number(payload.bonus || 0),
      deductionReason: payload.deductionReason || 'Standard EPF & Tax Deductions',
      bonusDescription: payload.bonusDescription || 'Performance Bonus',
      allowanceBreakdown: payload.allowanceBreakdown || 'General Allowance',
    };

    const response = await axiosInstance.post(ENDPOINTS.PAYROLL.GENERATE, dto);
    return response.data?.data || response.data;
  },

  /**
   * Mark payroll as PAID
   */
  markAsPaid: async (id) => {
    const response = await axiosInstance.put(ENDPOINTS.PAYROLL.PAY(id));
    return response.data?.data || response.data;
  },

  /**
   * Fetch itemized payslip breakdown
   */
  getPayslip: async (payrollId) => {
    const response = await axiosInstance.get(ENDPOINTS.PAYROLL.PAYSLIP(payrollId));
    return response.data?.data || response.data;
  },

  /**
   * Get total monthly net salary budget
   */
  getTotalBudget: async (month, year) => {
    const response = await axiosInstance.get(ENDPOINTS.PAYROLL.TOTAL_BUDGET, {
      params: { month, year },
    });
    return response.data?.data || response.data;
  },

  /**
   * Delete payroll record
   */
  deletePayroll: async (id) => {
    const response = await axiosInstance.delete(ENDPOINTS.PAYROLL.BY_ID(id));
    return response.data?.data || response.data;
  },
};
