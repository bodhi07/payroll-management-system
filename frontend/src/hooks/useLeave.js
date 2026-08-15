import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { leaveService } from '../services/leaveService';

/**
 * Custom Hook: useLeave
 *
 * Encapsulates live leave requests, application, approvals, and balance queries.
 */
export const useLeave = (status = 'PENDING', pageNo = 0, pageSize = 50) => {
  const queryClient = useQueryClient();

  const leaveQuery = useQuery({
    queryKey: ['leaveRequests', status, pageNo, pageSize],
    queryFn: () => leaveService.getLeavesByStatus(status, pageNo, pageSize),
    staleTime: 1000 * 30,
  });

  const applyMutation = useMutation({
    mutationFn: (data) => leaveService.applyLeave(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['leaveRequests'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
    },
  });

  const approveMutation = useMutation({
    mutationFn: ({ id, actionReason }) => leaveService.approveLeave(id, actionReason),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['leaveRequests'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
    },
  });

  const rejectMutation = useMutation({
    mutationFn: ({ id, actionReason }) => leaveService.rejectLeave(id, actionReason),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['leaveRequests'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
    },
  });

  return {
    ...leaveQuery,
    leaveRequests: leaveQuery.data?.data || [],
    totalRecords: leaveQuery.data?.total || 0,
    totalPages: leaveQuery.data?.totalPages || 1,
    applyLeave: applyMutation.mutateAsync,
    isApplying: applyMutation.isPending,
    approveLeave: approveMutation.mutateAsync,
    isApproving: approveMutation.isPending,
    rejectLeave: rejectMutation.mutateAsync,
    isRejecting: rejectMutation.isPending,
  };
};
