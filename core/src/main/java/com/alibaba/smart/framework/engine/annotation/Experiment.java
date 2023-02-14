package com.alibaba.smart.framework.engine.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 高海军 帝奇 74394 on  2019-11-12 16:07.
 *
 * 用于标记这段代码可能是实验特性.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Experiment {

}
