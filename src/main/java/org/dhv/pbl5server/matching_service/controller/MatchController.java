package org.dhv.pbl5server.matching_service.controller;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.annotation.CurrentAccount;
import org.dhv.pbl5server.authentication_service.annotation.PreAuthorizeSystemRoleWithoutAdmin;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.enums.DataSortOrder;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.PageUtils;
import org.dhv.pbl5server.matching_service.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/matching")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService service;

    @PreAuthorizeSystemRoleWithoutAdmin
    @GetMapping("")
    public ResponseEntity<ApiDataResponse> getMatches(
        @Nullable @RequestParam("matching_id") String matchingId,
        @Nullable @RequestParam("page") Integer page,
        @Nullable @RequestParam("paging") Integer paging,
        @Nullable @RequestParam("sort_by") String sortBy,
        @Nullable @RequestParam("order") DataSortOrder order,
        @CurrentAccount Account account
    ) {
        // Get matching by id
        if (CommonUtils.isNotEmptyOrNullString(matchingId))
            return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getMatchById(account, matchingId)));
        // Get matching by page
        var pageRequest = PageUtils.makePageRequest(
            sortBy == null ? "created_at" : sortBy,
            order == null ? DataSortOrder.DESC : order,
            page, paging);
        return ResponseEntity.ok(service.getMatches(account, pageRequest));
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
        @RequestParam("matching_id") String matchingId,
        @CurrentAccount Account account
    ) {
        if (!CommonUtils.isValidUuid(matchingId))
            throw new BadRequestException(ErrorMessageConstant.MATCH_ID_INVALID);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.acceptMatch(account, matchingId)));
    }

    @PreAuthorizeSystemRoleWithoutAdmin
    @PostMapping("/reject")
    public ResponseEntity<ApiDataResponse> rejectMatch(
        @Nullable @RequestParam("matching_id") String matchingId,
        @CurrentAccount Account account
    ) {
        if (!CommonUtils.isValidUuid(matchingId))
            throw new BadRequestException(ErrorMessageConstant.MATCH_ID_INVALID);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.requestMatch(account, matchingId)));
    }
}
