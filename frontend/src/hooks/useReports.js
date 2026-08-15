import { useQuery } from '@tanstack/react-query';
import { reportService } from '../services/reportService';

/**
 * Custom Hook: useReports
 */
export const useReports = (params = {}) => {
  return useQuery({
    queryKey: ['monthlyExpenseReport', params],
    queryFn: () => reportService.getMonthlyExpenseReport(params),
    staleTime: 1000 * 30,
  });
};
