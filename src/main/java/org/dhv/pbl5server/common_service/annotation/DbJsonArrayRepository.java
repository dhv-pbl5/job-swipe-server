package org.dhv.pbl5server.common_service.annotation;

import org.springframework.stereotype.Repository;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Repository
public @interface DbJsonArrayRepository {
}
