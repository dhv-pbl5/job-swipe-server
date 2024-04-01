package org.dhv.pbl5server.profile_service.repository;

import org.dhv.pbl5server.profile_service.entity.ApplicationSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationSkillRepository extends JpaRepository<ApplicationSkill, UUID> {
    @Query("SELECT aps FROM ApplicationSkill aps WHERE aps.applicationPosition.id = :apId")
    List<ApplicationSkill> findAllByApplicationPositionId(UUID apId);

    @Query("SELECT aps FROM ApplicationSkill aps WHERE aps.applicationPosition.id = :apId and aps.id = :apsId")
    Optional<ApplicationSkill> findByIdAndApplicationPositionId(UUID apId, UUID apsId);
}
