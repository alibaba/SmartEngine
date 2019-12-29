package com.alibaba.smart.framework.engine.configuration;


import java.lang.annotation.Annotation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.Activity;

/**
 * Created by 高海军 帝奇 74394 on 2019 December  16:03.
 */
public interface AnnotationScanner {

     void scan(String packageName, Class<? extends Annotation> bindingAnnotationClazz);

}
