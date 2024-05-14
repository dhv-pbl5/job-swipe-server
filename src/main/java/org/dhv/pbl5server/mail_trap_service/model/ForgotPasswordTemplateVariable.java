package org.dhv.pbl5server.mail_trap_service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;

// git commit -m "PBL-518 forgot password for company"

@Getter
@Setter
@Builder
@JsonSnakeCaseNaming
public class ForgotPasswordTemplateVariable {
    private String userEmail;
    private String resetPassCode;
}
