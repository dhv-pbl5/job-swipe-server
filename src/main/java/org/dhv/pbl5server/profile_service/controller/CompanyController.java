package org.dhv.pbl5server.profile_service.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.annotation.CurrentAccount;
import org.dhv.pbl5server.authentication_service.annotation.PreAuthorizeCompany;
import org.dhv.pbl5server.authentication_service.annotation.PreAuthorizeSystemRoleWithoutAdmin;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.ObjectValidator;
import org.dhv.pbl5server.profile_service.model.OtherDescription;
import org.dhv.pbl5server.profile_service.payload.request.CompanyProfileRequest;
import org.dhv.pbl5server.profile_service.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// git commit -m "PBL-538 company profile"

@RestController
@RequestMapping("/v1/profile/company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService service;
    private final ObjectValidator validator;

    @PreAuthorizeSystemRoleWithoutAdmin
    @GetMapping("")
    public ResponseEntity<ApiDataResponse> getCompanyProfile(
            @Nullable @RequestParam("company_id") String companyId,
            @CurrentAccount Account currentAccount) {
        if (CommonUtils.isNotEmptyOrNullString(companyId))
            return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getCompanyProfileById(companyId)));
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getCompanyProfile(currentAccount)));
    }

    @PreAuthorizeCompany
    @PatchMapping("")
    public ResponseEntity<ApiDataResponse> updateCompanyProfile(
            @Valid @RequestBody CompanyProfileRequest request,
            @CurrentAccount Account currentAccount) {
        return ResponseEntity
                .ok(ApiDataResponse.successWithoutMeta(service.updateCompanyProfile(currentAccount, request)));
    }

    @PreAuthorizeCompany
    @PostMapping("/others")
    public ResponseEntity<ApiDataResponse> insertOtherDescriptions(
            @RequestBody List<OtherDescription> request,
            @CurrentAccount Account currentAccount) {
        validator.validate(request);
        return ResponseEntity
                .ok(ApiDataResponse.successWithoutMeta(service.insertOtherDescriptions(currentAccount, request)));
    }

    @PreAuthorizeCompany
    @PatchMapping("/others")
    public ResponseEntity<ApiDataResponse> updateOtherDescriptions(
            @RequestBody List<OtherDescription> request,
            @CurrentAccount Account currentAccount) {
        validator.validate(request);
        return ResponseEntity
                .ok(ApiDataResponse.successWithoutMeta(service.updateOtherDescriptions(currentAccount, request)));
    }

    @PreAuthorizeCompany
    @PatchMapping("/avatar")
    public ResponseEntity<ApiDataResponse> updateAvatar(MultipartFile file, @CurrentAccount Account currentAccount) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.updateAvatar(currentAccount, file)));
    }

    @PreAuthorizeCompany
    @DeleteMapping("/others")
    public ResponseEntity<ApiDataResponse> deleteOtherDescriptions(
            @RequestBody List<String> ids,
            @CurrentAccount Account currentAccount) {
        service.deleteOtherDescriptions(currentAccount, ids);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    @PreAuthorizeCompany
    @GetMapping("/others")
    public ResponseEntity<ApiDataResponse> getOtherDescriptionById(
            @Nullable @RequestParam("company_id") String companyId,
            @RequestParam("other_id") String id,
            @CurrentAccount Account currentAccount) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getOtherDescriptionById(
                CommonUtils.isEmptyOrNullString(companyId)
                        ? currentAccount.getAccountId().toString()
                        : companyId,
                id)));
    }
}
