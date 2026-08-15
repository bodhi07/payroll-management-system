package com.payroll.member6.mapper;

import com.payroll.member6.dto.AuditLogDTO;
import com.payroll.member6.entity.AuditLog;
import org.springframework.stereotype.Component;

/**
 * ============================================================================
 * Member 06: Audit Log Component Mapper
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Converts AuditLog entities to AuditLogDTO view models.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Component
public class AuditLogMapper {

    public AuditLogDTO toDTO(final AuditLog log) {
        if (log == null) return null;
        return AuditLogDTO.builder()
                .id(log.getId())
                .username(log.getUsername())
                .action(log.getAction())
                .entityName(log.getEntityName())
                .details(log.getDetails())
                .ipAddress(log.getIpAddress())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
