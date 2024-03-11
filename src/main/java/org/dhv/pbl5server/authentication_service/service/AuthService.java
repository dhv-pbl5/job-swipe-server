package org.dhv.pbl5server.authentication_service.service;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.payload.request.LoginRequest;
import org.dhv.pbl5server.authentication_service.payload.request.RefreshTokenRequest;
import org.dhv.pbl5server.authentication_service.payload.request.RegisterRequest;
import org.dhv.pbl5server.authentication_service.payload.response.AccountResponse;
import org.dhv.pbl5server.authentication_service.payload.response.CredentialResponse;


public interface AuthService {
    CredentialResponse login(LoginRequest loginRequest, boolean isAdmin);

    CredentialResponse refreshToken(RefreshTokenRequest refreshTokenRequest, boolean isAdmin);

    AccountResponse getAccount(Account currentAccount);

    void logout(Account currentAccount);

    AccountResponse register(RegisterRequest request);
}
