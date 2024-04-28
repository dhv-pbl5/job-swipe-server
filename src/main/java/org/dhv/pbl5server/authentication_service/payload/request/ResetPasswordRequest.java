package org.dhv.pbl5server.authentication_service.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.common_service.annotation.PasswordValidation;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class ResetPasswordRequest {
    @Pattern(regexp = "^\\d{6}$")
    @NotNull
    private String resetPasswordCode;
    @NotNull
    @Email
    private String email;
    @PasswordValidation
    private String newPassword;
    @PasswordValidation
    private String newPasswordConfirmation;
}
