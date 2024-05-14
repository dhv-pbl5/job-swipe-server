package org.dhv.pbl5server.matching_service.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dhv.pbl5server.profile_service.payload.response.CompanyProfileResponse;
import org.dhv.pbl5server.profile_service.payload.response.UserProfileResponse;

import java.util.UUID;

// git commit -m "PBL-594 realtime matching for company"

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class MatchResponse {
    private UUID id;
    private String matchedTime;
    private UserProfileResponse user;
    private Boolean userMatched;
    private CompanyProfileResponse company;
    private Boolean companyMatched;
    private String createdAt;
    private String updatedAt;
}
