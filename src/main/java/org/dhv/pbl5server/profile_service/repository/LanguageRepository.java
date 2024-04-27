package org.dhv.pbl5server.profile_service.repository;

import org.dhv.pbl5server.profile_service.entity.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LanguageRepository extends JpaRepository<Language, UUID> {

    @Query("SELECT l FROM Language l WHERE l.account.accountId = :accountId")
    List<Language> findAllByAccountId(UUID accountId);

    @Query("SELECT l FROM Language l WHERE l.account.accountId = :accountId")
    Page<Language> findAllByAccountId(UUID accountId, Pageable pageable);

    @Query("SELECT l FROM Language l WHERE l.id=:id AND l.account.accountId = :accountId")
    Optional<Language> findByIdAndAccountId(UUID id, UUID accountId);
}
