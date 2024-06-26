package org.dhv.pbl5server.matching_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.repository.AccountRepository;
import org.dhv.pbl5server.chat_service.service.ChatService;
import org.dhv.pbl5server.common_service.constant.CommonConstant;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.constant.RedisCacheConstant;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.exception.ForbiddenException;
import org.dhv.pbl5server.common_service.exception.InternalServerException;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.repository.RedisRepository;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.PageUtils;
import org.dhv.pbl5server.constant_service.enums.ConstantTypePrefix;
import org.dhv.pbl5server.constant_service.enums.SystemRoleName;
import org.dhv.pbl5server.constant_service.service.ConstantService;
import org.dhv.pbl5server.mail_service.service.MailService;
import org.dhv.pbl5server.matching_service.entity.Match;
import org.dhv.pbl5server.matching_service.mapper.MatchMapper;
import org.dhv.pbl5server.matching_service.payload.InterviewInvitationRequest;
import org.dhv.pbl5server.matching_service.payload.MatchResponse;
import org.dhv.pbl5server.matching_service.repository.MatchRepository;
import org.dhv.pbl5server.matching_service.service.MatchService;
import org.dhv.pbl5server.notification_service.entity.NotificationType;
import org.dhv.pbl5server.notification_service.service.NotificationService;
import org.dhv.pbl5server.profile_service.entity.Company;
import org.dhv.pbl5server.profile_service.entity.User;
import org.dhv.pbl5server.realtime_service.service.RealtimeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
    @Value("${application.max-sent-interview-invitation-mail-per-day-and-user}")
    private Integer maxMailCount;
    private final MatchRepository repository;
    private final AccountRepository accountRepository;
    private final NotificationService notificationService;
    private final ChatService chatService;
    private final RealtimeService realtimeService;
    private final MatchMapper matchMapper;
    private final MailService mailService;
    private final RedisRepository redisRepository;
    private final ConstantService constantService;

    @Override
    public ApiDataResponse getMatches(String accountId, Pageable pageRequest) {
        var page = repository.findAllByAccountId(UUID.fromString(accountId), pageRequest);
        return ApiDataResponse.success(page
                .getContent()
                .stream()
                .map(matchMapper::toMatchResponse)
                .toList(),
            PageUtils.makePageInfo(page));
    }

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
    public MatchResponse getMatchByAccountId(Account account, String accountId) {
        var role = AbstractEnum.fromString(SystemRoleName.values(), account.getSystemRole().getConstantName());
        var match = switch (role) {
            case USER -> repository.findByUserIdAndCompanyId(account.getAccountId(), UUID.fromString(accountId))
                .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.MATCH_NOT_FOUND));
            case COMPANY -> repository.findByUserIdAndCompanyId(UUID.fromString(accountId), account.getAccountId())
                .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.MATCH_NOT_FOUND));
            default -> null;
        };
        if (match == null) throw new NotFoundObjectException(ErrorMessageConstant.MATCH_NOT_FOUND);
        return matchMapper.toMatchResponse(match);
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
        if (existedMatch != null) {
            if (existedMatch.isCompleted())
                throw new BadRequestException(ErrorMessageConstant.MATCH_ALREADY_ACCEPTED);
            return matchMapper.toMatchResponse(acceptMatch(account, existedMatch, true));
        }
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
            if (!match.isUserMatchedNull() && !match.isUserMatched())
                throw new BadRequestException(ErrorMessageConstant.MATCH_ALREADY_REJECTED);
            match.setUserMatched(false);
            receiverAccount = match.getCompany().getAccount();
        } else if (role == SystemRoleName.COMPANY) {
            if (!match.isCompanyMatchedNull() && !match.isCompanyMatched())
                throw new BadRequestException(ErrorMessageConstant.MATCH_ALREADY_REJECTED);
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
        // Disable conversation
        chatService.changeConversationStatus(match.getUser(), match.getCompany(), false);
        return matchMapper.toMatchResponse(match);
    }

    @Override
    public MatchResponse cancelMatch(Account account, String matchId) {
        var role = AbstractEnum.fromString(SystemRoleName.values(), account.getSystemRole().getConstantName());
        if (role != SystemRoleName.ADMIN)
            throw new ForbiddenException(ErrorMessageConstant.FORBIDDEN);
        var match = repository.findById(UUID.fromString(matchId))
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.MATCH_NOT_FOUND));
        if (!match.isCompanyMatched() && !match.isUserMatched()) {
            throw new BadRequestException(ErrorMessageConstant.MATCH_ALREADY_CANCELLED);
        }
        match.setUserMatched(false);
        match.setCompanyMatched(false);
        repository.save(match);
        var response = matchMapper.toMatchResponse(match);
        // Realtime
        realtimeService.sendToClientWithPrefix(
            response.getUser().getAccountId().toString(),
            NotificationType.ADMIN_CANCEL_MATCHING,
            response
        );
        realtimeService.sendToClientWithPrefix(
            response.getCompany().getAccountId().toString(),
            NotificationType.ADMIN_CANCEL_MATCHING,
            response
        );
        return response;
    }

    @Override
    public void sendInterviewInvitation(Account account, InterviewInvitationRequest request) {
        var match = repository.findById(UUID.fromString(request.getMatchingId()))
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.MATCH_NOT_FOUND));
        var user = match.getUser();
        var company = match.getCompany();
        // Check interview position must be application position
        constantService.checkConstantWithType(UUID.fromString(request.getInterviewPositionId()), ConstantTypePrefix.APPLY_POSITIONS);
        var interviewPosition = constantService.getConstantById(UUID.fromString(request.getInterviewPositionId()));
        // Check if match is not accepted
        if (!match.isCompleted())
            throw new BadRequestException(ErrorMessageConstant.MATCH_NOT_ACCEPTED);
        // Check if interview time is invalid
        if (request.getInterviewTime().before(CommonUtils.getCurrentTimestamp()))
            throw new BadRequestException(ErrorMessageConstant.INTERVIEW_TIME_INVALID);
        // Check email count exceed per day
        int count = 0;
        var dataInRedis = redisRepository.findByHashKey(
            RedisCacheConstant.MAIL_KEY,
            RedisCacheConstant.INTERVIEW_INVITATION_MAIL_HASH(company.getAccountId().toString(), user.getAccountId().toString()));
        var object = dataInRedis != null ? CommonUtils.decodeJson(dataInRedis.toString(), Map.class) : null;
        if (object != null) {
            count = (Integer) object.get("count");
            var time = (Long) object.get("time");
            if (System.currentTimeMillis() - time >= CommonConstant.DAY_IN_MILLIS) {
                count = 0;
            } else if (count >= maxMailCount) {
                throw new BadRequestException(ErrorMessageConstant.SENT_INTERVIEW_INVITATION_MAIL_EXCEED);
            }
        }
        // Save count to redis
        redisRepository.save(
            RedisCacheConstant.MAIL_KEY,
            RedisCacheConstant.INTERVIEW_INVITATION_MAIL_HASH(company.getAccountId().toString(), user.getAccountId().toString()),
            Map.of(
                "count", ++count,
                "time", System.currentTimeMillis()
            )
        );
        // Send email
        mailService.sendInterviewInvitationEmail(user, company, request.getInterviewTime(), interviewPosition.getConstantName());
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
        // Create conversation if not exist else enable conversation
        if (!chatService.isConversationExist(match.getUser().getAccountId(), match.getCompany().getAccountId()))
            chatService.createConversation(match.getUser(), match.getCompany());
        else chatService.changeConversationStatus(match.getUser(), match.getCompany(), true);
        result.setUser(match.getUser());
        result.setCompany(match.getCompany());
        return result;
    }

    private SystemRoleName getRole(Account account) {
        return AbstractEnum.fromString(SystemRoleName.values(), account.getSystemRole().getConstantName());
    }
}
