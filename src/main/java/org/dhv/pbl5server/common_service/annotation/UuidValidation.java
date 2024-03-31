package org.dhv.pbl5server.common_service.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.dhv.pbl5server.common_service.constant.CommonConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validation constraint for a UUID in string format.
 * e.g. 26929514-237c-11ed-861d-0242ac120002
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotNull
@Pattern(regexp = CommonConstant.UUID_REGEX_PATTERN)
@ReportAsSingleViolation
public @interface UuidValidation {
    String message() default "pbl5.validation.constraints.UuidValidation.message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
