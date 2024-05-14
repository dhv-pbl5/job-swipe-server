// git commit -m "PBL-850 set up base"

package org.dhv.pbl5server.common_service.exception;

public class InternalServerException extends RuntimeException {

    public InternalServerException(String message) {
        super(message);
    }
}
