package org.dhv.pbl5server.profile_service.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.constant_service.payload.ConstantSelectionRequest;

import java.util.UUID;

// git commit -m "PBL-526 position and skill"
// git commit -m "PBL-534 application position"
// git commit -m "PBL-530 update application position"

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class ApplicationSkillRequest {
    private UUID id;
    private String note;
    @NotNull
    @Valid
    private ConstantSelectionRequest skill;
}
