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
public class UserAwardResponse {
    private UUID id;
    private Timestamp certificateTime;
    private String certificateName;
    private String note;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
