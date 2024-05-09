package org.dhv.pbl5server.notification_service.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;
import org.dhv.pbl5server.constant_service.enums.ConstantTypePrefix;

@Getter
@AllArgsConstructor
public enum NotificationType implements AbstractEnum<NotificationType> {
    TEST("00000", "Test"),
    // Match
    MATCHING("00001", "Matching"),
    REQUEST_MATCHING("00002", "Request Matching"),
    REJECT_MATCHING("00003", "Reject Matching"),
    ADMIN_CANCEL_MATCHING("00009", "Admin Cancel Matching"),
    // Chat
    NEW_CONVERSATION("00004", "New Conversation"),
    NEW_MESSAGE("00005", "New Message"),
    READ_MESSAGE("00006", "Read Message"),
    // Account
    ADMIN_DEACTIVATE_ACCOUNT("00007", "Admin Deactivate Account"),
    ADMIN_ACTIVATE_ACCOUNT("00008", "Admin Activate Account");

    private final String value;
    private final String notificationName;
    private final String enumName = this.name();

    public String constantType() {
        return "%s%s".formatted(ConstantTypePrefix.NOTIFICATIONS.getValue(), value);
    }
}
