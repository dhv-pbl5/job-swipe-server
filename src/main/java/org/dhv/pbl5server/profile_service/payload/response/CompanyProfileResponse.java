package org.dhv.pbl5server.profile_service.payload.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.constant_service.payload.ConstantResponse;
import org.dhv.pbl5server.profile_service.model.OtherDescription;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class CompanyProfileResponse {
    // Account entity data
    private String email;
    private Boolean accountStatus;
    private String address;
    private String avatar;
    private String phoneNumber;
    private ConstantResponse systemRole;
    private List<ApplicationPositionResponse> applicationPositions;
    private Timestamp deletedAt;
    // User entity data
    private UUID accountId;
    private String companyName;
    private String companyUrl;
    private Timestamp establishedDate;
    private List<OtherDescription> others;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
