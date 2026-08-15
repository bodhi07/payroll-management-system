import axiosInstance from './axiosConfig';
import { ENDPOINTS } from '../constants/apiEndpoints';

/**
 * Audit Logs API Service
 *
 * Belongs to Member Module 6: Audit Trail Management.
 * Connects to Spring Boot `/api/v1/audit-logs`.
 */

export const auditApi = {
  getAuditLogs: async (pageNo = 0, pageSize = 20) => {
    const response = await axiosInstance.get(ENDPOINTS.AUDIT.BASE, {
      params: { pageNo, pageSize, sortBy: 'createdAt', sortDir: 'desc' },
    });
    const resData = response.data?.data || response.data;
    const content = resData?.content || (Array.isArray(resData) ? resData : []);

    return {
      data: content.map((item) => ({
        id: item.id,
        username: item.username,
        action: item.action,
        entityName: item.entityName,
        details: item.details,
        ipAddress: item.ipAddress,
        createdAt: item.createdAt,
      })),
      total: resData?.totalElements ?? content.length,
      page: (resData?.pageNo ?? 0) + 1,
      pageSize: resData?.pageSize || pageSize,
      totalPages: resData?.totalPages ?? 1,
    };
  },
};
