package org.dhv.pbl5server.notification_service.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;
import org.dhv.pbl5server.constant_service.enums.ConstantTypePrefix;

@Getter
@AllArgsConstructor
public enum NotificationType implements AbstractEnum<NotificationType> {
    TEST("001"),
    // Match
    MATCHING("002"),
    REJECT_MATCHING("003"),
    // Chat
    NEW_CONVERSATION("004"),
    NEW_MESSAGE("005"),
    READ_MESSAGE("006"),
    // Account
    ADMIN_DEACTIVATE_ACCOUNT("007"),
    ADMIN_ACTIVATE_ACCOUNT("008");

    private final String value;
    private final String enumName = this.name();

    public String constantType() {
        return "%s%s".formatted(ConstantTypePrefix.NOTIFICATION_TYPE, value);
    }
}
