package org.dhv.pbl5server.profile_service.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class UserEducationResponse {
    private UUID id;
    private String studyPlace;
    private Timestamp studyStartTime;
    private Timestamp studyEndTime;
    private String majority;
    private Double cpa;
    private String note;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
