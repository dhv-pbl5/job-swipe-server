package org.dhv.pbl5server.realtime_service.socket;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dhv.pbl5server.authentication_service.service.JwtService;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Map;

@Slf4j
@Getter
public class RealtimeServer implements Runnable {
    private final int port;
    private final JwtService jwtService;
    private boolean isRunning = true;
    private final Map<String, AbstractClient> clients = new Hashtable<>();
    private static final String LOG_PREFIX = "Server socket ===>";

    public RealtimeServer(int port, JwtService jwtService) {
        this.port = port;
        this.jwtService = jwtService;
    }

    @Override
    public void run() {
        log("Running on port: %d".formatted(port), false);
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
                    log("Socket client error: %s".formatted(e.getMessage()), true);
                }
            }
        } catch (Exception e) {
            log("Error: %s".formatted(e.getMessage()), true);
            isRunning = false;
        }
    }

    private void log(String message, boolean isError) {
        if (isError) {
            log.error("{} {}", LOG_PREFIX, message);
            return;
        }
        log.info("{} {}", LOG_PREFIX, message);
    }
}
