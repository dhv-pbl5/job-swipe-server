package org.dhv.pbl5server.realtime_service.service;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.constant_service.service.ConstantService;
import org.dhv.pbl5server.notification_service.entity.NotificationType;
import org.dhv.pbl5server.realtime_service.socket.AbstractClient;
import org.dhv.pbl5server.realtime_service.socket.RealtimeServer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RealtimeServiceImpl implements RealtimeService {
    private final RealtimeServer server;
    private final ConstantService constantService;

    @Override
    public void sendToAllClient(NotificationType type, String message) {
        var typeResponse = constantService.getConstantByType(type.constantType());
        server.getClients().forEach((ignore, value) -> value.send(typeResponse, message));
    }

    @Override
    public void sendToAllClient(NotificationType type, Object object) {
        var json = CommonUtils.convertToJson(object);
        if (json == null) return;
        var typeResponse = constantService.getConstantByType(type.constantType());
        server.getClients().forEach((ignore, value) -> value.send(typeResponse, json));
    }

    @Override
    public void sendToClientWithPrefix(String accountId, NotificationType type, String message) {
        var client = getClientByPrefixId(accountId);
        if (client != null) {
            var typeResponse = constantService.getConstantByType(type.constantType());
            client.send(typeResponse, message);
        }
    }

    @Override
    public void sendToClientWithPrefix(String accountId, NotificationType type, Object object) {
        var json = CommonUtils.convertToJson(object);
        var client = getClientByPrefixId(accountId);
        if (json == null || client == null) return;
        var typeResponse = constantService.getConstantByType(type.constantType());
        client.send(typeResponse, json);
    }

    @Override
    public void disconnectClientWithPrefix(String accountId) {
        var client = getClientByPrefixId(accountId);
        if (client != null)
            client.disconnect();
    }

    @Override
    public void disconnectAllClient() {
        server.getClients().forEach((ignore, value) -> value.disconnect());
    }

    private AbstractClient getClientByPrefixId(String prefix) {
        for (var key : server.getClients().keySet())
            if (key.startsWith(prefix)) return server.getClients().get(key);
        return null;
    }
}
