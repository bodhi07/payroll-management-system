package com.payroll.member6.service.impl;

import com.payroll.member6.dto.AuditLogDTO;
import com.payroll.member6.entity.AuditLog;
import com.payroll.member6.mapper.AuditLogMapper;
import com.payroll.member6.repository.AuditLogRepository;
import com.payroll.member6.service.AuditLogService;
import com.payroll.response.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ============================================================================
 * Member 06: Audit Log Service Implementation
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Records security & system action audit logs and retrieves them for compliance viewing.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    public AuditLogServiceImpl(final AuditLogRepository auditLogRepository, final AuditLogMapper auditLogMapper) {
        this.auditLogRepository = auditLogRepository;
        this.auditLogMapper = auditLogMapper;
    }

    @Override
    @Transactional
    public void logAction(final String username, final String action, final String entityName, final String details, final String ipAddress) {
        final AuditLog auditLog = AuditLog.builder()
                .username(username)
                .action(action)
                .entityName(entityName)
                .details(details)
                .ipAddress(ipAddress)
                .build();
        auditLogRepository.save(auditLog);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<AuditLogDTO> getAllAuditLogs(final int pageNo, final int pageSize, final String sortBy, final String sortDir) {
        final Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        final Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        final Page<AuditLog> page = auditLogRepository.findAll(pageable);
        final List<AuditLogDTO> content = page.getContent().stream().map(auditLogMapper::toDTO).collect(Collectors.toList());

        return PagedResponse.<AuditLogDTO>builder()
                .content(content)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
