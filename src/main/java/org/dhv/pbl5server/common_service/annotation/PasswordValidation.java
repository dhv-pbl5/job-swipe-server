package org.dhv.pbl5server.common_service.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.dhv.pbl5server.common_service.constant.CommonConstant;
import org.hibernate.validator.constraints.Length;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validation constraint for a password.
 * The password policy is:
 * -- 1. At least 8 chars
 * -- 2. At most 32 chars
 * -- 3. Contains at least one digit
 * -- 4. Contains at least one lower alpha char and one upper alpha char
 * -- 5. Does not contain space, tab, etc.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotBlank
@NotNull
@Length(min = 8, max = 32)
@Pattern(regexp = CommonConstant.PASSWORD_REGEXP_PATTERN)
@ReportAsSingleViolation
public @interface PasswordValidation {
    String message() default "Password format is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
