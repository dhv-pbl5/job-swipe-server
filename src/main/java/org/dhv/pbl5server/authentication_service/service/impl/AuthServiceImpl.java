package org.dhv.pbl5server.authentication_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.mapper.AccountMapper;
import org.dhv.pbl5server.authentication_service.payload.request.*;
import org.dhv.pbl5server.authentication_service.payload.response.AccountResponse;
import org.dhv.pbl5server.authentication_service.payload.response.CredentialResponse;
import org.dhv.pbl5server.authentication_service.repository.AccountRepository;
import org.dhv.pbl5server.authentication_service.service.AuthService;
import org.dhv.pbl5server.authentication_service.service.JwtService;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.exception.ForbiddenException;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.constant_service.entity.Constant;
import org.dhv.pbl5server.constant_service.enums.ConstantType;
import org.dhv.pbl5server.constant_service.enums.SystemRole;
import org.dhv.pbl5server.constant_service.repository.ConstantRepository;
import org.dhv.pbl5server.profile_service.entity.Company;
import org.dhv.pbl5server.profile_service.entity.User;
import org.dhv.pbl5server.profile_service.repository.CompanyRepository;
import org.dhv.pbl5server.profile_service.repository.UserRepository;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AccountRepository repository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AccountMapper mapper;
    private final ConstantRepository constantRepository;
    private final PasswordEncoder passwordEncoder;

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
    public AccountResponse getAccountById(String id) {
        return mapper.toAccountResponse(
            repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.ACCOUNT_NOT_FOUND))
        );
    }

    @Override
    public void logout(Account currentAccount) {
        currentAccount.setRefreshToken(null);
        repository.save(currentAccount);
        SecurityContextHolder.clearContext();
    }

    @Override
    public AccountResponse register(UserRegisterRequest request) {
        var role = checkValidSystemRole(request.getEmail(), request.getSystemRole().getConstantId());
        Account account = mapper.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setAccountStatus(true);
        account = repository.save(account);
        account.setSystemRole(role);
        // Creating user
        User user = User.builder()
            .accountId(account.getAccountId())
            .dateOfBirth(request.getDateOfBirth())
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .gender(request.getGender())
            .build();
        userRepository.save(user);
        return mapper.toAccountResponse(account);
    }

    @Override
    public AccountResponse register(CompanyRegisterRequest request) {
        var role = checkValidSystemRole(request.getEmail(), request.getSystemRole().getConstantId());
        Account account = mapper.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setAccountStatus(true);
        account = repository.save(account);
        account.setSystemRole(role);
        // Creating company
        Company company = Company.builder()
            .accountId(account.getAccountId())
            .companyName(request.getCompanyName())
            .companyUrl(request.getCompanyUrl())
            .establishedDate(request.getEstablishedDate())
            .build();
        companyRepository.save(company);
        return mapper.toAccountResponse(account);
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest request) {
        Account account = repository.findByEmail(request.getEmail())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.ACCOUNT_NOT_FOUND));
        return jwtService.generateTokenForResetPassword(account.getAccountId().toString());
    }

    @Override
    public void resetPassword(ResetPasswordRequest request, Account currentAccount) {
        if (!request.getNewPassword().equals(request.getNewPasswordConfirmation()))
            throw new BadRequestException(ErrorMessageConstant.NEW_PASSWORD_CONFIRMATION_NOT_MATCH);
        Account accountFromResetPwdToken = jwtService.getAccountFromResetPasswordToken(request.getResetPasswordToken());
        if (accountFromResetPwdToken.getAccountId().compareTo(currentAccount.getAccountId()) != 0)
            throw new BadRequestException(ErrorMessageConstant.INVALID_RESET_PASSWORD_TOKEN);
        accountFromResetPwdToken.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(accountFromResetPwdToken);
    }

    @Override
    public void changePassword(ChangePasswordRequest request, Account currentAccount) {
        if (request.getCurrentPassword().equals(request.getNewPassword()))
            throw new BadRequestException(ErrorMessageConstant.NEW_PASSWORD_SAME_OLD_PASSWORD);
        if (!request.getNewPassword().equals(request.getNewPasswordConfirmation()))
            throw new BadRequestException(ErrorMessageConstant.NEW_PASSWORD_CONFIRMATION_NOT_MATCH);
        if (!passwordEncoder.matches(request.getCurrentPassword(), currentAccount.getPassword()))
            throw new BadRequestException(ErrorMessageConstant.CURRENT_PASSWORD_IS_INCORRECT);
        currentAccount.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(currentAccount);
    }

    private Constant checkValidSystemRole(String email, String roleId) {
        var roleIdUUID = UUID.fromString(roleId);
        // Check if the email is already used
        if (repository.existsByEmail(email))
            throw new BadRequestException(ErrorMessageConstant.EMAIL_ALREADY_EXISTS);
        // Check constant's id is not null
        if (CommonUtils.isEmptyOrNullString(roleId))
            throw new BadRequestException(ErrorMessageConstant.SYSTEM_ROLE_NOT_FOUND);
        Constant role = constantRepository.findById(roleIdUUID).orElseThrow(() ->
            new NotFoundObjectException(ErrorMessageConstant.SYSTEM_ROLE_NOT_FOUND));
        // Check constant is not system role
        if (!ConstantType.isSystemRole(role.getConstantType()))
            throw new BadRequestException(ErrorMessageConstant.SYSTEM_ROLE_NOT_FOUND);
        // Check if the role is admin
        if (role.getConstantName().equalsIgnoreCase(SystemRole.ADMIN.name()))
            throw new BadRequestException(ErrorMessageConstant.ROLE_NOT_VALID);
        return role;
    }
}
