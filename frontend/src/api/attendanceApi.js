import axiosInstance from './axiosConfig';
import { ENDPOINTS } from '../constants/apiEndpoints';

/**
 * Attendance API Service
 *
 * Belongs to Member Module 2: Attendance Management (CRUD).
 * Connects to Spring Boot `/api/v1/attendance`.
 */

export const attendanceApi = {
  checkIn: async (data) => {
    const payload = {
      employeeId: Number(data.employeeId),
      date: data.date || new Date().toISOString().slice(0, 10),
      checkInTime: data.checkInTime || new Date().toTimeString().slice(0, 8),
    };
    const response = await axiosInstance.post(ENDPOINTS.ATTENDANCE.CHECK_IN, payload);
    return response.data?.data || response.data;
  },

  checkOut: async (data) => {
    const payload = {
      employeeId: Number(data.employeeId),
      date: data.date || new Date().toISOString().slice(0, 10),
      checkOutTime: data.checkOutTime || new Date().toTimeString().slice(0, 8),
    };
    const response = await axiosInstance.post(ENDPOINTS.ATTENDANCE.CHECK_OUT, payload);
    return response.data?.data || response.data;
  },

  getAttendanceById: async (id) => {
    const response = await axiosInstance.get(ENDPOINTS.ATTENDANCE.BY_ID(id));
    return response.data?.data || response.data;
  },

  getAttendanceByDate: async (date, pageNo = 0, pageSize = 20) => {
    const formattedDate = date || new Date().toISOString().slice(0, 10);
    const response = await axiosInstance.get(ENDPOINTS.ATTENDANCE.BASE, {
      params: { date: formattedDate, pageNo, pageSize },
    });
    const resData = response.data?.data || response.data;
    const content = resData?.content || (Array.isArray(resData) ? resData : []);

    return {
      data: content.map((item) => ({
        id: item.id,
        employeeId: item.employeeId,
        name: item.employeeName || `Employee ${item.employeeId}`,
        employeeNumber: item.employeeNumber || `EMP-${item.employeeId}`,
        date: item.date,
        checkIn: item.checkInTime ? String(item.checkInTime).slice(0, 5) : '-',
        checkOut: item.checkOutTime ? String(item.checkOutTime).slice(0, 5) : '-',
        workHours: item.workingHours ? `${item.workingHours} hrs` : (item.checkInTime && !item.checkOutTime ? 'In Progress' : '0 hrs'),
        lateHours: item.lateHours ? `${item.lateHours} hrs` : '0 hrs',
        overtimeHours: item.overtimeHours ? `${item.overtimeHours} hrs` : '0 hrs',
        status: item.status || 'PRESENT',
      })),
      total: resData?.totalElements ?? content.length,
      page: (resData?.pageNo ?? 0) + 1,
      pageSize: resData?.pageSize || pageSize,
      totalPages: resData?.totalPages ?? 1,
    };
  },

  getAttendanceByEmployee: async (employeeId, pageNo = 0, pageSize = 20) => {
    const response = await axiosInstance.get(ENDPOINTS.ATTENDANCE.BY_EMPLOYEE(employeeId), {
      params: { pageNo, pageSize },
    });
    return response.data?.data || response.data;
  },

  getAttendanceReport: async (employeeId, startDate, endDate) => {
    const response = await axiosInstance.get(ENDPOINTS.ATTENDANCE.REPORT, {
      params: { employeeId, startDate, endDate },
    });
    return response.data?.data || response.data;
  },

  deleteAttendance: async (id) => {
    const response = await axiosInstance.delete(ENDPOINTS.ATTENDANCE.BY_ID(id));
    return response.data?.data || response.data;
  },
};
