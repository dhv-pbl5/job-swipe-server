package org.dhv.pbl5server.matching_service.service;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.matching_service.payload.MatchResponse;
import org.springframework.data.domain.Pageable;

// git commit -m "PBL-593 realtime matching for user"
// git commit -m "PBL-594 realtime matching for company"
// git commit -m "PBL-584 request matching for user"

public interface MatchService {
    ApiDataResponse getMatches(String accountId, Pageable pageRequest);

    ApiDataResponse getMatches(Account account, Pageable pageRequest);

    ApiDataResponse getAcceptedMatches(Account account, Pageable pageRequest);

    ApiDataResponse getRejectedMatches(Account account, Pageable pageRequest);

    ApiDataResponse getRequestedMatches(Account account, Pageable pageRequest);

    MatchResponse getMatchById(Account account, String matchingId);

    MatchResponse requestMatch(Account account, String requestedAccountId);

    MatchResponse acceptMatch(Account account, String matchId);

    MatchResponse rejectMatch(Account account, String matchId);

    MatchResponse cancelMatch(Account account, String matchId);
}
