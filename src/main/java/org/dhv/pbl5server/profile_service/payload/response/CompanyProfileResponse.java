package org.dhv.pbl5server.profile_service.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.dhv.pbl5server.authentication_service.payload.response.AccountResponse;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.profile_service.model.OtherDescription;

import java.util.List;
import java.util.UUID;

// git commit -m "PBL-513 register for company"

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class CompanyProfileResponse extends AccountResponse {
    private UUID accountId;
    private String companyName;
    private String companyUrl;
    private String establishedDate;
    private List<OtherDescription> others;
}
