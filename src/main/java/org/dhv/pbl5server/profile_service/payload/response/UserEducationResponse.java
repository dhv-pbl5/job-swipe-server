package org.dhv.pbl5server.profile_service.payload.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
