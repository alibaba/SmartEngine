package com.alibaba.smart.framework.engine.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.impl.DefaultExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.util.ClassLoaderUtil;
import com.alibaba.smart.framework.engine.service.ExecutionService;
import com.alibaba.smart.framework.engine.service.ProcessService;
import com.alibaba.smart.framework.engine.service.RepositoryService;
import com.alibaba.smart.framework.engine.service.TaskService;
import com.alibaba.smart.framework.engine.util.ThreadLocalUtil;

/**
 * Default Smart Engine Created by ettear on 16-4-12.
 */
public class DefaultSmartEngine implements SmartEngine {

    private final static String      DEFAULT_MODULE    = "framework";

    private ExtensionPointRegistry   extensionPointRegistry;
    private Map<String, ClassLoader> classLoaderHolder = new ConcurrentHashMap<>();
    
    @Override
    public void init(ProcessEngineConfiguration processEngineConfiguration ) {
        this.extensionPointRegistry = new DefaultExtensionPointRegistry(this);
        
        processEngineConfiguration.setExtensionPointRegistry(this.extensionPointRegistry);
        ThreadLocalUtil.set(processEngineConfiguration);
        
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
                    //TODO
                }
            } catch (EngineException loadException) {
                throw new EngineException("Init engine failure!", loadException);
            }
        }
    }

  

    @Override
    public void destory() {
        this.extensionPointRegistry.stop();
        ThreadLocalUtil.remove();

    }

    @Override
    public ClassLoader getClassLoader(String moduleName) {
        if (StringUtils.isBlank(moduleName)) {
            moduleName = DEFAULT_MODULE;
        }
        return this.classLoaderHolder.get(moduleName);
    }

    @Override
    public RepositoryService getRepositoryService() {
        return this.extensionPointRegistry.getExtensionPoint(RepositoryService.class);
    }

    @Override
    public ProcessService getProcessService() {
        return this.extensionPointRegistry.getExtensionPoint(ProcessService.class);
    }

    @Override
    public ExecutionService getExecutionService() {
        return this.extensionPointRegistry.getExtensionPoint(ExecutionService.class);
    }

    @Override
    public TaskService getTaskService() {
        return this.extensionPointRegistry.getExtensionPoint(TaskService.class);
    }

    public ExtensionPointRegistry getExtensionPointRegistry() {
        return extensionPointRegistry;
    }

}
