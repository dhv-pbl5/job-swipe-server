package org.dhv.pbl5server.profile_service.repository;

import org.dhv.pbl5server.profile_service.entity.UserEducation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

// git commit -m "PBL-536 user profile"
// git commit -m "PBL-565 user education"

public interface UserEducationRepository extends JpaRepository<UserEducation, UUID> {
    @Query("SELECT ue FROM UserEducation ue WHERE ue.user.accountId = :userId")
    Page<UserEducation> findAllByUserId(UUID userId, Pageable pageable);
}
