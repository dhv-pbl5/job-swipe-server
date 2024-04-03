package org.dhv.pbl5server.common_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataSortOrder {
    ASC("asc"),
    DESC("desc");

    private final String value;

    public static DataSortOrder fromString(String order) {
        return order.equalsIgnoreCase(DataSortOrder.ASC.getValue()) ? DataSortOrder.ASC : DataSortOrder.DESC;
    }
}
