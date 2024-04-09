package org.dhv.pbl5server.realtime_service.service;

import org.dhv.pbl5server.notification_service.entity.NotificationType;

public interface RealtimeService {
    void sendToAllClient(NotificationType type, String message);

    void sendToAllClient(NotificationType type, Object object);

    /**
     * @param accountId is prefix of client id (format: accountId::ipAddress)
     */
    void sendToClientWithPrefix(String accountId, NotificationType type, String message);

    /**
     * @param accountId is prefix of client id (format: accountId::ipAddress)
     */
    void sendToClientWithPrefix(String accountId, NotificationType type, Object object);

    /**
     * @param accountId is prefix of client id (format: accountId::ipAddress)
     */
    void disconnectClientWithPrefix(String accountId);

    void disconnectAllClient();
}
