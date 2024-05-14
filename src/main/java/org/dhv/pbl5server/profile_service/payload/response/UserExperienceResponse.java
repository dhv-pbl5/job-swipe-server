package org.dhv.pbl5server.profile_service.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.constant_service.entity.Constant;

import java.util.UUID;

// git commit -m "PBL-559 user experience"
// git commit -m "PBL-557 update user experience"

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class UserExperienceResponse {
    private UUID id;
    private String experienceStartTime;
    private String experienceEndTime;
    private Constant experienceType;
    private String workPlace;
    private String position;
    private String note;
    private String createdAt;
    private String updatedAt;
}
