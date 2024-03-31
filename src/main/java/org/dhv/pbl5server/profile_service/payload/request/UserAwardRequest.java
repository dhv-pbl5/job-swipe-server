package org.dhv.pbl5server.profile_service.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class UserAwardRequest {
    private UUID id;
    @NotNull
    private Timestamp certificateTime;
    @NotNull
    @NotBlank
    private String certificateName;
    private String note;
}
