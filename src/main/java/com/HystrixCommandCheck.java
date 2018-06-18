package com;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lizhibo (Employee ID: R0015004)
 * @version 1.0.0, 2018/5/20 15:12
 * @since 1.0.0, 2018/5/20 15:12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface HystrixCommandCheck {

}
