package org.dhv.pbl5server.profile_service.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.dhv.pbl5server.authentication_service.payload.response.AccountResponse;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.profile_service.model.OtherDescription;

import java.util.List;
import java.util.UUID;

// git commit -m "PBL-536 user profile"

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonSnakeCaseNaming
public class UserProfileResponse extends AccountResponse {
    private UUID accountId;
    private String firstName;
    private String lastName;
    private Boolean gender;
    private String dateOfBirth;
    private String summaryIntroduction;
    private List<String> socialMediaLink;
    private List<OtherDescription> others;
    private List<UserEducationResponse> educations;
    private List<UserAwardResponse> awards;
    private List<UserExperienceResponse> experiences;
}
