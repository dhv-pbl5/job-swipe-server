package org.dhv.pbl5server.matching_service.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.annotation.CurrentAccount;
import org.dhv.pbl5server.authentication_service.annotation.PreAuthorizeCompany;
import org.dhv.pbl5server.authentication_service.annotation.PreAuthorizeSystemRoleWithoutAdmin;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.PageUtils;
import org.dhv.pbl5server.matching_service.enums.GetMatchType;
import org.dhv.pbl5server.matching_service.payload.InterviewInvitationRequest;
import org.dhv.pbl5server.matching_service.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/matched-pairs")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService service;

    @PreAuthorizeSystemRoleWithoutAdmin
    @GetMapping("")
    public ResponseEntity<ApiDataResponse> getMatches(
        @Nullable @RequestParam("match_id") String matchingId,
        @Nullable @RequestParam("type") String type,
        @Nullable @RequestParam("page") Integer page,
        @Nullable @RequestParam("paging") Integer paging,
        @CurrentAccount Account account
    ) {
        // Get matching by id
        if (CommonUtils.isNotEmptyOrNullString(matchingId))
            return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getMatchById(account, matchingId)));
        // Get matching by type
        var typeEnum = AbstractEnum.fromString(GetMatchType.values(), type);
        var pageRequest = PageUtils.makePageRequest("created_at", "desc", page, paging);
        return switch (typeEnum) {
            case ALL -> ResponseEntity.ok(service.getMatches(account, pageRequest));
            case ACCEPTED -> ResponseEntity.ok(service.getAcceptedMatches(account, pageRequest));
            case REJECTED -> ResponseEntity.ok(service.getRejectedMatches(account, pageRequest));
            case REQUESTED -> ResponseEntity.ok(service.getRequestedMatches(account, pageRequest));
        };
    }

    @PreAuthorizeSystemRoleWithoutAdmin
    @GetMapping("/by-account")
    public ResponseEntity<ApiDataResponse> getMatchByUserId(
        @NotNull @RequestParam("account_id") String accountId,
        @CurrentAccount Account account
    ) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getMatchByAccountId(account, accountId)));
    }

    @PreAuthorizeSystemRoleWithoutAdmin
    @PostMapping("/request")
    public ResponseEntity<ApiDataResponse> requestMatch(
        @RequestParam("requested_account_id") String requestedAccountId,
        @CurrentAccount Account account
    ) {
        if (!CommonUtils.isValidUuid(requestedAccountId))
            throw new BadRequestException(ErrorMessageConstant.REQUESTED_ACCOUNT_ID_INVALID);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.requestMatch(account, requestedAccountId)));
    }

    @PreAuthorizeSystemRoleWithoutAdmin
    @PostMapping("/accept")
    public ResponseEntity<ApiDataResponse> acceptMatch(
        @RequestParam("match_id") String matchingId,
        @CurrentAccount Account account
    ) {
        if (!CommonUtils.isValidUuid(matchingId))
            throw new BadRequestException(ErrorMessageConstant.MATCH_ID_INVALID);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.acceptMatch(account, matchingId)));
    }

    @PreAuthorizeSystemRoleWithoutAdmin
    @PostMapping("/reject")
    public ResponseEntity<ApiDataResponse> rejectMatch(
        @RequestParam("match_id") String matchingId,
        @CurrentAccount Account account
    ) {
        if (!CommonUtils.isValidUuid(matchingId))
            throw new BadRequestException(ErrorMessageConstant.MATCH_ID_INVALID);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.rejectMatch(account, matchingId)));
    }

    @PreAuthorizeCompany
    @PostMapping("/interview-invitation")
    public ResponseEntity<ApiDataResponse> sendInterviewInvitationMail(
        @Valid @RequestBody InterviewInvitationRequest request,
        @CurrentAccount Account account
    ) {
        service.sendInterviewInvitation(account, request);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }
}
