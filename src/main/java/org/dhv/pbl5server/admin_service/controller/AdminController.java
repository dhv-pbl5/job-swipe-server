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
import org.dhv.pbl5server.constant_service.enums.ConstantTypePrefix;
import org.dhv.pbl5server.constant_service.service.ConstantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService service;
    private final AuthService authService;
    private final ConstantService constantService;

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
    @PostMapping("/deactivate-account")
    public ResponseEntity<ApiDataResponse> deactivateAccount(@RequestBody List<String> accountIds) {
        service.deactivateAccount(accountIds);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    @PreAuthorizeAdmin
    @PostMapping("/activate-account")
    public ResponseEntity<ApiDataResponse> activateAccount(@RequestBody List<String> accountIds) {
        service.activateAccount(accountIds);
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
            case CONSTANT -> {
                if (constantTypePrefix != null && constantTypePrefix.length() == 2) {
                    var tmp = AbstractEnum.fromString(ConstantTypePrefix.values(), constantTypePrefix);
                    yield ResponseEntity.ok(ApiDataResponse.successWithoutMeta(constantService.getConstantsByTypePrefix(tmp)));
                } else {
                    yield ResponseEntity.ok(ApiDataResponse.error(null));
                }
            }
        };
    }

    @PreAuthorizeAdmin
    @PostMapping("/constant")
    public ResponseEntity<ApiDataResponse> createConstant(@Valid @RequestBody Map<String, String> request) {
        System.out.println(request);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    @PreAuthorizeAdmin
    @DeleteMapping("/constant")
    public ResponseEntity<ApiDataResponse> deleteConstant(@RequestBody List<String> ids) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }
}
