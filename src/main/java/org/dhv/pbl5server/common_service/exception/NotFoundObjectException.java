// git commit -m "PBL-850 set up base"

package org.dhv.pbl5server.common_service.exception;

public class NotFoundObjectException extends RuntimeException {
    public NotFoundObjectException(String message) {
        super(message);
    }
}
