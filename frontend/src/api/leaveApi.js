import axiosInstance from './axiosConfig';
import { ENDPOINTS } from '../constants/apiEndpoints';

/**
 * Leave API Service
 *
 * Belongs to Member Module 4: Leave Management (CRUD).
 * Connects to Spring Boot `/api/v1/leaves`.
 */

export const leaveApi = {
  applyLeave: async (data) => {
    const payload = {
      employeeId: Number(data.employeeId),
      leaveType: data.leaveType, // ANNUAL, CASUAL, MEDICAL
      startDate: data.startDate,
      endDate: data.endDate,
      reason: data.reason || '',
    };
    const response = await axiosInstance.post(ENDPOINTS.LEAVE.APPLY, payload);
    return response.data?.data || response.data;
  },

  approveLeave: async (id, actionReason = 'Approved by HR') => {
    const response = await axiosInstance.put(ENDPOINTS.LEAVE.APPROVE(id), { actionReason });
    return response.data?.data || response.data;
  },

  rejectLeave: async (id, actionReason = 'Rejected by HR') => {
    const response = await axiosInstance.put(ENDPOINTS.LEAVE.REJECT(id), { actionReason });
    return response.data?.data || response.data;
  },

  getLeaveById: async (id) => {
    const response = await axiosInstance.get(ENDPOINTS.LEAVE.BY_ID(id));
    return response.data?.data || response.data;
  },

  getLeavesByStatus: async (status = 'PENDING', pageNo = 0, pageSize = 50) => {
    const response = await axiosInstance.get(ENDPOINTS.LEAVE.BY_STATUS(status), {
      params: { pageNo, pageSize },
    });
    const resData = response.data?.data || response.data;
    const content = resData?.content || (Array.isArray(resData) ? resData : []);

    return {
      data: content.map((item) => ({
        id: item.id,
        leaveId: item.id,
        employeeId: item.employeeId,
        employeeName: item.employeeName || `Employee ${item.employeeId}`,
        employeeNumber: item.employeeNumber || '',
        department: item.departmentName || 'General',
        leaveType: item.leaveType,
        durationDays: item.totalDays || 1,
        totalDays: item.totalDays || 1,
        startDate: item.startDate,
        endDate: item.endDate,
        reason: item.reason,
        status: item.status || 'PENDING',
        approvedBy: item.approvedBy,
        actionReason: item.actionReason,
        createdAt: item.createdAt,
      })),
      total: resData?.totalElements ?? content.length,
      page: (resData?.pageNo ?? 0) + 1,
      pageSize: resData?.pageSize || pageSize,
      totalPages: resData?.totalPages ?? 1,
    };
  },

  getEmployeeLeaveHistory: async (employeeId, pageNo = 0, pageSize = 20) => {
    const response = await axiosInstance.get(ENDPOINTS.LEAVE.BY_EMPLOYEE(employeeId), {
      params: { pageNo, pageSize },
    });
    return response.data?.data || response.data;
  },

  getLeaveBalance: async (employeeId, year) => {
    const response = await axiosInstance.get(ENDPOINTS.LEAVE.BALANCE(employeeId), {
      params: { year: year || new Date().getFullYear() },
    });
    return response.data?.data || response.data;
  },
};
