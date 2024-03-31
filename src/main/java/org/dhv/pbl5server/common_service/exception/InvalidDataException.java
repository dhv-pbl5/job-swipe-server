package org.dhv.pbl5server.common_service.exception;

public class InvalidDataException extends RuntimeException {
    public String resource;
    public String fieldName;

    public InvalidDataException(String resource, String fieldName, String message) {
        super(message);
        this.resource = resource;
        this.fieldName = fieldName;
    }
}
