package org.dhv.pbl5server.profile_service.service;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.profile_service.payload.request.LanguageRequest;
import org.dhv.pbl5server.profile_service.payload.response.LanguageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LanguageService {
    List<LanguageResponse> insertLanguage(Account account, List<LanguageRequest> requests);

    List<LanguageResponse> updateLanguage(Account account, List<LanguageRequest> requests);

    List<LanguageResponse> getLanguages(String accountId);

    ApiDataResponse getLanguages(String accountId, Pageable pageable);

    LanguageResponse getLanguageById(String accountId, String languageId);

    void deleteLanguages(Account account, List<String> ids);
}
