package org.dhv.pbl5server.notification_service.controller;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.annotation.CurrentAccount;
import org.dhv.pbl5server.authentication_service.annotation.PreAuthorizeSystemRole;
import org.dhv.pbl5server.authentication_service.annotation.PreAuthorizeSystemRoleWithoutAdmin;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.PageUtils;
import org.dhv.pbl5server.notification_service.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService service;

    @PreAuthorizeSystemRole
    @PostMapping("/test/notify-to-all")
    public ResponseEntity<ApiDataResponse> testNotification() {
        service.notifyToAll("Test notification to all clients");
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    @PreAuthorizeSystemRoleWithoutAdmin
    @GetMapping("")
    public ResponseEntity<ApiDataResponse> getNotifications(
        @Nullable @RequestParam("notification_id") String notificationId,
        @Nullable @RequestParam("page") Integer page,
        @Nullable @RequestParam("paging") Integer paging,
        @CurrentAccount Account account
    ) {
        // Get notification by id
        if (CommonUtils.isNotEmptyOrNullString(notificationId))
            return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getNotificationById(account, notificationId)));
        // Get all notifications
        var pageRequest = PageUtils.makePageRequest("created_at", "desc", page, paging);
        return ResponseEntity.ok(service.getNotifications(account, pageRequest));
    }

    @PreAuthorizeSystemRoleWithoutAdmin
    @GetMapping("/unread-count")
    public ResponseEntity<ApiDataResponse> getUnreadCount(@CurrentAccount Account account) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getUnreadCount(account)));
    }

    @PreAuthorizeSystemRoleWithoutAdmin
    @PatchMapping("")
    public ResponseEntity<ApiDataResponse> markAsRead(
        @Nullable @RequestParam("notification_id") String notificationId,
        @Nullable @RequestParam("is_all") Boolean isAll,
        @CurrentAccount Account account
    ) {
        // Mark all notifications as read
        if (isAll != null && isAll) {
            service.markAllAsRead(account);
            return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
        }
        // Mark a notification as read
        if (!CommonUtils.isValidUuid(notificationId))
            throw new BadRequestException(ErrorMessageConstant.INVALID_UUID);
        service.markAsRead(account, notificationId);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }
}
