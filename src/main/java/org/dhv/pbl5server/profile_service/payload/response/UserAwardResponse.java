package org.dhv.pbl5server.profile_service.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;

import java.util.UUID;

// git commit -m "PBL-563 user award"

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class UserAwardResponse {
    private UUID id;
    private String certificateTime;
    private String certificateName;
    private String note;
    private String createdAt;
    private String updatedAt;
}
