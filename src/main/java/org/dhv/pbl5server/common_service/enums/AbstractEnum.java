package org.dhv.pbl5server.common_service.enums;

import org.dhv.pbl5server.common_service.exception.InternalServerException;

public interface AbstractEnum<T> {
    String getEnumName();

    String getValue();

    static <T extends AbstractEnum<T>> T fromString(T[] values, String value) {
        for (T v : values)
            if (v.getValue().equalsIgnoreCase(value))
                return v;
        throw new InternalServerException("Invalid enum value: %s - %s".formatted(value, values[0].getClass().getSimpleName()));
    }
}
