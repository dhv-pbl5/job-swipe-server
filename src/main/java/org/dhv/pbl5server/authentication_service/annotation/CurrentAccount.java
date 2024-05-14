package org.dhv.pbl5server.authentication_service.annotation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

// git commit -m "PBL-511 login for company and user"

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Documented
@AuthenticationPrincipal
public @interface CurrentAccount {
}
