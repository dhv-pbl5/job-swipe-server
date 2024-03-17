package org.dhv.pbl5server.profile_service.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import org.dhv.pbl5server.constant_service.entity.Constant;
import org.dhv.pbl5server.profile_service.config.OtherDescription;
import org.dhv.pbl5server.profile_service.entity.ApplicationPosition;
import org.dhv.pbl5server.profile_service.entity.UserAward;
import org.dhv.pbl5server.profile_service.entity.UserEducation;
import org.dhv.pbl5server.profile_service.entity.UserExperience;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class UserProfileResponse {
    // Account entity data
    private String email;
    private Boolean accountStatus;
    private String address;
    private String avatar;
    private String phoneNumber;
    private Constant systemRole;
    // User entity data
    private UUID accountId;
    private String firstName;
    private String lastName;
    private Boolean gender;
    private Timestamp dateOfBirth;
    private String summaryIntroduction;
    private List<String> socialMediaLink;
    private List<OtherDescription> other;
    private List<UserEducation> educations;
    private List<UserAward> awards;
    private List<UserExperience> experiences;
    private List<ApplicationPosition> applicationPositions;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
}
