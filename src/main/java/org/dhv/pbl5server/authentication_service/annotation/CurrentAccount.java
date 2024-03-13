package org.dhv.pbl5server.authentication_service.annotation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Documented
@AuthenticationPrincipal
public @interface CurrentAccount {
}
