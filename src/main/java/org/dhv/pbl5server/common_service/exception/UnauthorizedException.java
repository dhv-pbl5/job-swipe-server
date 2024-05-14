// git commit -m "PBL-850 set up base"

package org.dhv.pbl5server.common_service.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
