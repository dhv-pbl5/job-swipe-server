package org.dhv.pbl5server.authentication_service.repository;

import org.dhv.pbl5server.authentication_service.entity.ApplicationSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ApplicationSkillRepository extends JpaRepository<ApplicationSkill, UUID> {
}
