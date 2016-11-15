package com.alibaba.smart.framework.engine.impl;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.impl.DefaultExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.util.ClassLoaderUtil;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public class DefaultSmartEngine implements SmartEngine {

    private final static String      DEFAULT_MODULE    = "framework";

    private ExtensionPointRegistry   extensionPointRegistry;
    private Map<String, ClassLoader> classLoaderHolder = new ConcurrentHashMap<>();


    
    @Override
    public void init(ProcessEngineConfiguration processEngineConfiguration ) {
        this.extensionPointRegistry = new DefaultExtensionPointRegistry(this);
        
        processEngineConfiguration.setExtensionPointRegistry(this.extensionPointRegistry);

        ClassLoader classLoader = ClassLoaderUtil.getStandardClassLoader();
        this.install(DEFAULT_MODULE, classLoader);
        this.extensionPointRegistry.start();
    }

    private void install(String moduleName, ClassLoader classLoader) throws EngineException {
        if (StringUtils.isBlank(moduleName)) {
            moduleName = DEFAULT_MODULE;
        }
        if (!this.classLoaderHolder.containsKey(moduleName)) {
            try {
                boolean loaded = false;
                for (ClassLoader loader : classLoaderHolder.values()) {
                    if (loader == classLoader) {
                        loaded = true;
                        break;
                    }
                }
                this.classLoaderHolder.put(moduleName, classLoader);
                if (!loaded) {
                     this.extensionPointRegistry.register(moduleName, classLoader);
                } else {
                    throw  new IllegalStateException("duplicated module :"+moduleName);
                }
            } catch (EngineException loadException) {
                throw new EngineException("Init engine failure!", loadException);
            }
        }
    }

  

    @Override
    public void destroy() {
        this.extensionPointRegistry.stop();

    }

    @Override
    public ClassLoader getClassLoader(String moduleName) {
        if (StringUtils.isBlank(moduleName)) {
            moduleName = DEFAULT_MODULE;
        }
        return this.classLoaderHolder.get(moduleName);
    }

    @Override
    public RepositoryCommandService getRepositoryService() {
        return this.extensionPointRegistry.getExtensionPoint(RepositoryCommandService.class);
    }

    @Override
    public ProcessCommandService getProcessService() {
        return this.extensionPointRegistry.getExtensionPoint(ProcessCommandService.class);
    }

    @Override
    public ExecutionCommandService getExecutionCommandService() {
        return this.extensionPointRegistry.getExtensionPoint(ExecutionCommandService.class);
    }

    @Override
    public TaskCommandService getTaskCommandService() {
        return this.extensionPointRegistry.getExtensionPoint(TaskCommandService.class);
    }

    @Override
    public TaskQueryService getTaskQueryService() {
        return this.extensionPointRegistry.getExtensionPoint(TaskQueryService.class);
    }

    public ExtensionPointRegistry getExtensionPointRegistry() {
        return extensionPointRegistry;
    }

}
