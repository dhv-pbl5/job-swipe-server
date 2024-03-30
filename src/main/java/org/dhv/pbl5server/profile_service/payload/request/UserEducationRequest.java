package org.dhv.pbl5server.profile_service.payload.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserEducationRequest {
    private UUID id;
    @NotNull
    @NotBlank
    private String studyPlace;
    @NotNull
    private Timestamp studyStartTime;
    @NotNull
    private Timestamp studyEndTime;
    @NotNull
    @NotBlank
    private String majority;
    @NotNull
    private Double cpa;
    private String note;
}
