package org.dhv.pbl5server.authentication_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.mapper.AccountMapper;
import org.dhv.pbl5server.authentication_service.payload.request.LoginRequest;
import org.dhv.pbl5server.authentication_service.payload.request.RefreshTokenRequest;
import org.dhv.pbl5server.authentication_service.payload.request.RegisterRequest;
import org.dhv.pbl5server.authentication_service.payload.response.AccountResponse;
import org.dhv.pbl5server.authentication_service.payload.response.CredentialResponse;
import org.dhv.pbl5server.authentication_service.repository.AccountRepository;
import org.dhv.pbl5server.authentication_service.service.AuthService;
import org.dhv.pbl5server.authentication_service.service.JwtService;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.exception.ForbiddenException;
import org.dhv.pbl5server.constant_service.entity.Constant;
import org.dhv.pbl5server.constant_service.enums.ConstantType;
import org.dhv.pbl5server.constant_service.enums.SystemRole;
import org.dhv.pbl5server.constant_service.service.ConstantService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AccountRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AccountMapper mapper;
    private final ConstantService constantService;

    public CredentialResponse login(LoginRequest loginRequest, boolean isAdmin) {
        try {
            Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Account account = (Account) authentication.getPrincipal();
            // Check if the user is an admin
            if (isAdmin && !account.getSystemRole().getConstantName().equals(SystemRole.ADMIN.name())) {
                throw new ForbiddenException(ErrorMessageConstant.FORBIDDEN);
            }
            CredentialResponse response = jwtService.generateToken(account.getAccountId().toString());
            account.setRefreshToken(response.getRefreshToken());
            repository.save(account);
            return response;
        } catch (BadCredentialsException ex) {
            throw new BadRequestException(ErrorMessageConstant.INCORRECT_EMAIL_OR_PASSWORD);
        } catch (InternalAuthenticationServiceException ex) {
            throw new BadRequestException(ErrorMessageConstant.ACCOUNT_NOT_FOUND);
        } catch (DisabledException ex) {
            throw new ForbiddenException(ErrorMessageConstant.ACCOUNT_IS_DISABLED);
        } catch (Exception ex) {
            log.error("Error when login", ex);
            throw ex;
        }
    }

    @Override
    public CredentialResponse refreshToken(RefreshTokenRequest refreshTokenRequest, boolean isAdmin) {
        try {

            return jwtService.refreshToken(refreshTokenRequest.getRefreshToken(), isAdmin);
        } catch (Exception ex) {
            log.error("Error when refresh token", ex);
            throw ex;
        }
    }

    @Override
    public AccountResponse getAccount(Account currentAccount) {
        return mapper.toAccountResponse(currentAccount);
    }

    @Override
    public void logout(Account currentAccount) {
        currentAccount.setRefreshToken(null);
        repository.save(currentAccount);
        SecurityContextHolder.clearContext();
    }

    @Override
    public AccountResponse register(RegisterRequest request) {
        // Check if the email is already used
        if (repository.existsByEmail(request.getEmail()))
            throw new BadRequestException(ErrorMessageConstant.EMAIL_ALREADY_EXISTS);
        // Check constant's id is not null
        if (request.getSystemRole().getConstantId() == null)
            throw new BadRequestException(ErrorMessageConstant.SYSTEM_ROLE_NOT_FOUND);
        Constant role = constantService.getConstantById(request.getSystemRole().getConstantId());
        // Check constant is system role
        if (role.getConstantType() != ConstantType.SYSTEM_ROLE.getIndex())
            throw new BadRequestException(ErrorMessageConstant.SYSTEM_ROLE_NOT_FOUND);
        // Check if the role is admin
        if (role.getConstantName().equalsIgnoreCase(SystemRole.ADMIN.name()))
            throw new BadRequestException(ErrorMessageConstant.ROLE_NOT_VALID);
        Account account = repository.save(mapper.toAccount(request));
        return mapper.toAccountResponse(account);
    }
}
