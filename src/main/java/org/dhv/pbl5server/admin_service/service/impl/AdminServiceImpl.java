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
import org.dhv.pbl5server.profile_service.service.CompanyService;
import org.dhv.pbl5server.profile_service.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AccountRepository repository;
    private final UserService userService;
    private final CompanyService companyService;
    private final AccountMapper mapper;

    @Override
    public ApiDataResponse getAllCompany(Pageable pageRequest) {
        return companyService.getAllData(pageRequest);
    }

    @Override
    public ApiDataResponse getAllUser(Pageable pageRequest) {
        return userService.getAllData(pageRequest);
    }

    @Override
    public AccountResponse activateAccount(String accountId) {
        Account account = repository.findById(UUID.fromString(accountId))
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.ACCOUNT_NOT_FOUND));
        if (account.getDeletedAt() == null)
            throw new BadRequestException(ErrorMessageConstant.ACCOUNT_IS_ACTIVE);
        account.setDeletedAt(null);
        account.setUpdatedAt(CommonUtils.getCurrentTimestamp());
        return mapper.toAccountResponse(repository.save(account));
    }

    @Override
    public AccountResponse deactivateAccount(String accountId) {
        Account account = repository.findById(UUID.fromString(accountId))
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.ACCOUNT_NOT_FOUND));
        if (account.getDeletedAt() != null)
            throw new BadRequestException(ErrorMessageConstant.ACCOUNT_IS_NOT_ACTIVE);
        account.setDeletedAt(CommonUtils.getCurrentTimestamp());
        account.setUpdatedAt(CommonUtils.getCurrentTimestamp());
        return mapper.toAccountResponse(repository.save(account));
    }
}
