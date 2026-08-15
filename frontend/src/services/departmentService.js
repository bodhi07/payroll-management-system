import { departmentApi } from '../api/departmentApi';

export const departmentService = {
  getDepartments: async (params) => {
    return await departmentApi.getDepartments(params);
  },

  getDepartmentById: async (id) => {
    return await departmentApi.getDepartmentById(id);
  },

  createDepartment: async (data) => {
    return await departmentApi.createDepartment(data);
  },

  updateDepartment: async (id, data) => {
    return await departmentApi.updateDepartment(id, data);
  },

  deleteDepartment: async (id) => {
    return await departmentApi.deleteDepartment(id);
  },

  getDepartmentReport: async (id) => {
    return await departmentApi.getDepartmentReport(id);
  },

  getAllDepartmentReports: async () => {
    return await departmentApi.getAllDepartmentReports();
  },
};
