package org.dhv.pbl5server.authentication_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.mapper.AccountMapper;
import org.dhv.pbl5server.authentication_service.payload.request.*;
import org.dhv.pbl5server.authentication_service.payload.response.AccountResponse;
import org.dhv.pbl5server.authentication_service.payload.response.CredentialResponse;
import org.dhv.pbl5server.authentication_service.repository.AccountRepository;
import org.dhv.pbl5server.authentication_service.service.AuthService;
import org.dhv.pbl5server.authentication_service.service.JwtService;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.constant.RedisCacheConstant;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.exception.ForbiddenException;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.common_service.repository.RedisRepository;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.LogUtils;
import org.dhv.pbl5server.constant_service.entity.Constant;
import org.dhv.pbl5server.constant_service.enums.ConstantTypePrefix;
import org.dhv.pbl5server.constant_service.enums.SystemRoleName;
import org.dhv.pbl5server.constant_service.repository.ConstantRepository;
import org.dhv.pbl5server.mail_service.service.MailService;
import org.dhv.pbl5server.profile_service.entity.Company;
import org.dhv.pbl5server.profile_service.entity.User;
import org.dhv.pbl5server.profile_service.repository.CompanyRepository;
import org.dhv.pbl5server.profile_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    @Value("${application.reset-password-code-expiration-ms}")
    private Long resetPasswordTokenExpirationTime;
    private final RedisRepository redisRepository;
    private final AccountRepository repository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AccountMapper mapper;
    private final ConstantRepository constantRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public CredentialResponse login(LoginRequest loginRequest, boolean isAdmin) {
        try {
            Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Account account = (Account) authentication.getPrincipal();
            // Check if the user is an admin
            var role = AbstractEnum.fromString(SystemRoleName.values(), account.getSystemRole().getConstantName());
            if (isAdmin && role != SystemRoleName.ADMIN) {
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
            LogUtils.error("LOGIN", ex);
            throw ex;
        }
    }

    @Override
    public CredentialResponse refreshToken(RefreshTokenRequest refreshTokenRequest, boolean isAdmin) {
        try {
            return jwtService.refreshToken(refreshTokenRequest.getRefreshToken(), isAdmin);
        } catch (Exception ex) {
            LogUtils.error("REFRESH TOKEN", ex);

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
        redisRepository.save(
            RedisCacheConstant.AUTH_KEY,
            RedisCacheConstant.REVOKE_ACCESS_TOKEN_HASH(currentAccount.getAccountId().toString(), true),
            SecurityContextHolder.getContext().getAuthentication().getCredentials()
        );
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
    public void forgotPassword(ForgotPasswordRequest request) {
        Account account = repository.findByEmail(request.getEmail())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.ACCOUNT_NOT_FOUND));
        // Check if the account is disabled or admin
        var role = AbstractEnum.fromString(SystemRoleName.values(), account.getSystemRole().getConstantName());
        if (role == SystemRoleName.ADMIN || !account.isEnabled())
            throw new ForbiddenException(ErrorMessageConstant.FORBIDDEN);
        // Generate reset password code
        String resetPasswordCode = CommonUtils.generate6DigitsOTP();
        Map<String, Object> resetPasswordCodeMap = Map.of(
            "reset_password_code", resetPasswordCode,
            "expiration_time", System.currentTimeMillis() + resetPasswordTokenExpirationTime
        );
        // Save reset password code to redis
        redisRepository.save(
            RedisCacheConstant.OTP_KEY,
            RedisCacheConstant.FORGOT_PASSWORD_HASH(account.getAccountId().toString()),
            resetPasswordCodeMap
        );
        // Send email to user
        mailService.sendForgotPasswordEmail(account.getEmail(), resetPasswordCode);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        // Check email
        Account account = repository.findByEmail(request.getEmail())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.ACCOUNT_NOT_FOUND));
        // Check if the account is disabled or admin
        var role = AbstractEnum.fromString(SystemRoleName.values(), account.getSystemRole().getConstantName());
        if (role == SystemRoleName.ADMIN || !account.isEnabled())
            throw new ForbiddenException(ErrorMessageConstant.FORBIDDEN);
        // Check reset password code
        var dataInRedis = redisRepository.findByHashKey(
            RedisCacheConstant.OTP_KEY,
            RedisCacheConstant.FORGOT_PASSWORD_HASH(account.getAccountId().toString()));
        var object = dataInRedis != null ? CommonUtils.decodeJson(dataInRedis.toString(), Map.class) : null;
        if (object == null || !request.getResetPasswordCode().equals(object.get("reset_password_code")))
            throw new BadRequestException(ErrorMessageConstant.RESET_PASSWORD_CODE_INVALID);
        if (System.currentTimeMillis() > (long) object.get("expiration_time"))
            throw new BadRequestException(ErrorMessageConstant.RESET_PASSWORD_CODE_EXPIRED);
        // Check new password
        if (!request.getNewPassword().equals(request.getNewPasswordConfirmation()))
            throw new BadRequestException(ErrorMessageConstant.NEW_PASSWORD_CONFIRMATION_NOT_MATCH);
        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(account);
        // Remove reset password code from redis
        redisRepository.delete(
            RedisCacheConstant.OTP_KEY,
            RedisCacheConstant.FORGOT_PASSWORD_HASH(account.getAccountId().toString())
        );
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
        if (!ConstantTypePrefix.isSystemRole(role.getConstantType()))
            throw new BadRequestException(ErrorMessageConstant.SYSTEM_ROLE_NOT_FOUND);
        // Check if the role is admin
        var roleEnum = AbstractEnum.fromString(SystemRoleName.values(), role.getConstantName());
        if (roleEnum == SystemRoleName.ADMIN)
            throw new BadRequestException(ErrorMessageConstant.ROLE_NOT_VALID);
        return role;
    }
}
