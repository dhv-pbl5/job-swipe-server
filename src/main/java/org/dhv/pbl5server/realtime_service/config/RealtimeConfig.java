package org.dhv.pbl5server.realtime_service.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.service.JwtService;
import org.dhv.pbl5server.realtime_service.socket.RealtimeServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RealtimeConfig {

    @Value("${application.server.socket.port}")
    @Getter
    private int port;
    private final JwtService jwtService;

    @Bean
    public RealtimeServer realtimeServer() {
        RealtimeServer socket = new RealtimeServer(port, jwtService);
        new Thread(socket).start();
        return socket;
    }
}
