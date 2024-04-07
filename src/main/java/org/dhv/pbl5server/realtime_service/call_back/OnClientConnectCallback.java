package org.dhv.pbl5server.realtime_service.call_back;

import org.dhv.pbl5server.realtime_service.socket.RealtimeClient;

@FunctionalInterface
public interface OnClientConnectCallback {
    void call(String clientId, RealtimeClient client);
}
