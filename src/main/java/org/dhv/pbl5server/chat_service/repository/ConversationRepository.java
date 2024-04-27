package org.dhv.pbl5server.chat_service.repository;

import org.dhv.pbl5server.chat_service.entity.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {
    @Query("SELECT c FROM Conversation c JOIN FETCH c.user WHERE c.user.accountId = :userId")
    Page<Conversation> findAllByUserId(UUID userId, Pageable pageable);

    @Query("SELECT c FROM Conversation c JOIN FETCH c.company WHERE c.company.accountId = :companyId")
    Page<Conversation> findAllByCompanyId(UUID companyId, Pageable pageable);

    @Query("SELECT c FROM Conversation c JOIN FETCH c.user WHERE c.id=:id and c.user.accountId = :userId")
    Optional<Conversation> findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT c FROM Conversation c JOIN FETCH c.company WHERE c.id=:id and c.company.accountId = :companyId")
    Optional<Conversation> findByIdAndCompanyId(UUID id, UUID companyId);

    @Query("SELECT c FROM Conversation c WHERE c.user.accountId=:userId and c.company.accountId = :companyId")
    Optional<Conversation> findByUserIdAndCompanyId(UUID userId, UUID companyId);
}
