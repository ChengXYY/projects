package com.my.formtool.aop;

import java.lang.annotation.*;

//自定义注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {

    String value() default "";

}
