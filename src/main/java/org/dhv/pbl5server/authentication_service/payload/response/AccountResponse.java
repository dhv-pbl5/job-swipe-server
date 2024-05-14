package org.dhv.pbl5server.authentication_service.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.constant_service.payload.ConstantResponse;
import org.dhv.pbl5server.profile_service.payload.response.ApplicationPositionResponse;
import org.dhv.pbl5server.profile_service.payload.response.LanguageResponse;

import java.util.List;
import java.util.UUID;

// git commit -m "PBL-513 register for company"

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@JsonSnakeCaseNaming
public class AccountResponse {
    private UUID accountId;
    private String email;
    private Boolean accountStatus;
    private String address;
    private String avatar;
    private String phoneNumber;
    private ConstantResponse systemRole;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private List<ApplicationPositionResponse> applicationPositions;
    private List<LanguageResponse> languages;
}
