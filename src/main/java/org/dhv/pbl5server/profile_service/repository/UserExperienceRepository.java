package org.dhv.pbl5server.profile_service.repository;

import org.dhv.pbl5server.profile_service.entity.UserExperience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

// git commit -m "PBL-536 user profile"
// git commit -m "PBL-559 user experience"
// git commit -m "PBL-557 update user experience"

public interface UserExperienceRepository extends JpaRepository<UserExperience, UUID> {
    @Query("SELECT ue FROM UserExperience ue WHERE ue.user.accountId = :userId")
    Page<UserExperience> findAllByUserId(UUID userId, Pageable pageable);
}
