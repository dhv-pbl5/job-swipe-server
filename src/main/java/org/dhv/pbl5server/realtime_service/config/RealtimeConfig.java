package org.dhv.pbl5server.realtime_service.config;

import org.dhv.pbl5server.realtime_service.socket.RealtimeServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RealtimeConfig {

    @Value("${application.server.socket.port}")
    public int port;

    @Bean
    public RealtimeServer realtimeServer() {
        RealtimeServer socket = new RealtimeServer(port);
        new Thread(socket).start();
        return socket;
    }
}
