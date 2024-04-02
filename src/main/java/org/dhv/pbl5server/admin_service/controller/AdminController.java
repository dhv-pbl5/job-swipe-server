package org.dhv.pbl5server.admin_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.admin_service.enums.GetAllByType;
import org.dhv.pbl5server.admin_service.service.AdminService;
import org.dhv.pbl5server.authentication_service.annotation.PreAuthorizeAdmin;
import org.dhv.pbl5server.authentication_service.payload.request.LoginRequest;
import org.dhv.pbl5server.authentication_service.payload.request.RefreshTokenRequest;
import org.dhv.pbl5server.authentication_service.service.AuthService;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService service;
    private final AuthService authService;

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
        @NotNull @RequestParam GetAllByType type
    ) {
        return switch (type) {
            case COMPANY -> ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getAllCompany()));
            case USER -> ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getAllUser()));
        };
    }
}
