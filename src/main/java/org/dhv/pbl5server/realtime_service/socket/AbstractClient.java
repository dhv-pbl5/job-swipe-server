package org.dhv.pbl5server.realtime_service.socket;

public interface AbstractClient {
    void send(Object type, Object message);

    void disconnect();
}
