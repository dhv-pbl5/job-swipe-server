package org.dhv.pbl5server.profile_service.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import org.dhv.pbl5server.common_service.utils.CommonUtils;

@Convert
public class JsonConverter<T> implements AttributeConverter<T, String> {
    private Class<T> clazz;

    @Override
    public String convertToDatabaseColumn(T attribute) {
        return CommonUtils.convertToJson(attribute);
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        return CommonUtils.decodeJson(dbData, clazz);
    }
}
