import axiosInstance from './axiosConfig';
import { ENDPOINTS } from '../constants/apiEndpoints';

/**
 * Authentication API Service
 *
 * Belongs to Member Module 6: User Management & Authentication.
 * Connects with Spring Boot `/api/v1/auth` REST controllers.
 */

export const authApi = {
  /**
   * Post credentials to Spring Boot JWT Auth Endpoint `/api/v1/auth/login`
   * @param {Object} credentials - { usernameOrEmail, password }
   */
  login: async (credentials) => {
    const payload = {
      usernameOrEmail: (credentials.usernameOrEmail || credentials.email || '').trim(),
      password: credentials.password,
    };

    const response = await axiosInstance.post(ENDPOINTS.AUTH.LOGIN, payload);
    const resData = response.data?.data || response.data;

    const roles = Array.isArray(resData.roles) 
      ? resData.roles 
      : (resData.roles ? Array.from(resData.roles) : ['ROLE_EMPLOYEE']);

    const primaryRole = roles.includes('ROLE_ADMIN') 
      ? 'ADMIN' 
      : (roles.includes('ROLE_HR') ? 'HR' : 'EMPLOYEE');

    return {
      token: resData.accessToken || resData.token,
      refreshToken: resData.refreshToken,
      user: {
        id: resData.id,
        name: resData.username,
        email: resData.email,
        roles: roles,
        role: primaryRole,
        designation: primaryRole === 'ADMIN' ? 'System Administrator' : (primaryRole === 'HR' ? 'HR Manager' : 'Employee'),
        avatar: `https://ui-avatars.com/api/?name=${encodeURIComponent(resData.username || 'User')}&background=004ac6&color=fff`,
      },
    };
  },

  /**
   * Register a new user account via Spring Boot `/api/v1/auth/register`
   * @param {Object} registerData - { username, email, password, roles }
   */
  register: async (registerData) => {
    const response = await axiosInstance.post(ENDPOINTS.AUTH.REGISTER, registerData);
    return response.data?.data || response.data;
  },

  /**
   * Refresh JWT token
   */
  refreshToken: async (token) => {
    const response = await axiosInstance.post(`${ENDPOINTS.AUTH.REFRESH}?token=${encodeURIComponent(token)}`);
    return response.data?.data || response.data;
  },
};
