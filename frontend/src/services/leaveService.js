import { leaveApi } from '../api/leaveApi';

export const leaveService = {
  applyLeave: async (data) => {
    return await leaveApi.applyLeave(data);
  },

  approveLeave: async (id, actionReason) => {
    return await leaveApi.approveLeave(id, actionReason);
  },

  rejectLeave: async (id, actionReason) => {
    return await leaveApi.rejectLeave(id, actionReason);
  },

  getLeaveById: async (id) => {
    return await leaveApi.getLeaveById(id);
  },

  getLeavesByStatus: async (status, pageNo, pageSize) => {
    return await leaveApi.getLeavesByStatus(status, pageNo, pageSize);
  },

  getEmployeeLeaveHistory: async (employeeId, pageNo, pageSize) => {
    return await leaveApi.getEmployeeLeaveHistory(employeeId, pageNo, pageSize);
  },

  getLeaveBalance: async (employeeId, year) => {
    return await leaveApi.getLeaveBalance(employeeId, year);
  },
};
