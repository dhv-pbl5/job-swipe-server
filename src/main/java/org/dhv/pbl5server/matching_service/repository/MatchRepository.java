package org.dhv.pbl5server.matching_service.repository;

import org.dhv.pbl5server.matching_service.entity.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// git commit -m "PBL-594 realtime matching for company"

public interface MatchRepository extends JpaRepository<Match, UUID> {

    @Query("SELECT m FROM Match m WHERE m.user.accountId = :accountId OR m.company.accountId = :accountId")
    Page<Match> findAllByAccountId(UUID accountId, Pageable pageRequest);

    // Accepted when both user and company have accepted the match
    @Query("SELECT m FROM Match m WHERE m.userMatched = TRUE AND m.companyMatched = TRUE AND (m.user.accountId = :accountId OR m.company.accountId = :accountId)")
    Page<Match> findAllAcceptedMatchesByAccountId(UUID accountId, Pageable pageRequest);

    // Rejected by user when user has rejected the match
    @Query("SELECT m FROM Match m WHERE m.userMatched = FALSE AND m.user.accountId = :userId")
    Page<Match> findAllRejectedMatchesByUserId(UUID userId, Pageable pageRequest);

    // Rejected by company when company has rejected the match
    @Query("SELECT m FROM Match m WHERE m.companyMatched = FALSE AND m.company.accountId = :companyId")
    Page<Match> findAllRejectedMatchesByCompanyId(UUID companyId, Pageable pageRequest);

    // Requested by user when company has requested but user has not accepted
    @Query("SELECT m FROM Match m WHERE " +
            "m.companyMatched = TRUE " +
            "AND (m.userMatched IS NULL OR m.userMatched = FALSE) " +
            "AND m.user.accountId = :userId")
    Page<Match> findAllRequestedMatchesByUserId(UUID userId, Pageable pageRequest);

    // Requested by company when user has requested but company has not accepted
    @Query("SELECT m FROM Match m WHERE " +
            "(m.companyMatched IS NULL OR m.companyMatched = FALSE) " +
            "AND m.userMatched = TRUE " +
            "AND m.company.accountId = :companyId")
    Page<Match> findAllRequestedMatchesByCompanyId(UUID companyId, Pageable pageRequest);

    @Query("SELECT m FROM Match m WHERE m.id = :id AND (m.user.accountId = :accountId OR m.company.accountId = :accountId)")
    Optional<Match> findByIdAndUserIdOrCompanyId(UUID id, UUID accountId);

    @Query("SELECT m FROM Match m WHERE m.user.accountId = :userId AND m.company.accountId = :companyId")
    List<Match> findByUserIdAndCompanyId(UUID userId, UUID companyId);
}
