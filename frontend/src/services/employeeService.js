import { employeeApi } from '../api/employeeApi';

export const employeeService = {
  getEmployees: async (params) => {
    return await employeeApi.getEmployees(params);
  },

  getEmployeeById: async (id) => {
    return await employeeApi.getEmployeeById(id);
  },

  createEmployee: async (employeeData) => {
    return await employeeApi.createEmployee(employeeData);
  },

  updateEmployee: async (id, employeeData) => {
    return await employeeApi.updateEmployee(id, employeeData);
  },

  deleteEmployee: async (id) => {
    return await employeeApi.deleteEmployee(id);
  },

  linearSearch: async (keyword) => {
    return await employeeApi.linearSearch(keyword);
  },
};
