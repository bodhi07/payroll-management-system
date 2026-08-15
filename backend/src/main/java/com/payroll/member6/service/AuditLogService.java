package com.payroll.member6.service;

import com.payroll.member6.dto.AuditLogDTO;
import com.payroll.response.PagedResponse;

public interface AuditLogService {

    void logAction(String username, String action, String entityName, String details, String ipAddress);

    PagedResponse<AuditLogDTO> getAllAuditLogs(int pageNo, int pageSize, String sortBy, String sortDir);
}
