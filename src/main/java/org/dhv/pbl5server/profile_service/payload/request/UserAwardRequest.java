package org.dhv.pbl5server.profile_service.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.common_service.annotation.NotBlankStringValidation;

import java.sql.Timestamp;
import java.util.UUID;

// git commit -m "PBL-563 user award"

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class UserAwardRequest {
    private UUID id;
    @NotNull
    private Timestamp certificateTime;
    @NotBlankStringValidation
    private String certificateName;
    private String note;
}
