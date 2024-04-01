package org.dhv.pbl5server.profile_service.service;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.profile_service.entity.Company;
import org.dhv.pbl5server.profile_service.model.OtherDescription;
import org.dhv.pbl5server.profile_service.payload.request.CompanyProfileRequest;
import org.dhv.pbl5server.profile_service.payload.response.CompanyProfileResponse;

import java.util.List;
import java.util.UUID;

public interface CompanyService {
    CompanyProfileResponse getCompanyProfile(Account account);

    CompanyProfileResponse getCompanyProfileById(String accountId);

    CompanyProfileResponse updateCompanyProfile(Account account, CompanyProfileRequest request);

    CompanyProfileResponse insertOtherDescriptions(Account account, List<OtherDescription> request);

    CompanyProfileResponse updateOtherDescriptions(Account account, List<OtherDescription> request);

    void deleteOtherDescriptions(Account account, List<String> ids);

    OtherDescription getOtherDescriptionById(String companyId, String id);


    Company getAllDataByAccountId(UUID accountId);
}
