import { attendanceApi } from '../api/attendanceApi';

export const attendanceService = {
  checkIn: async (data) => {
    return await attendanceApi.checkIn(data);
  },

  checkOut: async (data) => {
    return await attendanceApi.checkOut(data);
  },

  getAttendanceById: async (id) => {
    return await attendanceApi.getAttendanceById(id);
  },

  getAttendanceByDate: async (date, pageNo, pageSize) => {
    return await attendanceApi.getAttendanceByDate(date, pageNo, pageSize);
  },

  getAttendanceByEmployee: async (employeeId, pageNo, pageSize) => {
    return await attendanceApi.getAttendanceByEmployee(employeeId, pageNo, pageSize);
  },

  getAttendanceReport: async (employeeId, startDate, endDate) => {
    return await attendanceApi.getAttendanceReport(employeeId, startDate, endDate);
  },

  deleteAttendance: async (id) => {
    return await attendanceApi.deleteAttendance(id);
  },
};
