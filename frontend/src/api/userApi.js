import axiosInstance from './axiosConfig';
import { ENDPOINTS } from '../constants/apiEndpoints';

/**
 * User Administration API Service
 *
 * Belongs to Member Module 6: User Administration & RBAC.
 * Connects to Spring Boot `/api/v1/users`.
 */

export const userApi = {
  getAllUsers: async (pageNo = 0, pageSize = 20) => {
    const response = await axiosInstance.get(ENDPOINTS.USERS.BASE, {
      params: { pageNo, pageSize },
    });
    const resData = response.data?.data || response.data;
    const content = resData?.content || (Array.isArray(resData) ? resData : []);

    return {
      data: content.map((u) => ({
        id: u.id,
        username: u.username,
        email: u.email,
        roles: Array.isArray(u.roles) ? u.roles : (u.roles ? Array.from(u.roles) : ['ROLE_EMPLOYEE']),
        enabled: u.enabled !== false,
        createdAt: u.createdAt,
      })),
      total: resData?.totalElements ?? content.length,
      page: (resData?.pageNo ?? 0) + 1,
      pageSize: resData?.pageSize || pageSize,
      totalPages: resData?.totalPages ?? 1,
    };
  },

  getUserById: async (id) => {
    const response = await axiosInstance.get(ENDPOINTS.USERS.BY_ID(id));
    return response.data?.data || response.data;
  },

  assignRole: async (userId, roleName) => {
    const response = await axiosInstance.put(ENDPOINTS.USERS.ROLES(userId), null, {
      params: { role: roleName },
    });
    return response.data?.data || response.data;
  },

  deleteUser: async (id) => {
    const response = await axiosInstance.delete(ENDPOINTS.USERS.BY_ID(id));
    return response.data?.data || response.data;
  },
};
