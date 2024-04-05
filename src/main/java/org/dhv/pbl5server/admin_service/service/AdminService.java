package org.dhv.pbl5server.admin_service.service;

import org.dhv.pbl5server.authentication_service.payload.response.AccountResponse;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    ApiDataResponse getAllCompany(Pageable pageRequest);

    ApiDataResponse getAllUser(Pageable pageRequest);


    AccountResponse activateAccount(String accountId);

    AccountResponse deactivateAccount(String accountId);
}