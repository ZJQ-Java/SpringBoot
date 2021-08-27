/**
 *
 */
package com.qiu.anotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 方法属性
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface MethodInfo {
    /**
     * 方法描述
     *
     */
    String value();

    /**
     * 展示类型<br>
     * json table
     */
    String showType() default "json";
}
