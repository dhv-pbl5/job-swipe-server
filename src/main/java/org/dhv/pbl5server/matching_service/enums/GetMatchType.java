package org.dhv.pbl5server.matching_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;

@Getter
@AllArgsConstructor
public enum GetMatchType implements AbstractEnum<GetMatchType> {
    ALL("all"),
    REQUESTED("requested"),
    ACCEPTED("accepted"),
    REJECTED("rejected");

    private final String value;
    private final String enumName = this.name();
}
