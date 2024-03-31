package org.dhv.pbl5server.common_service.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotBlank
@NotNull
@ReportAsSingleViolation
public @interface NotBlankStringValidation {
    String message() default "String must be not null and not empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
