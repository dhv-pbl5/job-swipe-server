package org.dhv.pbl5server.realtime_service.service;

public interface RealtimeService {
    void sendToAllClient(String message);

    void sendToAllClient(Object object);

    /**
     * @param accountId is prefix of client id (format: accountId::ipAddress)
     */
    void sendToClientWithPrefix(String accountId, String message);

    /**
     * @param accountId is prefix of client id (format: accountId::ipAddress)
     */
    void sendToClientWithPrefix(String accountId, Object object);

    /**
     * @param accountId is prefix of client id (format: accountId::ipAddress)
     */
    void disconnectClientWithPrefix(String accountId);

    void disconnectAllClient();
}
