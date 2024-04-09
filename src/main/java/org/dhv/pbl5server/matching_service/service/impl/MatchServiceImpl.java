package org.dhv.pbl5server.matching_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.matching_service.mapper.MatchMapper;
import org.dhv.pbl5server.matching_service.payload.MatchResponse;
import org.dhv.pbl5server.matching_service.repository.MatchRepository;
import org.dhv.pbl5server.matching_service.service.MatchService;
import org.dhv.pbl5server.realtime_service.service.RealtimeService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
    private final MatchRepository matchRepository;
    private final RealtimeService realtimeService;
    private final MatchMapper matchMapper;

    @Override
    public ApiDataResponse getMatches(Account account, Pageable pageRequest) {
        return null;
    }

    @Override
    public MatchResponse getMatchById(Account account, String matchingId) {
        var match = matchRepository.findByIdAndUserIdOrCompanyId(UUID.fromString(matchingId), account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.MATCH_NOT_FOUND));
        return matchMapper.toMatchResponse(match);
    }

    @Override
    public MatchResponse requestMatch(Account account, String requestedAccountId) {
        return null;
    }

    @Override
    public MatchResponse acceptMatch(Account account, String matchId) {
        return null;
    }

    @Override
    public MatchResponse rejectMatch(Account account, String matchId) {
        return null;
    }


}
