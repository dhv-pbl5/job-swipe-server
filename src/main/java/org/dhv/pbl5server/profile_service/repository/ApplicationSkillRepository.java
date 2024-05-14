package org.dhv.pbl5server.profile_service.repository;

import org.dhv.pbl5server.profile_service.entity.ApplicationSkill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// git commit -m "PBL-536 user profile"
// git commit -m "PBL-538 company profile"
// git commit -m "PBL-526 position and skill"
// git commit -m "PBL-534 application position"
// git commit -m "PBL-528 delete application position"
// git commit -m "PBL-530 update application position"

public interface ApplicationSkillRepository extends JpaRepository<ApplicationSkill, UUID> {
    @Query("SELECT aps FROM ApplicationSkill aps WHERE aps.applicationPosition.id = :apId")
    List<ApplicationSkill> findAllByApplicationPositionId(UUID apId);

    @Query("SELECT aps FROM ApplicationSkill aps WHERE aps.applicationPosition.id = :apId")
    Page<ApplicationSkill> findAllByApplicationPositionId(UUID apId, Pageable pageable);

    @Query("SELECT aps FROM ApplicationSkill aps WHERE aps.applicationPosition.id = :apId and aps.id = :apsId")
    Optional<ApplicationSkill> findByIdAndApplicationPositionId(UUID apId, UUID apsId);
}
