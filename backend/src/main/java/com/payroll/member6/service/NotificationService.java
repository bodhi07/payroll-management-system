package com.payroll.member6.service;

import com.payroll.member6.dto.NotificationDTO;
import com.payroll.response.PagedResponse;

import java.util.List;

public interface NotificationService {

    NotificationDTO sendNotification(Long userId, String title, String message);

    PagedResponse<NotificationDTO> getUserNotifications(Long userId, int pageNo, int pageSize);

    List<NotificationDTO> getUnreadNotifications(Long userId);

    void markAsRead(Long notificationId);
}
