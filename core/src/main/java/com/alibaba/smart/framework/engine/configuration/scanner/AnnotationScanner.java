package com.alibaba.smart.framework.engine.configuration.scanner;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;

import java.lang.annotation.Annotation;
import java.util.Map;

/** Created by 高海军 帝奇 74394 on 2019 December 16:03. */
public interface AnnotationScanner {

    void scan(
            ProcessEngineConfiguration processEngineConfiguration,
            Class<? extends Annotation> bindingAnnotationClazz);

    Map<String, ExtensionBindingResult> getScanResult();

    <T> T getExtensionPoint(String group, Class<T> modelType);

    Object getObject(String group, Class modelType);

    void clear();
}
