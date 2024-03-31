package org.dhv.pbl5server.profile_service.repository;

import org.dhv.pbl5server.profile_service.entity.UserAward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserAwardRepository extends JpaRepository<UserAward, UUID> {
}
