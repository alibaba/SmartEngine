package com.alibaba.smart.framework.engine.extension.annoation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 高海军 帝奇 74394 on  2019-08-25 21:36.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExtensionBinding {

    String group();

    Class bindKey();

    int priority() default 0;

    //AnnotationLookup lookup()  ;

    //Class<? extends AnnotationLookup> lookup() default PlaceHolderAnnotationLookup.class;


    //String[] value()  default   {};
}
