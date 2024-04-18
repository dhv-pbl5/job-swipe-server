package org.dhv.pbl5server.authentication_service.service;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.payload.request.*;
import org.dhv.pbl5server.authentication_service.payload.response.AccountResponse;
import org.dhv.pbl5server.authentication_service.payload.response.CredentialResponse;


public interface AuthService {
    CredentialResponse login(LoginRequest loginRequest, boolean isAdmin);

    CredentialResponse refreshToken(RefreshTokenRequest refreshTokenRequest, boolean isAdmin);

    AccountResponse getAccount(Account currentAccount);

    AccountResponse getAccountById(String id);

    void logout(Account currentAccount);

    AccountResponse register(UserRegisterRequest request);

    AccountResponse register(CompanyRegisterRequest request);

    String forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request, Account currentAccount);

    void changePassword(ChangePasswordRequest request, Account currentAccount);
}
