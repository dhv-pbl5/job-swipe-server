package org.dhv.pbl5server.authentication_service.repository;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.applicationPositions WHERE a.accountId = :accountId")
    Optional<Account> fetchAllDataApplicationPositionByAccountId(UUID accountId);
}
