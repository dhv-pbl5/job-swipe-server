package org.dhv.pbl5server.profile_service.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.annotation.CurrentAccount;
import org.dhv.pbl5server.authentication_service.annotation.PreAuthorizeSystemRoleWithoutAdmin;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.utils.ObjectValidator;
import org.dhv.pbl5server.profile_service.payload.request.LanguageRequest;
import org.dhv.pbl5server.profile_service.service.LanguageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// git commit -m "PBL-536 user profile"

@RestController
@RequestMapping("/v1/account/languages")
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageService service;
    private final ObjectValidator listValidator;

    @PreAuthorizeSystemRoleWithoutAdmin
    @PostMapping("")
    public ResponseEntity<ApiDataResponse> insertLanguages(
            @RequestBody List<LanguageRequest> requests,
            @CurrentAccount Account account) {
        listValidator.validate(requests);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.insertLanguage(account, requests)));
    }

    @PreAuthorizeSystemRoleWithoutAdmin
    @PatchMapping("")
    public ResponseEntity<ApiDataResponse> updateLanguage(
            @Valid @RequestBody List<LanguageRequest> requests,
            @CurrentAccount Account account) {
        listValidator.validate(requests);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.updateLanguage(account, requests)));
    }

    @PreAuthorizeSystemRoleWithoutAdmin
    @GetMapping("")
    public ResponseEntity<ApiDataResponse> getLanguages(
            @Nullable @RequestParam("account_id") String accountId,
            @Nullable @RequestParam("language_id") String languageId,
            @CurrentAccount Account account) {
        if (accountId != null && languageId != null)
            return ResponseEntity
                    .ok(ApiDataResponse.successWithoutMeta(service.getLanguageById(accountId, languageId)));
        if (accountId != null)
            return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getLanguages(accountId)));
        return ResponseEntity
                .ok(ApiDataResponse.successWithoutMeta(service.getLanguages(account.getAccountId().toString())));
    }

    @PreAuthorizeSystemRoleWithoutAdmin
    @DeleteMapping("")
    public ResponseEntity<ApiDataResponse> deleteLanguages(
            @RequestBody List<String> ids,
            @CurrentAccount Account account) {
        service.deleteLanguages(account, ids);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }
}
