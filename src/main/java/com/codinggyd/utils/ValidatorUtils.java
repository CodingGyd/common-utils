package com.codinggyd.utils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Iterator;
import java.util.Set;

public class ValidatorUtils {
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public ValidatorUtils() {
    }

    public static <T> void validate(T object, Class<?>... groups) {
        Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<T>> errors = validator.validate(object, groups);
        if (errors.size() != 0) {
            StringBuilder builder = new StringBuilder();
            if (errors.size() > 6) {
                builder.append(String.format("校验未通过：共%d项错误</br>", errors.size()));
            } else {
                builder.append("校验不通过：**");
            }

            int i = 1;
            for(Iterator var5 = errors.iterator(); var5.hasNext(); ++i) {
                ConstraintViolation<T> error = (ConstraintViolation)var5.next();
                if (i > 6) {
                    builder.append("......");
                    break;
                }
                builder.append(error.getMessage()).append("**");
            }
            throw new RuntimeException(builder.toString());
        }
    }
}
