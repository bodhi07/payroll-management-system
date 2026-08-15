import { authApi } from '../api/authApi';

/**
 * Authentication Service Layer
 *
 * Encapsulates authentication business logic and local storage token management.
 */
export const authService = {
  login: async (usernameOrEmail, password) => {
    const data = await authApi.login({ usernameOrEmail, password });
    if (data && data.token) {
      localStorage.setItem('token', data.token);
      if (data.refreshToken) {
        localStorage.setItem('refreshToken', data.refreshToken);
      }
      localStorage.setItem('user', JSON.stringify(data.user));
    }
    return data;
  },

  register: async (registerData) => {
    return await authApi.register(registerData);
  },

  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
  },

  getStoredUser: () => {
    try {
      const userStr = localStorage.getItem('user');
      return userStr ? JSON.parse(userStr) : null;
    } catch (e) {
      return null;
    }
  },

  isAuthenticated: () => {
    return !!localStorage.getItem('token');
  },
};
