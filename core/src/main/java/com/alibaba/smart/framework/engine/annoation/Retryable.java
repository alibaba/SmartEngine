package com.alibaba.smart.framework.engine.annoation;

import java.lang.annotation.*;

/**
 * Created by 高海军 帝奇 74394 on  2019-11-12 16:07.
 *
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Retryable {

    int maxAttempts() default 3;


    // delay 1000ms
    long delay() default 1000;

}
