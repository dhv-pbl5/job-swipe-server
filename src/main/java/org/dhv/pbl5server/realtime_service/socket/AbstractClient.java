package org.dhv.pbl5server.realtime_service.socket;

public interface AbstractClient {
    void send(String message);

    void disconnect();
}
