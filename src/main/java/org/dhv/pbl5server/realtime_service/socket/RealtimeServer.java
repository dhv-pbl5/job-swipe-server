package org.dhv.pbl5server.realtime_service.socket;

import lombok.Getter;
import org.dhv.pbl5server.authentication_service.service.JwtService;
import org.dhv.pbl5server.common_service.utils.LogUtils;
import org.dhv.pbl5server.realtime_service.constants.RealtimeConstant;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Map;

@Getter
public class RealtimeServer implements Runnable {
    private final int port;
    private final JwtService jwtService;
    private boolean isRunning = true;
    private final Map<String, AbstractClient> clients = new Hashtable<>();

    public RealtimeServer(int port, JwtService jwtService) {
        this.port = port;
        this.jwtService = jwtService;
    }

    @Override
    public void run() {
        LogUtils.info(RealtimeConstant.SERVER_DEBUG_PREFIX, RealtimeConstant.SERVER_RUNNING, port);
        // Run server
        try (ServerSocket server = new ServerSocket(port)) {
            while (isRunning) {
                try {
                    Socket socket = server.accept();
                    RealtimeClient.builder()
                            .socket(socket)
                            .jwtService(jwtService)
                            .onConnect(clients::putIfAbsent)
                            .onDisconnect(clients::remove)
                            .build()
                            .start();
                } catch (Exception e) {
                    LogUtils.error(RealtimeConstant.SERVER_DEBUG_PREFIX, RealtimeConstant.ACCEPTING_CLIENT_ERROR, e);
                }
            }
        } catch (Exception e) {
            LogUtils.error(RealtimeConstant.SERVER_DEBUG_PREFIX, e);
            isRunning = false;
        }
    }
}
