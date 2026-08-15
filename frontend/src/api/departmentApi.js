import axiosInstance from './axiosConfig';
import { ENDPOINTS } from '../constants/apiEndpoints';

/**
 * Department API Service
 *
 * Belongs to Member Module 5: Department Management (CRUD).
 * Connects to Spring Boot `/api/v1/departments`.
 */

export const departmentApi = {
  getDepartments: async (params = {}) => {
    const pageNo = params.pageNo ?? 0;
    const pageSize = params.pageSize ?? 50;
    const response = await axiosInstance.get(ENDPOINTS.DEPARTMENTS.BASE, {
      params: { pageNo, pageSize },
    });
    const resData = response.data?.data || response.data;
    const list = Array.isArray(resData) ? resData : (resData?.content || []);

    return list.map((item) => ({
      id: item.departmentId || item.id,
      departmentId: item.departmentId || item.id,
      name: item.departmentName || item.name,
      code: item.departmentCode || item.code || 'DEPT',
      description: item.description || '',
      headcount: item.headcount || item.employeeCount || 0,
      percentage: `${item.percentage || 0}%`,
      createdAt: item.createdAt,
    }));
  },

  getDepartmentById: async (id) => {
    const response = await axiosInstance.get(ENDPOINTS.DEPARTMENTS.BY_ID(id));
    return response.data?.data || response.data;
  },

  createDepartment: async (deptData) => {
    const payload = {
      name: deptData.name,
      code: deptData.code,
      description: deptData.description || '',
    };
    const response = await axiosInstance.post(ENDPOINTS.DEPARTMENTS.BASE, payload);
    return response.data?.data || response.data;
  },

  updateDepartment: async (id, deptData) => {
    const payload = {
      name: deptData.name,
      code: deptData.code,
      description: deptData.description || '',
    };
    const response = await axiosInstance.put(ENDPOINTS.DEPARTMENTS.BY_ID(id), payload);
    return response.data?.data || response.data;
  },

  deleteDepartment: async (id) => {
    const response = await axiosInstance.delete(ENDPOINTS.DEPARTMENTS.BY_ID(id));
    return response.data?.data || response.data;
  },

  getDepartmentReport: async (id) => {
    const response = await axiosInstance.get(ENDPOINTS.DEPARTMENTS.REPORT(id));
    return response.data?.data || response.data;
  },

  getAllDepartmentReports: async () => {
    const response = await axiosInstance.get(ENDPOINTS.DEPARTMENTS.ALL_REPORTS);
    return response.data?.data || response.data;
  },
};
