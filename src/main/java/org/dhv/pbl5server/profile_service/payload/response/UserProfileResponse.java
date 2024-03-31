package org.dhv.pbl5server.profile_service.payload.response;

import lombok.Getter;
import lombok.Setter;
import org.dhv.pbl5server.authentication_service.entity.ApplicationPosition;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.constant_service.payload.ConstantResponse;
import org.dhv.pbl5server.profile_service.model.OtherDescription;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@JsonSnakeCaseNaming
public class UserProfileResponse {
    // Account entity data
    private String email;
    private Boolean accountStatus;
    private String address;
    private String avatar;
    private String phoneNumber;
    private ConstantResponse systemRole;
    private List<ApplicationPosition> applicationPositions;
    // User entity data
    private UUID accountId;
    private String firstName;
    private String lastName;
    private Boolean gender;
    private Timestamp dateOfBirth;
    private String summaryIntroduction;
    private List<String> socialMediaLink;
    private List<OtherDescription> others;
    private List<UserEducationResponse> educations;
    private List<UserAwardResponse> awards;
    private List<UserExperienceResponse> experiences;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
}
