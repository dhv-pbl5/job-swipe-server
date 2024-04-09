package org.dhv.pbl5server.profile_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;

@Getter
@AllArgsConstructor
public enum UserProfileRequestType implements AbstractEnum<UserProfileRequestType> {
    BASIC_INFO("basic_info"),
    EDUCATION("education"),
    EXPERIENCE("experience"),
    AWARD("award"),
    OTHER_DESCRIPTION("other_description");

    private final String value;
    private final String enumName = this.name();
}
