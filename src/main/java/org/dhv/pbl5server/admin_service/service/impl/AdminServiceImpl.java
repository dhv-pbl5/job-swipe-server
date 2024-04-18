package org.dhv.pbl5server.admin_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.admin_service.service.AdminService;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.mapper.AccountMapper;
import org.dhv.pbl5server.authentication_service.payload.response.AccountResponse;
import org.dhv.pbl5server.authentication_service.repository.AccountRepository;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.constant_service.entity.Constant;
import org.dhv.pbl5server.constant_service.enums.SystemRoleName;
import org.dhv.pbl5server.constant_service.service.ConstantService;
import org.dhv.pbl5server.profile_service.service.ApplicationPositionService;
import org.dhv.pbl5server.profile_service.service.CompanyService;
import org.dhv.pbl5server.profile_service.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AccountRepository repository;
    private final ConstantService constantService;
    private final UserService userService;
    private final CompanyService companyService;
    private final ApplicationPositionService applicationPositionService;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiDataResponse getAllCompany(Pageable pageRequest) {
        return companyService.getAllData(pageRequest);
    }

    @Override
    public ApiDataResponse getAllUser(Pageable pageRequest) {
        return userService.getAllData(pageRequest);
    }

    @Override
    public ApiDataResponse getAllApplicationPosition(String accountId, Pageable pageRequest) {
        return applicationPositionService.getApplicationPositions(accountId, pageRequest);
    }

    @Override
    public ApiDataResponse getAllUserAward(String userId, Pageable pageRequest) {
        return userService.getListAwardByUserId(userId, pageRequest);
    }

    @Override
    public ApiDataResponse getAllUserEducation(String userId, Pageable pageRequest) {
        return userService.getListEducationByUserId(userId, pageRequest);
    }

    @Override
    public ApiDataResponse getAllUserExperience(String userId, Pageable pageRequest) {
        return userService.getListExperienceByUserId(userId, pageRequest);
    }

    @Override
    public AccountResponse activateAccount(String accountId) {
        Account account = repository.findById(UUID.fromString(accountId))
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.ACCOUNT_NOT_FOUND));
        if (account.getDeletedAt() == null)
            throw new BadRequestException(ErrorMessageConstant.ACCOUNT_IS_ACTIVE);
        account.setDeletedAt(null);
        account.setUpdatedAt(CommonUtils.getCurrentTimestamp());
        return accountMapper.toAccountResponse(repository.save(account));
    }

    @Override
    public AccountResponse deactivateAccount(String accountId) {
        Account account = repository.findById(UUID.fromString(accountId))
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.ACCOUNT_NOT_FOUND));
        if (account.getDeletedAt() != null)
            throw new BadRequestException(ErrorMessageConstant.ACCOUNT_IS_NOT_ACTIVE);
        account.setDeletedAt(CommonUtils.getCurrentTimestamp());
        account.setUpdatedAt(CommonUtils.getCurrentTimestamp());
        return accountMapper.toAccountResponse(repository.save(account));
    }

    @Override
    @SuppressWarnings("unchecked")
    public AccountResponse initialAdminAccount() {
        var roles = (List<Constant>) constantService.getSystemRoles(null);
        for (var role : roles) {
            if (role.getConstantName().equalsIgnoreCase(SystemRoleName.ADMIN.getValue())) {
                var admin = Account.builder()
                    .address("Da Nang")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("123456Aa"))
                    .systemRole(Constant.builder().constantId(role.getConstantId()).build())
                    .phoneNumber("0348219257")
                    .build();
                repository.save(admin);
                return accountMapper.toAccountResponse(admin);
            }
        }
        return null;
    }
}
