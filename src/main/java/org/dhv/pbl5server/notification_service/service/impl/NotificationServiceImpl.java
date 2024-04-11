package org.dhv.pbl5server.notification_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.utils.PageUtils;
import org.dhv.pbl5server.constant_service.entity.Constant;
import org.dhv.pbl5server.constant_service.service.ConstantService;
import org.dhv.pbl5server.notification_service.entity.Notification;
import org.dhv.pbl5server.notification_service.entity.NotificationType;
import org.dhv.pbl5server.notification_service.mapper.NotificationMapper;
import org.dhv.pbl5server.notification_service.payload.NotificationResponse;
import org.dhv.pbl5server.notification_service.repository.NotificationRepository;
import org.dhv.pbl5server.notification_service.service.NotificationService;
import org.dhv.pbl5server.realtime_service.service.RealtimeService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final RealtimeService realtimeService;
    private final ConstantService constantService;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public void notifyToAll(String message) {
        realtimeService.sendToAllClient(NotificationType.TEST, message);
    }

    @Override
    public ApiDataResponse getNotifications(Account account, Pageable pageRequest) {
        var page = notificationRepository.findAllByReceiverId(account.getAccountId(), pageRequest);
        return ApiDataResponse.success(page
                .getContent()
                .stream()
                .map(notificationMapper::toNotificationResponse)
                .toList(),
            PageUtils.makePageInfo(page));
    }

    @Override
    public NotificationResponse getNotificationById(Account account, String notificationId) {
        var notification = notificationRepository.findByIdAndReceiverId(UUID.fromString(notificationId), account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.NOTIFICATION_NOT_FOUND));
        return notificationMapper.toNotificationResponse(notification);
    }

    @Override
    public void markAsRead(Account account, String notificationId) {
        var notification = notificationRepository.findByIdAndReceiverId(UUID.fromString(notificationId), account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.NOTIFICATION_NOT_FOUND));
        if (notification.isReadStatus()) return;
        notification.setReadStatus(true);
        notificationRepository.save(notification);
    }

    @Override
    public void markAllAsRead(Account account) {
        var unreadNotifications = notificationRepository.findAllByReceiverIdAndReadStatus(account.getAccountId(), false)
            .stream()
            .peek(e -> e.setReadStatus(true))
            .toList();
        notificationRepository.saveAll(unreadNotifications);
    }

    @Override
    public int getUnreadCount(Account account) {
        return notificationRepository.findAllByReceiverIdAndReadStatus(account.getAccountId(), false).size();
    }

    @Override
    public void createNotification(UUID objectId, Account sender, Account receiver, NotificationType type) {
        if (sender == null || receiver == null)
            throw new BadRequestException(ErrorMessageConstant.REQUIRED_SENDER_AND_RECEIVER);
        if (type == null)
            throw new BadRequestException(ErrorMessageConstant.REQUIRED_NOTIFICATION_TYPE);
        var constant = constantService.getConstantByType(type.constantType());
        var notification = Notification.builder()
            .sender(sender)
            .receiver(receiver)
            .objectId(objectId)
            .readStatus(false)
            .content(constant.getConstantName())
            .type(Constant.builder().constantId(constant.getConstantId()).build())
            .build();
        var response = notificationMapper.toNotificationResponse(notificationRepository.save(notification));
        // Realtime: send notification to receiver
        realtimeService.sendToClientWithPrefix(
            receiver.getAccountId().toString(),
            type,
            response);
    }
}
