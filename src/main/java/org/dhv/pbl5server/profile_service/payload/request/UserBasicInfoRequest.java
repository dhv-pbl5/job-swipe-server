package org.dhv.pbl5server.profile_service.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.common_service.annotation.NotBlankStringValidation;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class UserBasicInfoRequest {
    @NotNull
    private Boolean accountStatus;
    @NotBlankStringValidation
    private String address;
    @NotBlankStringValidation
    private String phoneNumber;
    @NotBlankStringValidation
    private String firstName;
    @NotBlankStringValidation
    private String lastName;
    @NotNull
    private Boolean gender;
    @NotNull
    private Timestamp dateOfBirth;
    private String summaryIntroduction;
    private List<String> socialMediaLink;
}
