package org.dhv.pbl5server.authentication_service.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.common_service.annotation.PasswordValidation;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSnakeCaseNaming
public class LoginRequest {
    @NotBlank
    @Email
    private String email;
    
    @PasswordValidation
    private String password;
}
