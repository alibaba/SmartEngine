package com.alibaba.smart.framework.process.engine;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.process.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.process.service.RuntimeService;
import com.alibaba.smart.framework.process.service.TaskService;

public interface ProcessEngine {

    void init(ProcessEngineConfiguration processEngineConfiguration);

    RuntimeService getRuntimeService();

    TaskService getTaskService();

    ExtensionPointRegistry getExtensionPointRegistry();

    // RepositoryService getRepositoryService();

}
