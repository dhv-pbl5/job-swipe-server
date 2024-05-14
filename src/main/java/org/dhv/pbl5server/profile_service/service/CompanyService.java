package org.dhv.pbl5server.profile_service.service;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.profile_service.entity.Company;
import org.dhv.pbl5server.profile_service.model.OtherDescription;
import org.dhv.pbl5server.profile_service.payload.request.CompanyProfileRequest;
import org.dhv.pbl5server.profile_service.payload.response.CompanyProfileResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

// git commit -m "PBL-538 company profile"
// git commit -m "PBL-534 application position"

public interface CompanyService {
    CompanyProfileResponse getCompanyProfile(Account account);

    CompanyProfileResponse getCompanyProfileById(String accountId);

    CompanyProfileResponse updateCompanyProfile(Account account, CompanyProfileRequest request);

    CompanyProfileResponse insertOtherDescriptions(Account account, List<OtherDescription> request);

    CompanyProfileResponse updateOtherDescriptions(Account account, List<OtherDescription> request);

    void deleteOtherDescriptions(Account account, List<String> ids);

    OtherDescription getOtherDescriptionById(String companyId, String id);

    String updateAvatar(Account account, MultipartFile file);

    Company getAllDataByAccountId(UUID accountId);

    Company getAllDataByAccountId(Company company, UUID accountId);

    ApiDataResponse getAllData(Pageable pageable);
}
