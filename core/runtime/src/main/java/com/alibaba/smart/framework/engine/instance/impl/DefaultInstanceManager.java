package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.deployment.Deployer;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.instance.InstanceManager;
import com.alibaba.smart.framework.engine.instance.InstanceStore;
import com.alibaba.smart.framework.engine.invocation.Event;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcessComponent;

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
    public ProcessInstance start(String processId, String version, Event startEvent) {
        RuntimeProcessComponent processComponent=this.deployer.getProcess(processId,version);
        processComponent.getProcess();
        return null;
    }

    @Override
    public ProcessInstance load(String instanceId) {
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

    private ProcessInstance createInstance(RuntimeProcessComponent runtimeProcessComponent){
        DefaultProcessInstance instance=new DefaultProcessInstance();

        return instance;
    }
}
