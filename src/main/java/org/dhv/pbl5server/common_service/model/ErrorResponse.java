// git commit -m "PBL-850 set up base"

package org.dhv.pbl5server.common_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
}
