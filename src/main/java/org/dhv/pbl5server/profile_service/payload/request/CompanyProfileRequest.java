package org.dhv.pbl5server.profile_service.payload.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.common_service.annotation.NotBlankStringValidation;
import org.dhv.pbl5server.profile_service.model.OtherDescription;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class CompanyProfileRequest {
    private UUID accountId;
    @NotBlankStringValidation
    private String companyName;
    @NotBlankStringValidation
    private String companyUrl;
    @NotNull
    private Timestamp establishedDate;
    private List<OtherDescription> others;
}
