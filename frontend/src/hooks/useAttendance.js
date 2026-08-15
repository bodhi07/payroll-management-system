import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { attendanceService } from '../services/attendanceService';

/**
 * Custom Hook: useAttendance
 *
 * Encapsulates live shift log querying, check-in, check-out, and reports.
 */
export const useAttendance = (date, pageNo = 0, pageSize = 20) => {
  const queryClient = useQueryClient();
  const targetDate = date || new Date().toISOString().slice(0, 10);

  const attendanceQuery = useQuery({
    queryKey: ['attendance', targetDate, pageNo, pageSize],
    queryFn: () => attendanceService.getAttendanceByDate(targetDate, pageNo, pageSize),
    staleTime: 1000 * 30,
  });

  const checkInMutation = useMutation({
    mutationFn: (data) => attendanceService.checkIn(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['attendance'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
    },
  });

  const checkOutMutation = useMutation({
    mutationFn: (data) => attendanceService.checkOut(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['attendance'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
    },
  });

  const deleteAttendanceMutation = useMutation({
    mutationFn: (id) => attendanceService.deleteAttendance(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['attendance'] });
    },
  });

  return {
    ...attendanceQuery,
    attendanceLogs: attendanceQuery.data?.data || [],
    totalRecords: attendanceQuery.data?.total || 0,
    totalPages: attendanceQuery.data?.totalPages || 1,
    checkIn: checkInMutation.mutateAsync,
    isCheckingIn: checkInMutation.isPending,
    checkOut: checkOutMutation.mutateAsync,
    isCheckingOut: checkOutMutation.isPending,
    deleteAttendance: deleteAttendanceMutation.mutateAsync,
  };
};
