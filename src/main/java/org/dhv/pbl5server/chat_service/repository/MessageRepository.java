package org.dhv.pbl5server.chat_service.repository;

import org.dhv.pbl5server.chat_service.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    @Query("SELECT m FROM Message m WHERE m.account.accountId = :accountId")
    Page<Message> findAllByAccountId(UUID accountId, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.account.accountId = :accountId and m.conversation.id = :conversationId")
    Page<Message> findAllByAccountIdAndConversationId(UUID accountId, UUID conversationId, Pageable pageable);

    Optional<Message> findFirstByConversationId(UUID conversationId, Sort sort);

    Optional<Message> findByIdAndAccount_AccountIdAndConversationId(UUID messageId, UUID accountId, UUID conversationId);

    List<Message> findAllByAccount_AccountIdAndConversationIdAndReadStatus(UUID accountId, UUID conversationId, boolean readStatus);
}
