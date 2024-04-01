package org.dhv.pbl5server.profile_service.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.constant_service.entity.Constant;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class UserExperienceResponse {
    private UUID id;
    private Timestamp experienceStartTime;
    private Timestamp experienceEndTime;
    private Constant experienceType;
    private String workPlace;
    private String position;
    private String note;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
