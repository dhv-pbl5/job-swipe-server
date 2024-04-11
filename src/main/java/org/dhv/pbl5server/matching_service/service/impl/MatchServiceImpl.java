package org.dhv.pbl5server.matching_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.repository.AccountRepository;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.exception.InternalServerException;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.PageUtils;
import org.dhv.pbl5server.constant_service.enums.SystemRoleName;
import org.dhv.pbl5server.matching_service.entity.Match;
import org.dhv.pbl5server.matching_service.mapper.MatchMapper;
import org.dhv.pbl5server.matching_service.payload.MatchResponse;
import org.dhv.pbl5server.matching_service.repository.MatchRepository;
import org.dhv.pbl5server.matching_service.service.MatchService;
import org.dhv.pbl5server.notification_service.entity.NotificationType;
import org.dhv.pbl5server.notification_service.service.NotificationService;
import org.dhv.pbl5server.profile_service.entity.Company;
import org.dhv.pbl5server.profile_service.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
    private final MatchRepository repository;
    private final AccountRepository accountRepository;
    private final NotificationService notificationService;
    private final MatchMapper matchMapper;

    @Override
    public ApiDataResponse getMatches(Account account, Pageable pageRequest) {
        var page = repository.findAllByAccountId(account.getAccountId(), pageRequest);
        return ApiDataResponse.success(page
                .getContent()
                .stream()
                .map(matchMapper::toMatchResponse)
                .toList(),
            PageUtils.makePageInfo(page));
    }

    @Override
    public ApiDataResponse getAcceptedMatches(Account account, Pageable pageRequest) {
        var page = repository.findAllAcceptedMatchesByAccountId(account.getAccountId(), pageRequest);
        return ApiDataResponse.success(page
                .getContent()
                .stream()
                .map(matchMapper::toMatchResponse)
                .toList(),
            PageUtils.makePageInfo(page));
    }

    @Override
    public ApiDataResponse getRejectedMatches(Account account, Pageable pageRequest) {
        var role = getRole(account);
        var page = switch (role) {
            case USER -> repository.findAllRejectedMatchesByUserId(account.getAccountId(), pageRequest);
            case COMPANY -> repository.findAllRejectedMatchesByCompanyId(account.getAccountId(), pageRequest);
            default -> throw new BadRequestException(ErrorMessageConstant.MATCH_FEATURE_NOT_FOR_ADMIN);
        };
        return ApiDataResponse.success(page
                .getContent()
                .stream()
                .map(matchMapper::toMatchResponse)
                .toList(),
            PageUtils.makePageInfo(page));
    }

    @Override
    public ApiDataResponse getRequestedMatches(Account account, Pageable pageRequest) {
        var role = getRole(account);
        var page = switch (role) {
            case USER -> repository.findAllRequestedMatchesByUserId(account.getAccountId(), pageRequest);
            case COMPANY -> repository.findAllRequestedMatchesByCompanyId(account.getAccountId(), pageRequest);
            default -> throw new BadRequestException(ErrorMessageConstant.MATCH_FEATURE_NOT_FOR_ADMIN);
        };
        return ApiDataResponse.success(page
                .getContent()
                .stream()
                .map(matchMapper::toMatchResponse)
                .toList(),
            PageUtils.makePageInfo(page));
    }

    @Override
    public MatchResponse getMatchById(Account account, String matchingId) {
        var match = repository.findByIdAndUserIdOrCompanyId(UUID.fromString(matchingId), account.getAccountId())
            .orElseThrow(
                () -> new NotFoundObjectException(ErrorMessageConstant.MATCH_NOT_FOUND));
        return matchMapper.toMatchResponse(match);
    }

    @Override
    public MatchResponse requestMatch(Account account, String requestedAccountId) {
        UUID requestedAccountUuid = UUID.fromString(requestedAccountId);
        // Get requested account
        var requestedAccount = accountRepository.findById(requestedAccountUuid)
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.REQUESTED_ACCOUNT_NOT_FOUND));
        // Check if requested account have same role with current account
        if (account.getSystemRole().getConstantId() == requestedAccount.getSystemRole().getConstantId())
            throw new BadRequestException(ErrorMessageConstant.REQUESTED_ACCOUNT_SAME_ROLE);
        // Check if requested account is banned
        if (!requestedAccount.isEnabled())
            throw new BadRequestException(ErrorMessageConstant.REQUESTED_ACCOUNT_BANNED);
        // Get userId and companyId
        var role = getRole(account);
        UUID userId = null;
        UUID companyId = null;
        Boolean userMatched = null;
        Boolean companyMatched = null;
        if (role == SystemRoleName.USER) {
            userId = account.getAccountId();
            companyId = requestedAccountUuid;
            userMatched = true;
        } else if (role == SystemRoleName.COMPANY) {
            userId = requestedAccountUuid;
            companyId = account.getAccountId();
            companyMatched = true;
        } else throw new BadRequestException(ErrorMessageConstant.MATCH_FEATURE_NOT_FOR_ADMIN);
        // Check if match already exist --> accept match
        var existedMatch = repository.findByUserIdAndCompanyId(userId, companyId).orElse(null);
        if (existedMatch != null) return matchMapper.toMatchResponse(acceptMatch(account, existedMatch, true));
        // Create new match
        var match = repository.save(
            Match.builder()
                .matchedTime(CommonUtils.getCurrentTimestamp())
                .userMatched(userMatched)
                .companyMatched(companyMatched)
                .user(User.builder().accountId(userId).build())
                .company(Company.builder().accountId(companyId).build())
                .build());
        // Realtime notification
        notificationService.createNotification(
            match.getId(),
            account,
            requestedAccount,
            NotificationType.REQUEST_MATCHING
        );
        return matchMapper.toMatchResponse(repository.findById(match.getId()).orElseThrow());
    }

    @Override
    public MatchResponse acceptMatch(Account account, String matchId) {
        var match = repository.findByIdAndUserIdOrCompanyId(UUID.fromString(matchId), account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.MATCH_NOT_FOUND));
        return matchMapper.toMatchResponse(acceptMatch(account, match, false));
    }

    @Override
    public MatchResponse rejectMatch(Account account, String matchId) {
        var match = repository.findByIdAndUserIdOrCompanyId(UUID.fromString(matchId), account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.MATCH_NOT_FOUND));
        Account receiverAccount = null;
        var role = getRole(account);
        if (role == SystemRoleName.USER) {
            if (!match.isUserMatched()) throw new BadRequestException(ErrorMessageConstant.MATCH_ALREADY_REJECTED);
            match.setUserMatched(false);
            receiverAccount = match.getCompany().getAccount();
        } else if (role == SystemRoleName.COMPANY) {
            if (!match.isCompanyMatched()) throw new BadRequestException(ErrorMessageConstant.MATCH_ALREADY_REJECTED);
            match.setCompanyMatched(false);
            receiverAccount = match.getUser().getAccount();
        } else throw new BadRequestException(ErrorMessageConstant.MATCH_FEATURE_NOT_FOR_ADMIN);
        // Realtime
        notificationService.createNotification(
            match.getId(),
            account,
            receiverAccount,
            NotificationType.REJECT_MATCHING
        );
        repository.save(match);
        return matchMapper.toMatchResponse(match);
    }

    private Match acceptMatch(Account currentAccount, Match match, boolean isRequest) {
        // Check if match is already accepted
        if (match.isCompleted())
            throw new BadRequestException(ErrorMessageConstant.MATCH_ALREADY_ACCEPTED);
        // Check if match is invalid
        if (match.isInvalidMatch()) {
            repository.delete(match); // Delete invalid match
            throw new InternalServerException("Match's data is invalid: %s".formatted(CommonUtils.convertToJson(match)));
        }
        Account receiverAccount = null;
        var role = getRole(currentAccount);
        // Company accept user's request --> receiver is user
        if (match.isUserMatched() && role == SystemRoleName.COMPANY) {
            if (isRequest) throw new BadRequestException(ErrorMessageConstant.MATCH_ALREADY_REQUESTED);
            match.setCompanyMatched(true);
            receiverAccount = match.getUser().getAccount();
        }
        // User accept company's request --> receiver is company
        else if (match.isCompanyMatched() && role == SystemRoleName.USER) {
            if (isRequest) throw new BadRequestException(ErrorMessageConstant.MATCH_ALREADY_REQUESTED);
            match.setUserMatched(true);
            receiverAccount = match.getCompany().getAccount();
        }
        // Match accepted by yourself
        else throw new BadRequestException(ErrorMessageConstant.MATCH_NOT_ACCEPTED_YOURSELF);
        var result = repository.save(match);
        // Realtime
        notificationService.createNotification(
            match.getId(),
            currentAccount,
            receiverAccount,
            NotificationType.MATCHING
        );
        result.setUser(match.getUser());
        result.setCompany(match.getCompany());
        return result;
    }

    private SystemRoleName getRole(Account account) {
        return AbstractEnum.fromString(SystemRoleName.values(), account.getSystemRole().getConstantName());
    }
}
