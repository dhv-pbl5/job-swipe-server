package org.dhv.pbl5server.profile_service.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.common_service.annotation.NotBlankStringValidation;
import org.dhv.pbl5server.constant_service.payload.ConstantSelectionRequest;

import java.sql.Timestamp;
import java.util.UUID;

// git commit -m "PBL-559 user experience"

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class UserExperienceRequest {
    private UUID id;
    @NotNull
    private Timestamp experienceStartTime;
    @NotNull
    private Timestamp experienceEndTime;
    @NotNull
    @Valid
    private ConstantSelectionRequest experienceType;
    @NotBlankStringValidation
    private String workPlace;
    @NotBlankStringValidation
    private String position;
    private String note;
}
