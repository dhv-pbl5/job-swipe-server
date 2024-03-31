package org.dhv.pbl5server.authentication_service.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.common_service.constant.CommonConstant;
import org.dhv.pbl5server.constant_service.entity.Constant;
import org.hibernate.validator.constraints.Length;

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
    @NotBlank
    @Length(min = 8, max = 20)
    @Pattern(regexp = CommonConstant.PASSWORD_REGEXP_PATTERN)
    private String password;
    @NotNull
    @NotBlank
    private String address;
    @NotNull
    @NotBlank
    private String phoneNumber;
    @NotNull
    private Constant systemRole;
    @NotNull
    @NotBlank
    private String companyName;
    @NotNull
    @NotBlank
    private String companyUrl;
    @NotNull
    private Timestamp establishedDate;
}
