package org.dhv.pbl5server.profile_service.repository;

import org.dhv.pbl5server.profile_service.entity.ApplicationPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// git commit -m "PBL-536 user profile"

public interface ApplicationPositionRepository extends JpaRepository<ApplicationPosition, UUID> {
    @Query("SELECT ap FROM ApplicationPosition ap LEFT JOIN FETCH ap.skills WHERE ap.id = :id")
    Optional<ApplicationPosition> fetchAllDataApplicationSkillById(UUID id);

    @Query("SELECT ap FROM ApplicationPosition ap WHERE ap.account.accountId = :accountId")
    List<ApplicationPosition> findAllByAccountId(UUID accountId);

    @Query("SELECT ap FROM ApplicationPosition ap WHERE ap.account.accountId = :accountId")
    Page<ApplicationPosition> findAllByAccountId(UUID accountId, Pageable pageable);

    @Query("SELECT ap FROM ApplicationPosition ap WHERE ap.account.accountId = :accountId and ap.id = :id")
    Optional<ApplicationPosition> findByIdAndAccountId(UUID accountId, UUID id);
}
