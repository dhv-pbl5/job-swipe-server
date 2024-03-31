package org.dhv.pbl5server.authentication_service.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.common_service.annotation.NotBlankStringValidation;
import org.dhv.pbl5server.common_service.annotation.PasswordValidation;
import org.dhv.pbl5server.constant_service.payload.ConstantSelectionRequest;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonSnakeCaseNaming
public class CompanyRegisterRequest {
    @Email
    @NotBlank
    private String email;
    @PasswordValidation
    private String password;
    @NotBlankStringValidation
    private String address;
    @NotBlankStringValidation
    private String phoneNumber;
    @NotNull
    @Valid
    private ConstantSelectionRequest systemRole;
    @NotBlankStringValidation
    private String companyName;
    @NotBlankStringValidation
    private String companyUrl;
    @NotNull
    private Timestamp establishedDate;
}
