package com.alibaba.smart.framework.engine;

import com.alibaba.smart.framework.engine.service.ExecutionService;
import com.alibaba.smart.framework.engine.service.ProcessService;
import com.alibaba.smart.framework.engine.service.RepositoryService;
import com.alibaba.smart.framework.engine.service.TaskService;

/**
 * Smart Engine Created by ettear on 16-4-12.
 */
public interface SmartEngine {

    void install()  ;

    
    void install(String moduleName, ClassLoader classLoader)  ;

    /**
     * This method will be invoked when started.
     */
    void start();

    /**
     * This method will be invoked when stopped
     */
    void stop();

    ClassLoader getClassLoader(String moduleName);

    RepositoryService getRepositoryService();

    ProcessService getProcessManager();

    ExecutionService getExecutionManager();

    TaskService getTaskManager();
}
