package org.dhv.pbl5server.realtime_service.service;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.realtime_service.socket.AbstractClient;
import org.dhv.pbl5server.realtime_service.socket.RealtimeServer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RealtimeServiceImpl implements RealtimeService {
    private final RealtimeServer server;

    @Override
    public void sendToAllClient(String message) {
        server.getClients().forEach((_, value) -> value.send(message));
    }

    @Override
    public void sendToAllClient(Object object) {
        var json = CommonUtils.convertToJson(object);
        if (json == null) return;
        server.getClients().forEach((_, value) -> value.send(json));
    }

    @Override
    public void sendToClientWithPrefix(String accountId, String message) {
        var client = getClientByPrefixId(accountId);
        if (client != null)
            client.send(message);
    }

    @Override
    public void sendToClientWithPrefix(String accountId, Object object) {
        var json = CommonUtils.convertToJson(object);
        var client = getClientByPrefixId(accountId);
        if (json == null || client == null) return;
        client.send(json);
    }

    @Override
    public void disconnectClientWithPrefix(String accountId) {
        var client = getClientByPrefixId(accountId);
        if (client != null)
            client.disconnect();
    }

    @Override
    public void disconnectAllClient() {
        server.getClients().forEach((_, value) -> value.disconnect());
    }

    private AbstractClient getClientByPrefixId(String prefix) {
        for (var key : server.getClients().keySet())
            if (key.startsWith(prefix)) return server.getClients().get(key);
        return null;
    }
}