// git commit -m "PBL-850 set up base"

package org.dhv.pbl5server.common_service.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
