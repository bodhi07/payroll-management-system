import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { employeeService } from '../services/employeeService';

/**
 * Custom Hook: useEmployees
 *
 * Encapsulates React Query cache, fetching, and mutations for Employee Management.
 */
export const useEmployees = (params = {}) => {
  const queryClient = useQueryClient();

  const employeesQuery = useQuery({
    queryKey: ['employees', params],
    queryFn: () => employeeService.getEmployees(params),
    staleTime: 1000 * 30, // 30 seconds
  });

  const createEmployeeMutation = useMutation({
    mutationFn: (newEmployee) => employeeService.createEmployee(newEmployee),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['employees'] });
      queryClient.invalidateQueries({ queryKey: ['departments'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
    },
  });

  const updateEmployeeMutation = useMutation({
    mutationFn: ({ id, data }) => employeeService.updateEmployee(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['employees'] });
      queryClient.invalidateQueries({ queryKey: ['departments'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
    },
  });

  const deleteEmployeeMutation = useMutation({
    mutationFn: (id) => employeeService.deleteEmployee(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['employees'] });
      queryClient.invalidateQueries({ queryKey: ['departments'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
    },
  });

  return {
    ...employeesQuery,
    employees: employeesQuery.data?.data || [],
    totalRecords: employeesQuery.data?.total || 0,
    totalPages: employeesQuery.data?.totalPages || 1,
    createEmployee: createEmployeeMutation.mutateAsync,
    isCreating: createEmployeeMutation.isPending,
    updateEmployee: updateEmployeeMutation.mutateAsync,
    isUpdating: updateEmployeeMutation.isPending,
    deleteEmployee: deleteEmployeeMutation.mutateAsync,
    isDeleting: deleteEmployeeMutation.isPending,
  };
};
