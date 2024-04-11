package org.dhv.pbl5server.notification_service.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;
import org.dhv.pbl5server.constant_service.enums.ConstantTypePrefix;

@Getter
@AllArgsConstructor
public enum NotificationType implements AbstractEnum<NotificationType> {
    TEST("001", "Test"),
    // Match
    MATCHING("002", "Matching"),
    REQUEST_MATCHING("003", "Request Matching"),
    REJECT_MATCHING("004", "Reject Matching"),
    // Chat
    NEW_CONVERSATION("005", "New Conversation"),
    NEW_MESSAGE("006", "New Message"),
    READ_MESSAGE("007", "Read Message"),
    // Account
    ADMIN_DEACTIVATE_ACCOUNT("008", "Admin Deactivate Account"),
    ADMIN_ACTIVATE_ACCOUNT("009", "Admin Activate Account"),
    ;

    private final String value;
    private final String notificationName;
    private final String enumName = this.name();

    public String constantType() {
        return "%s%s".formatted(ConstantTypePrefix.NOTIFICATION_TYPE.getValue(), value);
    }
}
