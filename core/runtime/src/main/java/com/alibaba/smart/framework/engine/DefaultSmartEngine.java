package com.alibaba.smart.framework.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensibility.ClassLoaderExtensionPoint;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensibility.exception.ExtensionPointLoadException;
import com.alibaba.smart.framework.engine.extensibility.impl.DefaultExtensionPointRegistry;
import com.alibaba.smart.framework.engine.service.ExecutionService;
import com.alibaba.smart.framework.engine.service.ProcessService;
import com.alibaba.smart.framework.engine.service.RepositoryService;
import com.alibaba.smart.framework.engine.service.TaskService;

/**
 * Default Smart Engine Created by ettear on 16-4-12.
 */
public class DefaultSmartEngine implements SmartEngine {

    private final static String      DEFAULT_MODULE = "framework";

    private ExtensionPointRegistry   extensionPointRegistry;
    private Map<String, ClassLoader> classLoaders   = new ConcurrentHashMap<>();

    public DefaultSmartEngine() throws EngineException {
        this.extensionPointRegistry = new DefaultExtensionPointRegistry(this);
        ClassLoader classLoader = DefaultSmartEngine.class.getClassLoader();
        this.install(DEFAULT_MODULE, classLoader);
    }

    @Override
    public void install() {
        install(DEFAULT_MODULE,DefaultSmartEngine.class.getClassLoader());
    }
    
    
    @Override
    public void install(String moduleName, ClassLoader classLoader) throws EngineException {
        if (StringUtils.isBlank(moduleName)) {
            moduleName = DEFAULT_MODULE;
        }
        if (!this.classLoaders.containsKey(moduleName)) {
            try {
                boolean loaded = false;
                for (ClassLoader loader : classLoaders.values()) {
                    if (loader == classLoader) {
                        loaded = true;
                        break;
                    }
                }
                this.classLoaders.put(moduleName, classLoader);
                if (!loaded && this.extensionPointRegistry instanceof ClassLoaderExtensionPoint) {
                    ((ClassLoaderExtensionPoint) this.extensionPointRegistry).load(moduleName, classLoader);
                }
            } catch (ExtensionPointLoadException loadException) {
                throw new EngineException("Init engine failure!", loadException);
            }
        }
    }

    @Override
    public void start() {
        this.extensionPointRegistry.start();
    }

    @Override
    public void stop() {
        this.extensionPointRegistry.stop();
    }

    @Override
    public ClassLoader getClassLoader(String moduleName) {
        if (StringUtils.isBlank(moduleName)) {
            moduleName = DEFAULT_MODULE;
        }
        return this.classLoaders.get(moduleName);
    }

    @Override
    public RepositoryService getRepositoryService() {
        return this.extensionPointRegistry.getExtensionPoint(RepositoryService.class);
    }

    @Override
    public ProcessService getProcessManager() {
        return this.extensionPointRegistry.getExtensionPoint(ProcessService.class);
    }

    @Override
    public ExecutionService getExecutionManager() {
        return this.extensionPointRegistry.getExtensionPoint(ExecutionService.class);
    }

    @Override
    public TaskService getTaskManager() {
        return this.extensionPointRegistry.getExtensionPoint(TaskService.class);
    }

    public ExtensionPointRegistry getExtensionPointRegistry() {
        return extensionPointRegistry;
    }


}
