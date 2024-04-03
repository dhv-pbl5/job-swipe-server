package org.dhv.pbl5server.profile_service.repository;

import org.dhv.pbl5server.profile_service.entity.UserExperience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserExperienceRepository extends JpaRepository<UserExperience, UUID> {
    @Query("SELECT ue FROM UserExperience ue WHERE ue.user.accountId = :userId")
    Page<UserExperience> findAllByUserId(UUID userId, Pageable pageable);
}
