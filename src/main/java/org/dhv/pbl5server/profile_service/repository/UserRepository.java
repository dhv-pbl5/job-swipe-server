package org.dhv.pbl5server.profile_service.repository;

import org.dhv.pbl5server.profile_service.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

// git commit -m "PBL-514 register for user"

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.educations WHERE u.accountId = :accountId")
    Optional<User> fetchAllDataEducationById(UUID accountId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.experiences WHERE u.accountId = :accountId")
    Optional<User> fetchAllDataExperienceById(UUID accountId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.awards WHERE u.accountId = :accountId")
    Optional<User> fetchAllDataAwardById(UUID accountId);

    /*
     * Search user
     */
    @Query("SELECT u FROM User u WHERE CONCAT(u.lastName, ' ', u.firstName) LIKE %:name%")
    Page<User> searchByName(String name, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.account.email LIKE %:email%")
    Page<User> searchByEmail(String email, Pageable pageable);
}
