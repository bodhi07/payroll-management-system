package com.payroll.member6.controller;

import com.payroll.member6.dto.AuditLogDTO;
import com.payroll.member6.service.AuditLogService;
import com.payroll.response.ApiResponse;
import com.payroll.response.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ============================================================================
 * Member 06: Audit Log REST Controller
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Endpoint (`/api/v1/audit-logs`) to query audit logs. Admin restricted.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/audit-logs")
@Tag(name = "Audit Trail Management", description = "Endpoints for retrieving system audit trails.")
public class AuditController {

    private final AuditLogService auditLogService;

    public AuditController(final AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get Audit Logs", description = "Fetches a paginated list of system audit logs.")
    public ResponseEntity<ApiResponse<PagedResponse<AuditLogDTO>>> getAuditLogs(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) final int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) final int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "createdAt", required = false) final String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) final String sortDir) {

        final PagedResponse<AuditLogDTO> response = auditLogService.getAllAuditLogs(pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Audit logs fetched successfully", response));
    }
}
