package org.dhv.pbl5server.constant_service.enums;

import lombok.Getter;

@Getter
public enum ConstantType {
    SYSTEM_ROLE(1),
    WORK_POSITION(2),
    NOTIFICATION_TYPE(3),
    GENDER(4),
    EXPERIENCE_TYPE(5);

    private final int index;

    ConstantType(int index) {
        this.index = index;
    }
}
