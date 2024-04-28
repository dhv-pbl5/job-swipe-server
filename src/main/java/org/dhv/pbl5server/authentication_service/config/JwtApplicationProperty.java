package org.dhv.pbl5server.authentication_service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
@Getter
public class JwtApplicationProperty {
    @Value("${application.jwt.access-token-secret-key}")
    private String accessTokenSecret;
    @Value("${application.jwt.access-token-expiration-ms}")
    private Long accessTokenExpirationMs;
    @Value("${application.jwt.refresh-token-secret-key}")
    private String refreshTokenSecret;
    @Value("${application.jwt.refresh-token-expiration-ms}")
    private Long refreshTokenExpirationMs;
}
