package org.dhv.pbl5server.profile_service.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.constant_service.payload.ConstantResponse;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class ApplicationPositionResponse {
    private UUID id;
    private Boolean status;
    private ConstantResponse applyPosition;
    private ConstantResponse salaryRange;
    private List<ApplicationSkillResponse> skills;
    private String createdAt;
    private String updatedAt;
}
