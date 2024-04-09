package org.dhv.pbl5server.admin_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;

@Getter
@AllArgsConstructor
public enum GetAllByType implements AbstractEnum<GetAllByType> {
    COMPANY("company"),
    USER("user"),
    APPLICATION_POSITION("application_position"),
    USER_AWARD("user_award"),
    USER_EDUCATION("user_education"),
    USER_EXPERIENCE("user_experience");

    private final String value;
    private final String enumName = this.name();
}
