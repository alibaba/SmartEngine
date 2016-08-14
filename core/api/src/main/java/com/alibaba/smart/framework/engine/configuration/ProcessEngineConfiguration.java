package com.alibaba.smart.framework.engine.configuration;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;

public interface ProcessEngineConfiguration {

    ExtensionPointRegistry getExtensionPointRegistry();

    void setExtensionPointRegistry(ExtensionPointRegistry extensionPointRegistry);
}
