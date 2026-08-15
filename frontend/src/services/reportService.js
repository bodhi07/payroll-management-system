import { reportApi } from '../api/reportApi';

export const reportService = {
  getMonthlyExpenseReport: async (params) => {
    return await reportApi.getMonthlyExpenseReport(params);
  },
};
