package com.qiu.anotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 参数属性
 */
@Retention(RUNTIME)
@Target({PARAMETER})
public @interface ParameterInfo {

    /**
     * 参数名
     */
    String parameter();

    /**
     * 参数描述
     */
    String desc();

    /**
     * 是否必填（默认不能为空）<br>
     * 因基础类型不能为空值，此设置无效
     */
    boolean must() default true;
}
