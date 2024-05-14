// git commit -m "PBL-850 set up base"

package org.dhv.pbl5server.common_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataSortOrder implements AbstractEnum<DataSortOrder> {
    ASC("asc"),
    DESC("desc");

    private final String value;
    private final String enumName = this.name();
}