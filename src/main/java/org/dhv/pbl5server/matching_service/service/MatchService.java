package org.dhv.pbl5server.matching_service.service;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.matching_service.payload.InterviewInvitationRequest;
import org.dhv.pbl5server.matching_service.payload.MatchResponse;
import org.springframework.data.domain.Pageable;

public interface MatchService {
    ApiDataResponse getMatches(String accountId, Pageable pageRequest);

    ApiDataResponse getMatches(Account account, Pageable pageRequest);

    ApiDataResponse getAcceptedMatches(Account account, Pageable pageRequest);

    ApiDataResponse getRejectedMatches(Account account, Pageable pageRequest);

    ApiDataResponse getRequestedMatches(Account account, Pageable pageRequest);

    MatchResponse getMatchByAccountId(Account account, String accountId);

    MatchResponse getMatchById(Account account, String matchingId);

    MatchResponse requestMatch(Account account, String requestedAccountId);

    MatchResponse acceptMatch(Account account, String matchId);

    MatchResponse rejectMatch(Account account, String matchId);

    MatchResponse cancelMatch(Account account, String matchId);

    void sendInterviewInvitation(Account account, InterviewInvitationRequest request);
}
