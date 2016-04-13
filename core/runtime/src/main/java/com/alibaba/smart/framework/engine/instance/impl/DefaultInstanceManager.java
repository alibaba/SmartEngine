package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.deployment.Deployer;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.Instance;
import com.alibaba.smart.framework.engine.instance.InstanceManager;
import com.alibaba.smart.framework.engine.instance.InstanceStore;
import com.alibaba.smart.framework.engine.invocation.Event;

/**
 * Created by ettear on 16-4-13.
 */
public class DefaultInstanceManager implements InstanceManager,LifeCycleListener {

    /**
     * 扩展点注册器
     */
    private ExtensionPointRegistry extensionPointRegistry;

    private Deployer deployer;

    private InstanceStore instanceStore;

    public DefaultInstanceManager(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public Instance start(String processId, String version, Event startEvent) {
        return null;
    }

    @Override
    public Instance load(String instanceId) {
        return this.instanceStore.load(instanceId);
    }

    @Override
    public void stop(String instanceId) {

    }

    @Override
    public void start() {
        this.deployer=this.extensionPointRegistry.getExtensionPoint(Deployer.class);
        this.instanceStore=this.extensionPointRegistry.getExtensionPoint(InstanceStore.class);
    }

    @Override
    public void stop() {

    }
}
