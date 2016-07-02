package com.alibaba.smart.framework.engine;

import com.alibaba.smart.framework.engine.service.ExecutionService;
import com.alibaba.smart.framework.engine.service.ProcessService;
import com.alibaba.smart.framework.engine.service.RepositoryService;
import com.alibaba.smart.framework.engine.service.TaskService;


/**
 * @author 高海军 帝奇 
 */
public interface SmartEngine {

    RepositoryService getRepositoryService();

    ProcessService getProcessManager();

    ExecutionService getExecutionManager();

    TaskService getTaskManager();
 
    void init();

    void destory();

    ClassLoader getClassLoader(String moduleName);
}
