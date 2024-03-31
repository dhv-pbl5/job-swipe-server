package org.dhv.pbl5server.profile_service.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class UserBasicInfoRequest {
    @NotNull
    private Boolean accountStatus;
    @NotBlank
    @NotNull
    private String address;
    @NotBlank
    @NotNull
    private String phoneNumber;
    @NotBlank
    @NotNull
    private String firstName;
    @NotBlank
    @NotNull
    private String lastName;
    @NotNull
    private Boolean gender;
    @NotNull
    private Timestamp dateOfBirth;
    private String summaryIntroduction;
    private List<String> socialMediaLink;
}
