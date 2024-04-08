package org.dhv.pbl5server.realtime_service.service;

public interface RealtimeService {
    void sendToAllClient(String type, String message);

    void sendToAllClient(String type, Object object);

    /**
     * @param accountId is prefix of client id (format: accountId::ipAddress)
     */
    void sendToClientWithPrefix(String accountId, String type, String message);

    /**
     * @param accountId is prefix of client id (format: accountId::ipAddress)
     */
    void sendToClientWithPrefix(String accountId, String type, Object object);

    /**
     * @param accountId is prefix of client id (format: accountId::ipAddress)
     */
    void disconnectClientWithPrefix(String accountId);

    void disconnectAllClient();
}
