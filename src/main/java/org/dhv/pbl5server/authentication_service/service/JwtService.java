package org.dhv.pbl5server.authentication_service.service;

import org.dhv.pbl5server.authentication_service.payload.response.CredentialResponse;
import org.springframework.security.core.userdetails.UserDetails;

// git commit -m "PBL-511 login for company and user"

public interface JwtService {
    UserDetails getAccountFromToken(String token);

    CredentialResponse generateToken(String accountId);

    CredentialResponse refreshToken(String refreshToken, boolean isAdmin);
}
