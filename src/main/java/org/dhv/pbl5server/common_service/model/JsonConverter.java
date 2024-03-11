package org.dhv.pbl5server.common_service.model;

public interface JsonConverter<T> {
    String toJson();

    T fromJson(String json);
}
