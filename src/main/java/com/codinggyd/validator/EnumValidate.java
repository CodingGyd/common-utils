package com.codinggyd.validator;

/**
 * @description 枚举值校验
 */
public interface EnumValidate<T> {

    /**
     * 校验枚举值是否存在
     */
    boolean existValidate(T value);

}
