package org.dhv.pbl5server.constant_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;
import org.dhv.pbl5server.common_service.utils.CommonUtils;

@Getter
@AllArgsConstructor
public enum ConstantTypePrefix implements AbstractEnum<ConstantTypePrefix> {
    SYSTEM_ROLES("01"),
    APPLY_POSITIONS("02"),
    SKILLS("03"),
    EXPERIENCE_TYPES("04"),
    NOTIFICATIONS("05"),
    LANGUAGES("06"),
    SALARY_RANGES("07");

    private final String value;
    private final String enumName = this.name();

    public static boolean isSystemRole(String value) {
        if (CommonUtils.isEmptyOrNullString(value)) return false;
        return value.startsWith("01");
    }
}
