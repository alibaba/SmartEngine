package com.alibaba.smart.framework.engine.configuration;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;

/**
 * @author 高海军 帝奇  2016.11.11
 */
public interface ProcessEngineConfiguration {

    ExtensionPointRegistry getExtensionPointRegistry();

    void setExtensionPointRegistry(ExtensionPointRegistry extensionPointRegistry);

    Object getBean(String bean);
}
