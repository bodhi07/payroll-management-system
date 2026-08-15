import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { payrollService } from '../services/payrollService';

/**
 * Custom Hook: usePayroll
 *
 * Encapsulates React Query cache & mutations for Payroll processing and Payslip generation.
 */
export const usePayroll = (month, year) => {
  const queryClient = useQueryClient();

  const currentMonth = month || (new Date().getMonth() + 1);
  const currentYear = year || new Date().getFullYear();

  const payrollQuery = useQuery({
    queryKey: ['payrollRun', currentMonth, currentYear],
    queryFn: () => payrollService.getPayrollRun(currentMonth, currentYear),
    staleTime: 1000 * 30,
  });

  const generatePayrollMutation = useMutation({
    mutationFn: (payload) => payrollService.generatePayroll(payload),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['payrollRun'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
      queryClient.invalidateQueries({ queryKey: ['reports'] });
    },
  });

  const markAsPaidMutation = useMutation({
    mutationFn: (id) => payrollService.markAsPaid(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['payrollRun'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
    },
  });

  const deletePayrollMutation = useMutation({
    mutationFn: (id) => payrollService.deletePayroll(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['payrollRun'] });
    },
  });

  return {
    ...payrollQuery,
    payrollData: payrollQuery.data,
    generatePayroll: generatePayrollMutation.mutateAsync,
    isGenerating: generatePayrollMutation.isPending,
    markAsPaid: markAsPaidMutation.mutateAsync,
    isMarkingPaid: markAsPaidMutation.isPending,
    deletePayroll: deletePayrollMutation.mutateAsync,
    isDeleting: deletePayrollMutation.isPending,
  };
};
