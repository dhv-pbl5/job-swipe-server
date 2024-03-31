package org.dhv.pbl5server.authentication_service.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.common_service.annotation.NotBlankStringValidation;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSnakeCaseNaming
public class RefreshTokenRequest {
    @NotBlankStringValidation
    private String refreshToken;
}
