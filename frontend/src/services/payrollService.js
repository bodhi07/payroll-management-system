import { payrollApi } from '../api/payrollApi';

export const payrollService = {
  getPayrollRun: async (month, year) => {
    return await payrollApi.getPayrollRun(month, year);
  },

  generatePayroll: async (payload) => {
    return await payrollApi.generatePayroll(payload);
  },

  markAsPaid: async (id) => {
    return await payrollApi.markAsPaid(id);
  },

  getPayslip: async (payrollId) => {
    return await payrollApi.getPayslip(payrollId);
  },

  getTotalBudget: async (month, year) => {
    return await payrollApi.getTotalBudget(month, year);
  },

  deletePayroll: async (id) => {
    return await payrollApi.deletePayroll(id);
  },
};
