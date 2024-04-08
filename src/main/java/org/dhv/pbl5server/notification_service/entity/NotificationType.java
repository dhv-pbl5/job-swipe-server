package org.dhv.pbl5server.notification_service.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    TEST(""),
    // Match
    MATCHING(""),
    REJECT_MATCHING(""),
    // Chat
    NEW_CONVERSATION(""),
    NEW_MESSAGE(""),
    READ_MESSAGE(""),
    // Account
    ADMIN_DEACTIVATE_ACCOUNT(""),
    ADMIN_ACTIVATE_ACCOUNT("");

    private final String value;
    private final String name = this.name();
}
