package com.alibaba.smart.framework.engine;

import com.alibaba.smart.framework.engine.deployment.Deployer;
import com.alibaba.smart.framework.engine.instance.manager.ExecutionManager;
import com.alibaba.smart.framework.engine.instance.manager.ProcessManager;
import com.alibaba.smart.framework.engine.instance.manager.TaskManager;

/**
 * Smart Engine
 * Created by ettear on 16-4-12.
 */
public interface SmartEngine {

    void install(String moduleName, ClassLoader classLoader) throws EngineException;

    /**
     * This method will be invoked when started.
     */
    void start();

    /**
     * This method will be invoked when stopped
     */
    void stop();

    ClassLoader getClassLoader(String moduleName);

    Deployer getDeployer();

    ProcessManager getProcessManager();

    ExecutionManager getExecutionManager();

    TaskManager getTaskManager();
}
