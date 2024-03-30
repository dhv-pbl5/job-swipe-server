package org.dhv.pbl5server.constant_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dhv.pbl5server.common_service.utils.CommonUtils;

@Getter
@AllArgsConstructor
public enum ConstantType {

    ADMIN("01100"),
    USER("01110"),
    COMPANY("01120"),
    APPLY_POSITION("02000"),
    SKILL("03000"),
    EXPERIENCE_TYPE("04000"),
    NOTIFICATION_TYPE("05000");

    private final String value;

    public static boolean isSystemRole(String value) {
        if (CommonUtils.isEmptyOrNullString(value)) return false;
        return value.startsWith("01");
    }
}
