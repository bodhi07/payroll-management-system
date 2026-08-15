import axiosInstance from './axiosConfig';
import { ENDPOINTS } from '../constants/apiEndpoints';

/**
 * Notification API Service
 *
 * Belongs to Member Module 6: Notification Management.
 * Connects to Spring Boot `/api/v1/notifications`.
 */

export const notificationApi = {
  getUserNotifications: async (userId, pageNo = 0, pageSize = 20) => {
    const response = await axiosInstance.get(ENDPOINTS.NOTIFICATIONS.BY_USER(userId), {
      params: { pageNo, pageSize },
    });
    const resData = response.data?.data || response.data;
    const content = resData?.content || (Array.isArray(resData) ? resData : []);

    return content.map((n) => ({
      id: n.id,
      userId: n.userId,
      title: n.title,
      message: n.message,
      read: n.read,
      createdAt: n.createdAt,
    }));
  },

  getUnreadNotifications: async (userId) => {
    const response = await axiosInstance.get(ENDPOINTS.NOTIFICATIONS.UNREAD(userId));
    const resData = response.data?.data || response.data;
    const list = Array.isArray(resData) ? resData : [];

    return list.map((n) => ({
      id: n.id,
      userId: n.userId,
      title: n.title,
      message: n.message,
      read: n.read,
      createdAt: n.createdAt,
    }));
  },

  markAsRead: async (id) => {
    const response = await axiosInstance.put(ENDPOINTS.NOTIFICATIONS.MARK_READ(id));
    return response.data?.data || response.data;
  },
};
