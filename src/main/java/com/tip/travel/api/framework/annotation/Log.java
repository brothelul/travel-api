package com.tip.travel.api.framework.annotation;

import com.tip.travel.api.framework.constants.LogTypeConstant;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Log {
    int logType() default LogTypeConstant.COMMON_TYPE;
}
