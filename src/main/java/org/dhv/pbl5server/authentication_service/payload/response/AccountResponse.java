package org.dhv.pbl5server.authentication_service.payload.response;

import lombok.Getter;
import lombok.Setter;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.constant_service.payload.ConstantResponse;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@JsonSnakeCaseNaming
public class AccountResponse {
    private UUID accountId;
    private String email;
    private Boolean accountStatus;
    private String address;
    private String avatar;
    private String phoneNumber;
    private ConstantResponse systemRole;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
    private List<ApplicationPositionResponse> applicationPositions;
}
