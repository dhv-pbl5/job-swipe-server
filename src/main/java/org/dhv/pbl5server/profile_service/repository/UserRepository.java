package org.dhv.pbl5server.profile_service.repository;


import org.dhv.pbl5server.profile_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.educations WHERE u.accountId = :accountId")
    Optional<User> fetchAllDataEducationById(UUID accountId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.experiences WHERE u.accountId = :accountId")
    Optional<User> fetchAllDataExperienceById(UUID accountId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.awards WHERE u.accountId = :accountId")
    Optional<User> fetchAllDataAwardById(UUID accountId);

}
