package org.dhv.pbl5server.matching_service.service;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.matching_service.payload.MatchResponse;
import org.springframework.data.domain.Pageable;

public interface MatchService {

    ApiDataResponse getMatches(Account account, Pageable pageRequest);

    MatchResponse getMatchById(Account account, String matchingId);

    MatchResponse requestMatch(Account account, String requestedAccountId);

    MatchResponse acceptMatch(Account account, String matchId);

    MatchResponse rejectMatch(Account account, String matchId);
}
