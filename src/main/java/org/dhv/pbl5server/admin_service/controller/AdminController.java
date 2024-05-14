package org.dhv.pbl5server.admin_service.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.admin_service.enums.GetAllByType;
import org.dhv.pbl5server.admin_service.service.AdminService;
import org.dhv.pbl5server.authentication_service.annotation.CurrentAccount;
import org.dhv.pbl5server.authentication_service.annotation.PreAuthorizeAdmin;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.payload.request.LoginRequest;
import org.dhv.pbl5server.authentication_service.payload.request.RefreshTokenRequest;
import org.dhv.pbl5server.authentication_service.service.AuthService;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.utils.PageUtils;
import org.dhv.pbl5server.constant_service.enums.ConstantTypePrefix;
import org.dhv.pbl5server.constant_service.payload.ConstantRequest;
import org.dhv.pbl5server.constant_service.service.ConstantService;
import org.dhv.pbl5server.matching_service.service.MatchService;
import org.dhv.pbl5server.profile_service.service.CompanyService;
import org.dhv.pbl5server.profile_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService service;
    private final AuthService authService;
    private final ConstantService constantService;
    private final MatchService matchService;
    private final UserService userService;
    private final CompanyService companyService;

    @PostMapping("/initial-default-account")
    public ResponseEntity<ApiDataResponse> initialDefaultAccount() {
        service.initialDefaultAccount();
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiDataResponse> loginAdmin(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(authService.login(loginRequest, true)));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiDataResponse> refreshTokenAdmin(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity
                .ok(ApiDataResponse.successWithoutMeta(authService.refreshToken(refreshTokenRequest, true)));
    }

    @PreAuthorizeAdmin
    @PostMapping("/deactivate-account")
    public ResponseEntity<ApiDataResponse> deactivateAccount(@RequestBody List<String> accountIds,
            @CurrentAccount Account currentAccount) {
        service.deactivateAccount(currentAccount, accountIds);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    @PreAuthorizeAdmin
    @PostMapping("/activate-account")
    public ResponseEntity<ApiDataResponse> activateAccount(@RequestBody List<String> accountIds,
            @CurrentAccount Account currentAccount) {
        service.activateAccount(currentAccount, accountIds);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    @PreAuthorizeAdmin
    @GetMapping("")
    public ResponseEntity<ApiDataResponse> getAll(
            @NotNull @RequestParam String type,
            @Nullable @RequestParam(name = "constant_type_prefix") String constantTypePrefix,
            @Nullable @RequestParam("page") Integer page,
            @Nullable @RequestParam("paging") Integer paging,
            @Nullable @RequestParam("sort_by") String sortBy,
            @Nullable @RequestParam("order") String order,
            @Nullable @RequestParam("account_id") String accountId) {
        var pageRequest = PageUtils.makePageRequest(sortBy, order, page, paging);
        return switch (AbstractEnum.fromString(GetAllByType.values(), type)) {
            case COMPANY -> ResponseEntity.ok(companyService.getAllData(pageRequest));
            case USER -> ResponseEntity.ok(userService.getAllData(pageRequest));
            case CONSTANT -> {
                if (constantTypePrefix != null && constantTypePrefix.length() == 2) {
                    var tmp = AbstractEnum.fromString(ConstantTypePrefix.values(), constantTypePrefix);
                    yield ResponseEntity
                            .ok(ApiDataResponse.successWithoutMeta(constantService.getConstantsByTypePrefix(tmp)));
                } else {
                    yield ResponseEntity.ok(ApiDataResponse.error(null));
                }
            }
        };
    }

    @PreAuthorizeAdmin
    @GetMapping("/matches")
    public ResponseEntity<ApiDataResponse> getMatches(
            @RequestParam("account_id") String accountId,
            @Nullable @RequestParam("page") Integer page,
            @Nullable @RequestParam("paging") Integer paging,
            @Nullable @RequestParam("sort_by") String sortBy,
            @Nullable @RequestParam("order") String order) {
        var pageRequest = PageUtils.makePageRequest(sortBy, order, page, paging);
        return ResponseEntity.ok(matchService.getMatches(accountId, pageRequest));
    }

    @PreAuthorizeAdmin
    @PatchMapping("/matches")
    public ResponseEntity<ApiDataResponse> cancelMatch(
            @RequestParam("match_id") String matchId,
            @CurrentAccount Account currentAccount) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(matchService.cancelMatch(currentAccount, matchId)));
    }

    @PreAuthorizeAdmin
    @PostMapping("/constant")
    public ResponseEntity<ApiDataResponse> createConstant(@Valid @RequestBody ConstantRequest request) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(constantService.createConstant(request)));
    }

    @PreAuthorizeAdmin
    @DeleteMapping("/constant")
    public ResponseEntity<ApiDataResponse> deleteConstant(@RequestBody List<String> ids) {
        constantService.deleteConstant(ids);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }
}
