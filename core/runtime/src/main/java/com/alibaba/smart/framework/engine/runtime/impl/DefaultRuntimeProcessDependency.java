package com.alibaba.smart.framework.engine.runtime.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.smart.framework.engine.assembly.Process;
import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.deployment.ProcessContainer;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.invocation.Message;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;
import com.alibaba.smart.framework.engine.runtime.RuntimeTransition;

/**
 * DefaultRuntimeProcessDependency Created by ettear on 16-4-21.
 */
@Data
public class DefaultRuntimeProcessDependency implements RuntimeProcess {

    private RuntimeProcess                 processProxy;
    private String                         uri;
    private String                         refUri;
    private String                         refProcessId;
    private String                         refProcessVersion;
    private Map<String, RuntimeTransition> incomeTransitions  = new ConcurrentHashMap<>();
    private Map<String, RuntimeTransition> outcomeTransitions = new ConcurrentHashMap<>();
    private ExtensionPointRegistry         extensionPointRegistry;

    public DefaultRuntimeProcessDependency(String refUri) {
        this.refUri = refUri;
    }

    public DefaultRuntimeProcessDependency(String refProcessId, String refProcessVersion) {
        this.refProcessId = refProcessId;
        this.refProcessVersion = refProcessVersion;
    }

    @Override
    public Process getModel() {
        return this.processProxy.getModel();
    }

    @Override
    public Message run(InstanceContext context) {
        return this.processProxy.run(context);
    }

    @Override
    public Message resume(InstanceContext context) {
        return this.processProxy.resume(context);
    }

    @Override
    public Map<String, RuntimeTransition> getIncomeTransitions() {
        return this.incomeTransitions;
    }

    @Override
    public Map<String, RuntimeTransition> getOutcomeTransitions() {
        return this.outcomeTransitions;
    }

    @Override
    public Message execute(InstanceContext context) {
        return this.processProxy.execute(context);
    }

    @Override
    public boolean isStartActivity() {
        return false;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Class<?> getModelType() {
        return null;
    }

    @Override
    public Message invoke(String event, InstanceContext context) {
        return this.processProxy.invoke(event, context);
    }

    @Override
    public Message invokeAsync(String event, InstanceContext context) {
        return this.processProxy.invokeAsync(event, context);
    }

    @Override
    public void start() {
        ProcessContainer processContainer = this.extensionPointRegistry.getExtensionPoint(ProcessContainer.class);
        if (StringUtils.isNotBlank(this.refUri)) {
            this.processProxy = processContainer.get(this.refUri);
        } else if (StringUtils.isNotBlank(this.refProcessId)) {
            this.processProxy = processContainer.get(this.refProcessId, this.refProcessVersion);
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public RuntimeActivity getStartActivity() {
        return processProxy.getStartActivity();
    }
}
