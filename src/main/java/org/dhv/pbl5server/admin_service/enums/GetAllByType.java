package org.dhv.pbl5server.admin_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;

@Getter
@AllArgsConstructor
public enum GetAllByType implements AbstractEnum<GetAllByType> {
    CONSTANT("constant"),
    COMPANY("company"),
    USER("user");

    private final String value;
    private final String enumName = this.name();
}
