package org.dhv.pbl5server.authentication_service.repository;

import org.dhv.pbl5server.authentication_service.entity.ApplicationPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationPositionRepository extends JpaRepository<ApplicationPosition, UUID> {
    @Query("SELECT ap FROM ApplicationPosition ap LEFT JOIN FETCH ap.skills WHERE ap.id = :id")
    Optional<ApplicationPosition> fetchAllDataApplicationSkillById(UUID id);
}
