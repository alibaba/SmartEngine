package com.alibaba.smart.framework.engine;

import com.alibaba.smart.framework.engine.deployment.Deployer;
import com.alibaba.smart.framework.engine.invocation.Event;

/**
 * Created by ettear on 16-4-12.
 */
public interface SmartEngine {
    void init(ClassLoader classLoader) throws EngineException;

    Deployer getDeployer();



}
