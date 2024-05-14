package org.dhv.pbl5server.notification_service.repository;

import org.dhv.pbl5server.notification_service.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// git commit -m "PBL-597 realtime conversation"

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    @Query("SELECT n FROM Notification n JOIN FETCH n.receiver WHERE n.receiver.accountId = :accountId")
    Page<Notification> findAllByReceiverId(UUID accountId, Pageable pageable);

    @Query("SELECT n FROM Notification n JOIN FETCH n.receiver WHERE n.receiver.accountId = :accountId and n.readStatus = :readStatus")
    List<Notification> findAllByReceiverIdAndReadStatus(UUID accountId, boolean readStatus);

    @Query("SELECT n FROM Notification n JOIN FETCH n.receiver WHERE n.receiver.accountId = :accountId and n.id= :notificationId")
    Optional<Notification> findByIdAndReceiverId(UUID notificationId, UUID accountId);

    @Query("SELECT n FROM Notification n JOIN FETCH n.sender WHERE n.sender.accountId = :accountId and n.id= :notificationId")
    Optional<Notification> findByIdAndSenderId(UUID notificationId, UUID accountId);
}
