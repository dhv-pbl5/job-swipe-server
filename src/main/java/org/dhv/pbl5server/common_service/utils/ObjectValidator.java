// git commit -m "PBL-850 set up base"

package org.dhv.pbl5server.common_service.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ObjectValidator {
    private final SpringValidatorAdapter validator;

    public void validate(List<?> list) {
        for (var obj : list) {
            ErrorUtils.checkConstraintViolation(validator.validate(obj));
        }
    }

    public void validate(Object object) {
        ErrorUtils.checkConstraintViolation(validator.validate(object));
    }
}
