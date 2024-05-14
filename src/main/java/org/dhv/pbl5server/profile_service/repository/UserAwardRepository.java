package org.dhv.pbl5server.profile_service.repository;

import org.dhv.pbl5server.profile_service.entity.UserAward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

// git commit -m "PBL-536 user profile"

public interface UserAwardRepository extends JpaRepository<UserAward, UUID> {
    @Query("SELECT ua FROM UserAward ua WHERE ua.user.accountId = :userId")
    Page<UserAward> findAllByUserId(UUID userId, Pageable pageable);
}
