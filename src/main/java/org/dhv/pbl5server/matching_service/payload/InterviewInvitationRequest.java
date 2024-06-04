package org.dhv.pbl5server.matching_service.payload;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.common_service.annotation.UuidValidation;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonSnakeCaseNaming
public class InterviewInvitationRequest {
    @UuidValidation
    private String matchingId;
    @UuidValidation
    private String interviewPositionId;
    @NotNull
    private Timestamp interviewTime;
}
