package com.codinggyd.annotation;

import java.lang.annotation.*;

/**
 * 中文描述注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})//注解作用于类和字段上
public @interface CNDesc {
    String value();
}