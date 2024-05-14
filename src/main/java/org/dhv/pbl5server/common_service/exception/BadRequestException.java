// git commit -m "PBL-850 set up base"

package org.dhv.pbl5server.common_service.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
