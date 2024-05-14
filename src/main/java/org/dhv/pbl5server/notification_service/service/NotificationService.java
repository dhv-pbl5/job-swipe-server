package org.dhv.pbl5server.notification_service.service;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.notification_service.entity.NotificationType;
import org.dhv.pbl5server.notification_service.payload.NotificationResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

// git commit -m "PBL-597 realtime conversation"
// git commit -m "PBL-599 unread notification count"
// git commit -m "PBL-600 unread notification count for company"

public interface NotificationService {
    void notifyToAll(String message);

    ApiDataResponse getNotifications(Account account, Pageable pageRequest);

    NotificationResponse getNotificationById(Account account, String notificationId);

    void markAsRead(Account account, String notificationId);

    void markAllAsRead(Account account);

    int getUnreadCount(Account account);

    void createNotification(UUID objectId, Account sender, Account receiver, NotificationType type);
}
