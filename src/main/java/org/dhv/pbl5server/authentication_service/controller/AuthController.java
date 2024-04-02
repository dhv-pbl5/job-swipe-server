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

import java.util.Map;

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
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(authService.refreshToken(refreshTokenRequest, false)));
    }

    @PreAuthorizeSystemRole
    @PostMapping("/logout")
    public ResponseEntity<ApiDataResponse> logoutUser(@CurrentAccount Account currentAccount) {
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

    @PreAuthorizeSystemRoleWithoutAdmin
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiDataResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        String resetPasswordToken = authService.forgotPassword(request);
        Map<String, String> data = Map.of("reset_password_token", resetPasswordToken);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(data));
    }

    @PreAuthorizeSystemRoleWithoutAdmin
    @PostMapping("/reset-password")
    public ResponseEntity<ApiDataResponse> resetPassword(
        @Valid @RequestBody ResetPasswordRequest request,
        @CurrentAccount Account currentAccount
    ) {
        authService.resetPassword(request, currentAccount);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    @PreAuthorizeSystemRoleWithoutAdmin
    @PostMapping("/change-password")
    public ResponseEntity<ApiDataResponse> changePassword(
        @Valid @RequestBody ChangePasswordRequest request,
        @CurrentAccount Account currentAccount
    ) {
        authService.changePassword(request, currentAccount);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }


}

