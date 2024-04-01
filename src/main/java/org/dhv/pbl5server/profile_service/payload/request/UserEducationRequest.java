package org.dhv.pbl5server.profile_service.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.common_service.annotation.NotBlankStringValidation;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class UserEducationRequest {
    private UUID id;
    @NotBlankStringValidation
    private String studyPlace;
    @NotNull
    private Timestamp studyStartTime;
    @NotNull
    private Timestamp studyEndTime;
    @NotBlankStringValidation
    private String majority;
    @NotNull
    private Double cpa;
    private String note;
}
