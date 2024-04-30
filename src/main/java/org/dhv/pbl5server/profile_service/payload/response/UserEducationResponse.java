package org.dhv.pbl5server.profile_service.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class UserEducationResponse {
    private UUID id;
    private String studyPlace;
    private String studyStartTime;
    private String studyEndTime;
    private String majority;
    private Double cpa;
    private String note;
    private String createdAt;
    private String updatedAt;
}
