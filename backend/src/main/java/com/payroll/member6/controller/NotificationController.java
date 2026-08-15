package com.payroll.member6.controller;

import com.payroll.member6.dto.NotificationDTO;
import com.payroll.member6.service.NotificationService;
import com.payroll.response.ApiResponse;
import com.payroll.response.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ============================================================================
 * Member 06: Notification REST Controller
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Endpoints (`/api/v1/notifications`) to read user notifications and mark them read.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notification Management", description = "Endpoints for viewing and updating user in-app notifications.")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get User Notifications", description = "Fetches a paginated list of notifications for a user ID.")
    public ResponseEntity<ApiResponse<PagedResponse<NotificationDTO>>> getUserNotifications(
            @PathVariable("userId") final Long userId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) final int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) final int pageSize) {
        
        final PagedResponse<NotificationDTO> response = notificationService.getUserNotifications(userId, pageNo, pageSize);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Notifications fetched successfully", response));
    }

    @GetMapping("/user/{userId}/unread")
    @Operation(summary = "Get Unread Notifications", description = "Fetches all unread notifications for a user.")
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> getUnreadNotifications(@PathVariable("userId") final Long userId) {
        final List<NotificationDTO> unread = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Unread notifications fetched successfully", unread));
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Mark Notification As Read", description = "Updates a notification status to read.")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable("id") final Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Notification marked as read"));
    }
}
