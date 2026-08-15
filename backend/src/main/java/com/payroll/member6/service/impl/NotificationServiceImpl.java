package com.payroll.member6.service.impl;

import com.payroll.exception.ResourceNotFoundException;
import com.payroll.member6.dto.NotificationDTO;
import com.payroll.member6.entity.Notification;
import com.payroll.member6.mapper.NotificationMapper;
import com.payroll.member6.repository.NotificationRepository;
import com.payroll.member6.service.NotificationService;
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
 * Member 06: Notification Service Implementation
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Manages creation, delivery, and read status updates for user notifications.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(final NotificationRepository notificationRepository,
                                    final NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    @Override
    @Transactional
    public NotificationDTO sendNotification(final Long userId, final String title, final String message) {
        final Notification notification = Notification.builder()
                .userId(userId)
                .title(title)
                .message(message)
                .read(false)
                .build();

        final Notification saved = notificationRepository.save(notification);
        return notificationMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<NotificationDTO> getUserNotifications(final Long userId, final int pageNo, final int pageSize) {
        final Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        final Page<Notification> page = notificationRepository.findByUserId(userId, pageable);
        final List<NotificationDTO> content = page.getContent().stream().map(notificationMapper::toDTO).collect(Collectors.toList());

        return PagedResponse.<NotificationDTO>builder()
                .content(content)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getUnreadNotifications(final Long userId) {
        return notificationRepository.findByUserIdAndRead(userId, false).stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(final Long notificationId) {
        final Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
