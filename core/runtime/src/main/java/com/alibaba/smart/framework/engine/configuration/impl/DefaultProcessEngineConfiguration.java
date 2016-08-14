package com.alibaba.smart.framework.engine.configuration.impl;

import lombok.Data;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;

@Data
public class DefaultProcessEngineConfiguration implements ProcessEngineConfiguration {

    private ExtensionPointRegistry   extensionPointRegistry;

}
