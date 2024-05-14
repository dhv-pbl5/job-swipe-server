package org.dhv.pbl5server.authentication_service.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;

// git commit -m "PBL-511 login for company and user"

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonSnakeCaseNaming
public class CredentialResponse {
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";
    private String expiredAt;
}
