package com.my.common.aop;

import java.lang.annotation.*;

//自定义注解
@Target(value = {ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {

    String value() default "";

}
