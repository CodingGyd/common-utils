package com.codinggyd.validator;

/**
 * @ClassName EnumIntegerValid
 * @Description TODO
 * @Author guoyading
 * @Date 2023/7/20 13:51
 * @Version 1.0
 */

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EnumStringValidator.class})
@Documented
public @interface EnumStringValid {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?>[] target() default {};

    /**
     * 允许的枚举
     * @return
     */
    Class<? extends Enum<?>> enumClass();
}