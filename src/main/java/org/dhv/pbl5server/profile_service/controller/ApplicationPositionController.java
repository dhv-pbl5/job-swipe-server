package org.dhv.pbl5server.profile_service.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.annotation.CurrentAccount;
import org.dhv.pbl5server.authentication_service.annotation.PreAuthorizeSystemRoleWithoutAdmin;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.utils.ObjectValidator;
import org.dhv.pbl5server.profile_service.payload.request.ApplicationPositionRequest;
import org.dhv.pbl5server.profile_service.payload.request.ApplicationSkillRequest;
import org.dhv.pbl5server.profile_service.service.ApplicationPositionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// git commit -m "PBL-526 position and skill"
// git commit -m "PBL-536 user profile"
// git commit -m "PBL-534 application position"

@RestController
@RequestMapping("/v1/account/application-positions")
@RequiredArgsConstructor
public class ApplicationPositionController {
    private final ApplicationPositionService service;
    private final ObjectValidator listValidator;

    @PreAuthorizeSystemRoleWithoutAdmin
    @PostMapping("")
    public ResponseEntity<ApiDataResponse> insertApplicationPositions(
            @RequestBody List<ApplicationPositionRequest> requests,
            @CurrentAccount Account account) {
        listValidator.validate(requests);
        return ResponseEntity
                .ok(ApiDataResponse.successWithoutMeta(service.insertApplicationPositions(account, requests)));
    }

    @PreAuthorizeSystemRoleWithoutAdmin
    @PatchMapping("")
    public ResponseEntity<ApiDataResponse> updateApplicationPosition(
            @Valid @RequestBody List<ApplicationPositionRequest> requests,
            @CurrentAccount Account account) {
        listValidator.validate(requests);
        return ResponseEntity
                .ok(ApiDataResponse.successWithoutMeta(service.updateApplicationPosition(account, requests)));
    }

    @PreAuthorizeSystemRoleWithoutAdmin
    @PatchMapping("/{application_position_id}")
    public ResponseEntity<ApiDataResponse> updateApplicationSkill(
            @PathVariable("application_position_id") String applicationPositionId,
            @RequestBody List<ApplicationSkillRequest> request,
            @CurrentAccount Account account) {
        listValidator.validate(request);
        return ResponseEntity.ok(ApiDataResponse
                .successWithoutMeta(service.insertOrUpdateApplicationSkills(account, applicationPositionId, request)));
    }

    @PreAuthorizeSystemRoleWithoutAdmin
    @GetMapping("")
    public ResponseEntity<ApiDataResponse> getApplicationPositions(
            @Nullable @RequestParam("account_id") String accountId,
            @Nullable @RequestParam("application_position_id") String applicationPositionId,
            @CurrentAccount Account account) {
        if (accountId != null && applicationPositionId != null)
            return ResponseEntity.ok(ApiDataResponse
                    .successWithoutMeta(service.getApplicationPositionById(accountId, applicationPositionId)));
        if (accountId != null)
            return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getApplicationPositions(accountId)));
        return ResponseEntity.ok(
                ApiDataResponse.successWithoutMeta(service.getApplicationPositions(account.getAccountId().toString())));
    }

    @PreAuthorizeSystemRoleWithoutAdmin
    @DeleteMapping("")
    public ResponseEntity<ApiDataResponse> deleteApplicationPositions(
            @Nullable @RequestParam("application_position_id") String applicationPositionId,
            @RequestBody List<String> ids,
            @CurrentAccount Account account) {
        if (applicationPositionId != null)
            service.deleteApplicationSkills(account, applicationPositionId, ids);
        else
            service.deleteApplicationPositions(account, ids);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }
}
