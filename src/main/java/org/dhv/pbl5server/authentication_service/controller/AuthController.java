package org.dhv.pbl5server.authentication_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.annotation.CurrentAccount;
import org.dhv.pbl5server.authentication_service.annotation.PreAuthorizeSystemRole;
import org.dhv.pbl5server.authentication_service.annotation.PreAuthorizeSystemRoleWithoutAdmin;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.payload.request.*;
import org.dhv.pbl5server.authentication_service.service.AuthService;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// git commit -m "PBL-511 login for company and user"
// git commit -m "PBL-512 login for company and user"
// git commit -m "PBL-514 register for user"
// git commit -m "PBL-518 forgot password for company"
// git commit -m "PBL-519 forgot password for user"
// git commit -m "PBL-515 refresh token for admin"
// git commit -m "PBL-516 refresh token for company"
// git commit -m "PBL-517 refresh token for user"

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiDataResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(authService.login(loginRequest, false)));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiDataResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity
                .ok(ApiDataResponse.successWithoutMeta(authService.refreshToken(refreshTokenRequest, false)));
    }

    @PreAuthorizeSystemRole
    @PostMapping("/logout")
    public ResponseEntity<ApiDataResponse> logout(@CurrentAccount Account currentAccount) {
        authService.logout(currentAccount);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    @PreAuthorizeSystemRole
    @GetMapping("/account")
    public ResponseEntity<ApiDataResponse> getAccount(@CurrentAccount Account currentAccount) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(authService.getAccount(currentAccount)));
    }

    @PreAuthorizeSystemRole
    @GetMapping("/account/{id}")
    public ResponseEntity<ApiDataResponse> getAccountById(@PathVariable("id") String id) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(authService.getAccountById(id)));
    }

    @PostMapping("/user-register")
    public ResponseEntity<ApiDataResponse> userRegister(@Valid @RequestBody UserRegisterRequest request) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(authService.register(request)));
    }

    @PostMapping("/company-register")
    public ResponseEntity<ApiDataResponse> companyRegister(@Valid @RequestBody CompanyRegisterRequest request) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(authService.register(request)));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiDataResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiDataResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    @PreAuthorizeSystemRoleWithoutAdmin
    @PostMapping("/change-password")
    public ResponseEntity<ApiDataResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @CurrentAccount Account currentAccount) {
        authService.changePassword(request, currentAccount);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

}
