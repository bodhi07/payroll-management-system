import axiosInstance from './axiosConfig';
import { ENDPOINTS } from '../constants/apiEndpoints';

/**
 * Employee API Service
 *
 * Belongs to Member Module 1: Employee Management (CRUD).
 * Interacts with Spring Boot `/api/v1/employees` REST endpoints.
 */

export const employeeApi = {
  /**
   * Get paginated employees list with optional search query and filters
   */
  getEmployees: async (params = {}) => {
    const pageNo = Math.max(0, (params.page || 1) - 1);
    const pageSize = params.pageSize || 10;
    const sortBy = params.sortBy || 'employeeId';
    const sortDir = params.sortDir || 'asc';

    let response;
    if (params.search && params.search.trim()) {
      response = await axiosInstance.get(ENDPOINTS.EMPLOYEES.SEARCH, {
        params: { query: params.search.trim(), pageNo, pageSize, sortBy, sortDir },
      });
    } else if (params.departmentId && params.departmentId !== 'All') {
      response = await axiosInstance.get(ENDPOINTS.EMPLOYEES.FILTER_DEPT(params.departmentId), {
        params: { pageNo, pageSize },
      });
    } else if (params.status && params.status !== 'All' && params.status !== 'All Statuses') {
      response = await axiosInstance.get(ENDPOINTS.EMPLOYEES.FILTER_STATUS(params.status.toUpperCase()), {
        params: { pageNo, pageSize },
      });
    } else {
      response = await axiosInstance.get(ENDPOINTS.EMPLOYEES.BASE, {
        params: { pageNo, pageSize, sortBy, sortDir },
      });
    }

    const resData = response.data?.data || response.data;
    const content = resData?.content || (Array.isArray(resData) ? resData : []);

    const formattedEmployees = content.map((item) => ({
      id: item.employeeId,
      employeeId: item.employeeNumber || `EMP-${item.employeeId}`,
      employeeNumber: item.employeeNumber,
      firstName: item.firstName,
      lastName: item.lastName,
      name: `${item.firstName || ''} ${item.lastName || ''}`.trim() || 'Unnamed Employee',
      email: item.email,
      phone: item.phone,
      nic: item.nic,
      gender: item.gender,
      address: item.address || 'N/A',
      departmentId: item.departmentId,
      department: item.departmentName || 'General',
      designation: item.designation,
      baseSalary: Number(item.basicSalary || 0),
      basicSalary: Number(item.basicSalary || 0),
      joinDate: item.joinDate,
      status: item.status || 'ACTIVE',
      avatar: `https://ui-avatars.com/api/?name=${encodeURIComponent(`${item.firstName || ''}+${item.lastName || ''}`)}&background=004ac6&color=fff`,
    }));

    return {
      data: formattedEmployees,
      total: resData?.totalElements ?? formattedEmployees.length,
      page: (resData?.pageNo ?? 0) + 1,
      pageSize: resData?.pageSize || pageSize,
      totalPages: resData?.totalPages ?? 1,
    };
  },

  /**
   * Get single employee detail by ID
   */
  getEmployeeById: async (id) => {
    const response = await axiosInstance.get(ENDPOINTS.EMPLOYEES.BY_ID(id));
    const item = response.data?.data || response.data;
    return {
      id: item.employeeId,
      employeeId: item.employeeNumber,
      employeeNumber: item.employeeNumber,
      firstName: item.firstName,
      lastName: item.lastName,
      name: `${item.firstName || ''} ${item.lastName || ''}`.trim(),
      email: item.email,
      phone: item.phone,
      nic: item.nic,
      gender: item.gender,
      address: item.address,
      departmentId: item.departmentId,
      department: item.departmentName,
      designation: item.designation,
      basicSalary: Number(item.basicSalary || 0),
      baseSalary: Number(item.basicSalary || 0),
      joinDate: item.joinDate,
      status: item.status,
    };
  },

  /**
   * Create a new employee record via Spring Boot POST /api/v1/employees
   */
  createEmployee: async (employeeData) => {
    const payload = {
      employeeNumber: employeeData.employeeNumber || `EMP-${Date.now().toString().slice(-4)}`,
      firstName: employeeData.firstName || employeeData.name?.split(' ')[0] || 'Employee',
      lastName: employeeData.lastName || employeeData.name?.split(' ').slice(1).join(' ') || 'User',
      email: employeeData.email,
      phone: employeeData.phone || '+94770000000',
      nic: employeeData.nic || `${Date.now()}`,
      gender: employeeData.gender || 'MALE',
      address: employeeData.address || '',
      departmentId: Number(employeeData.departmentId || 1),
      designation: employeeData.designation || 'Staff',
      basicSalary: Number(employeeData.basicSalary || employeeData.baseSalary || 50000),
      joinDate: employeeData.joinDate || new Date().toISOString().slice(0, 10),
      status: employeeData.status || 'ACTIVE',
    };

    const response = await axiosInstance.post(ENDPOINTS.EMPLOYEES.BASE, payload);
    return response.data?.data || response.data;
  },

  /**
   * Update existing employee record
   */
  updateEmployee: async (id, employeeData) => {
    const payload = {
      employeeNumber: employeeData.employeeNumber,
      firstName: employeeData.firstName || employeeData.name?.split(' ')[0],
      lastName: employeeData.lastName || employeeData.name?.split(' ').slice(1).join(' '),
      email: employeeData.email,
      phone: employeeData.phone,
      nic: employeeData.nic,
      gender: employeeData.gender,
      address: employeeData.address,
      departmentId: Number(employeeData.departmentId),
      designation: employeeData.designation,
      basicSalary: Number(employeeData.basicSalary || employeeData.baseSalary),
      joinDate: employeeData.joinDate,
      status: employeeData.status,
    };

    const response = await axiosInstance.put(ENDPOINTS.EMPLOYEES.BY_ID(id), payload);
    return response.data?.data || response.data;
  },

  /**
   * Delete employee record
   */
  deleteEmployee: async (id) => {
    const response = await axiosInstance.delete(ENDPOINTS.EMPLOYEES.BY_ID(id));
    return response.data?.data || response.data;
  },

  /**
   * Run custom Linear Search Algorithm on employee names
   */
  linearSearch: async (keyword) => {
    const response = await axiosInstance.get(ENDPOINTS.EMPLOYEES.LINEAR_SEARCH, {
      params: { keyword },
    });
    return response.data?.data || response.data;
  },
};
