package com.codinggyd.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * @description value值是String类型的枚举校验器
 */
public class EnumStringValidator implements ConstraintValidator<EnumStringValid,String> {

    private Class<? extends Enum> enumClass;

    @Override
    public void initialize(EnumStringValid enumStringValid) {
        enumClass = enumStringValid.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || "".equals(value)) {
            return true;
        }

        EnumValidate[] enums = (EnumValidate[]) enumClass.getEnumConstants();
        if(enums ==null || enums.length == 0){
            return false;
        }

        return enums[0].existValidate(value);
    }
}