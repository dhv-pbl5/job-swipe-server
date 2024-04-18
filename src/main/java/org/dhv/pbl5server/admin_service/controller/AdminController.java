package org.dhv.pbl5server.admin_service.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.admin_service.enums.GetAllByType;
import org.dhv.pbl5server.admin_service.service.AdminService;
import org.dhv.pbl5server.authentication_service.annotation.PreAuthorizeAdmin;
import org.dhv.pbl5server.authentication_service.payload.request.LoginRequest;
import org.dhv.pbl5server.authentication_service.payload.request.RefreshTokenRequest;
import org.dhv.pbl5server.authentication_service.service.AuthService;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.PageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService service;
    private final AuthService authService;

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
    public ResponseEntity<ApiDataResponse> refreshTokenAdmin(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(authService.refreshToken(refreshTokenRequest, true)));
    }

    @PreAuthorizeAdmin
    @PostMapping("/deactivate-account/{accountId}")
    public ResponseEntity<ApiDataResponse> deactivateAccount(@PathVariable("accountId") String accountId) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.deactivateAccount(accountId)));
    }

    @PreAuthorizeAdmin
    @PostMapping("/activate-account/{accountId}")
    public ResponseEntity<ApiDataResponse> activateAccount(@PathVariable("accountId") String accountId) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.activateAccount(accountId)));
    }

    @PreAuthorizeAdmin
    @GetMapping("")
    public ResponseEntity<ApiDataResponse> getAll(
        @NotNull @RequestParam String type,
        @Nullable @RequestParam("page") Integer page,
        @Nullable @RequestParam("paging") Integer paging,
        @Nullable @RequestParam("sort_by") String sortBy,
        @Nullable @RequestParam("order") String order,
        @Nullable @RequestParam("account_id") String accountId
    ) {
        var pageRequest = PageUtils.makePageRequest(sortBy, order, page, paging);
        return switch (AbstractEnum.fromString(GetAllByType.values(), type)) {
            case COMPANY -> ResponseEntity.ok(service.getAllCompany(pageRequest));
            case USER -> ResponseEntity.ok(service.getAllUser(pageRequest));
            case APPLICATION_POSITION -> {
                if (CommonUtils.isEmptyOrNullString(accountId))
                    throw new BadRequestException(ErrorMessageConstant.ACCOUNT_ID_IS_REQUIRED);
                yield ResponseEntity.ok(service.getAllApplicationPosition(accountId, pageRequest));
            }
            case USER_EXPERIENCE -> {
                if (CommonUtils.isEmptyOrNullString(accountId))
                    throw new BadRequestException(ErrorMessageConstant.ACCOUNT_ID_IS_REQUIRED);
                yield ResponseEntity.ok(service.getAllUserExperience(accountId, pageRequest));
            }
            case USER_AWARD -> {
                if (CommonUtils.isEmptyOrNullString(accountId))
                    throw new BadRequestException(ErrorMessageConstant.ACCOUNT_ID_IS_REQUIRED);
                yield ResponseEntity.ok(service.getAllUserAward(accountId, pageRequest));
            }
            case USER_EDUCATION -> {
                if (CommonUtils.isEmptyOrNullString(accountId))
                    throw new BadRequestException(ErrorMessageConstant.ACCOUNT_ID_IS_REQUIRED);
                yield ResponseEntity.ok(service.getAllUserEducation(accountId, pageRequest));
            }
        };
    }
}
