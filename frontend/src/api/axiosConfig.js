import axios from 'axios';
import { API_BASE_URL, ENDPOINTS } from '../constants/apiEndpoints';

/**
 * Axios Configuration Module
 *
 * Configures the primary Axios instance for Spring Boot JWT Authentication.
 * Includes:
 * - Request Interceptor: Injects `Authorization: Bearer <token>` header on outgoing calls.
 * - Response Interceptor: Catches HTTP 401 Unauthorized, handles automatic token refresh,
 *   and gracefully unwraps Spring Boot ApiResponse envelopes.
 */

const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request Interceptor: Attach JWT Token
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response Interceptor: Auto Refresh Token & Error Handling
axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // Handle 401 Unauthorized and auto refresh token once
    if (error.response && error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      try {
        const refreshToken = localStorage.getItem('refreshToken');
        if (refreshToken) {
          const res = await axios.post(`${API_BASE_URL}${ENDPOINTS.AUTH.REFRESH}?token=${encodeURIComponent(refreshToken)}`);
          const tokenData = res.data?.data || res.data;
          const newToken = tokenData.accessToken || tokenData.token;
          if (newToken) {
            localStorage.setItem('token', newToken);
            axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${newToken}`;
            originalRequest.headers['Authorization'] = `Bearer ${newToken}`;
            return axiosInstance(originalRequest);
          }
        }
      } catch (refreshError) {
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('user');
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;
