package org.dhv.pbl5server.admin_service.service;

import org.dhv.pbl5server.authentication_service.payload.response.AccountResponse;
import org.dhv.pbl5server.profile_service.payload.response.CompanyProfileResponse;
import org.dhv.pbl5server.profile_service.payload.response.UserProfileResponse;

import java.util.List;

public interface AdminService {

    List<CompanyProfileResponse> getAllCompany();

    List<UserProfileResponse> getAllUser();


    AccountResponse activateAccount(String accountId);

    AccountResponse deactivateAccount(String accountId);
}
