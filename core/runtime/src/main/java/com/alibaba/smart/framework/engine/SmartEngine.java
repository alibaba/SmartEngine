package com.alibaba.smart.framework.engine;

import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.deployment.Deployer;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.InstanceManager;
import com.alibaba.smart.framework.engine.invocation.Event;

/**
 * Smart Engine
 * Created by ettear on 16-4-12.
 */
public interface SmartEngine extends LifeCycleListener {
    void install(String moduleName,ClassLoader classLoader) throws EngineException;

    ExtensionPointRegistry getExtensionPointRegistry();

    ClassLoader getClassLoader(String moduleName);

    Deployer getDeployer();

    InstanceManager getInstanceManager();
}
