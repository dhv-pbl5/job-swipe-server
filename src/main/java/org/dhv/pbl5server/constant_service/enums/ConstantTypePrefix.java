package org.dhv.pbl5server.constant_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;
import org.dhv.pbl5server.common_service.utils.CommonUtils;

@Getter
@AllArgsConstructor
public enum ConstantTypePrefix implements AbstractEnum<ConstantTypePrefix> {
    SYSTEM_ROLE("01"),
    APPLY_POSITION("02"),
    SKILL("03"),
    EXPERIENCE_TYPE("04"),
    NOTIFICATION_TYPE("05"),
    LANGUAGE("06");

    private final String value;
    private final String enumName = this.name();

    public static boolean isSystemRole(String value) {
        if (CommonUtils.isEmptyOrNullString(value)) return false;
        return value.startsWith("01");
    }
}
