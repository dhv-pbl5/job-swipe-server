package org.dhv.pbl5server.admin_service.service;

import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {

    ApiDataResponse getAllCompany(Pageable pageRequest);

    ApiDataResponse getAllUser(Pageable pageRequest);

    ApiDataResponse getAllApplicationPosition(String accountId, Pageable pageRequest);

    ApiDataResponse getAllUserAward(String userId, Pageable pageRequest);

    ApiDataResponse getAllUserEducation(String userId, Pageable pageRequest);

    ApiDataResponse getAllUserExperience(String userId, Pageable pageRequest);

    void activateAccount(List<String> accountIds);

    void deactivateAccount(List<String> accountIds);

    void initialDefaultAccount();
}
