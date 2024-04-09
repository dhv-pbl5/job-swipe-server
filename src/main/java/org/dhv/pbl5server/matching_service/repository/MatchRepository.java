package org.dhv.pbl5server.matching_service.repository;

import org.dhv.pbl5server.matching_service.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface MatchRepository extends JpaRepository<Match, UUID> {

    @Query("SELECT m FROM Match m WHERE m.id = :id AND (m.user.accountId = :accountId OR m.company.accountId = :accountId)")
    Optional<Match> findByIdAndUserIdOrCompanyId(UUID id, UUID accountId);
}
