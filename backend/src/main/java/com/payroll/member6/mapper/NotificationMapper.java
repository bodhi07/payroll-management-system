package com.payroll.member6.mapper;

import com.payroll.member6.dto.NotificationDTO;
import com.payroll.member6.entity.Notification;
import org.springframework.stereotype.Component;

/**
 * ============================================================================
 * Member 06: Notification Component Mapper
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Converts Notification entities to NotificationDTO view models.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Component
public class NotificationMapper {

    public NotificationDTO toDTO(final Notification notification) {
        if (notification == null) return null;
        return NotificationDTO.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
