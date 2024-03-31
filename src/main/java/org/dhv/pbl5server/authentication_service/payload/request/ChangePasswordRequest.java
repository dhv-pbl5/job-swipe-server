package org.dhv.pbl5server.authentication_service.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.common_service.constant.CommonConstant;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class ChangePasswordRequest {
    @NotBlank
    @Length(min = 8, max = 20)
    @Pattern(regexp = CommonConstant.PASSWORD_REGEXP_PATTERN)
    private String currentPassword;


    @NotBlank
    @Length(min = 8, max = 20)
    @Pattern(regexp = CommonConstant.PASSWORD_REGEXP_PATTERN)
    private String newPassword;

    @NotBlank
    @Length(min = 8, max = 20)
    @Pattern(regexp = CommonConstant.PASSWORD_REGEXP_PATTERN)
    private String newPasswordConfirmation;
}
