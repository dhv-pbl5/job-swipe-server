package org.dhv.pbl5server.search_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;

@Getter
@AllArgsConstructor
public enum SearchType implements AbstractEnum<SearchType> {
    NAME("name");

    private final String value;
    private final String enumName = this.name();
}
