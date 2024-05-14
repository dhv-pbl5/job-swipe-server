package org.dhv.pbl5server.profile_service.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.constant_service.payload.ConstantResponse;

import java.util.UUID;

// git commit -m "PBL-526 position and skill"
// git commit -m "PBL-534 application position"

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class ApplicationSkillResponse {
    private UUID id;
    private String note;
    private ConstantResponse skill;
    private String createdAt;
    private String updatedAt;
}
