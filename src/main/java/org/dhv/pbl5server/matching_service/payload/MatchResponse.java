package org.dhv.pbl5server.matching_service.payload;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.profile_service.payload.response.CompanyProfileResponse;
import org.dhv.pbl5server.profile_service.payload.response.UserProfileResponse;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class MatchResponse {
    private UUID id;
    private Timestamp matchedTime;
    private UserProfileResponse user;
    private boolean userMatched;
    private CompanyProfileResponse company;
    private boolean companyMatched;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
