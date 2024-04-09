package org.dhv.pbl5server.constant_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;

@Getter
@AllArgsConstructor
public enum SystemRoleName implements AbstractEnum<SystemRoleName> {
    ADMIN("Admin"),
    USER("User"),
    COMPANY("Company");

    private final String value;
    private final String enumName = this.name();
}
