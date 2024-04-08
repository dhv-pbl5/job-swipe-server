package org.dhv.pbl5server.realtime_service.socket;

public interface AbstractClient {
    void send(String type, String message);

    void disconnect();
}
