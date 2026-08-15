package com.payroll.member6.repository;

import com.payroll.member6.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ============================================================================
 * Member 06: Notification JPA Repository
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Data access abstraction layer for user in-app notifications.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Interface Realization: Extends {@link JpaRepository}.
 * 
 * Design Patterns Used:
 * --------------------
 * - Repository Pattern.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Finds paginated notifications for a recipient user ID.
     *
     * @param userId   Target recipient user ID
     * @param pageable Pagination settings
     * @return Page of notifications
     */
    Page<Notification> findByUserId(Long userId, Pageable pageable);

    /**
     * Finds unread notifications for recipient user ID.
     *
     * @param userId Target recipient user ID
     * @param read   Read status filter (false for unread)
     * @return List of unread notifications
     */
    List<Notification> findByUserIdAndRead(Long userId, boolean read);
}
