// git commit -m "PBL-850 set up base"

package org.dhv.pbl5server;

import org.dhv.pbl5server.authentication_service.config.JwtApplicationProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({ JwtApplicationProperty.class })
@SpringBootApplication
public class Pbl5ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(Pbl5ServerApplication.class, args);
    }
}
