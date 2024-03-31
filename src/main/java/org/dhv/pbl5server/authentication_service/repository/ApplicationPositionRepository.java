package org.dhv.pbl5server.authentication_service.repository;

import org.dhv.pbl5server.authentication_service.entity.ApplicationPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ApplicationPositionRepository extends JpaRepository<ApplicationPosition, UUID> {
}
