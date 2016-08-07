package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.deployment.ProcessContainer;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcess;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.model.assembly.Process;

/**
 * DefaultRuntimeProcessDependency Created by ettear on 16-4-21.
 */
@Data
public class DefaultRuntimeProcessDependency implements PvmProcess {

    private PvmProcess                 processProxy;
    private String                         uri;
    private String                         refUri;
    private String                         refProcessId;
    private String                         refProcessVersion;
    private Map<String, PvmTransition> incomeTransitions  = new ConcurrentHashMap<>();
    private Map<String, PvmTransition> outcomeTransitions = new ConcurrentHashMap<>();
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
    public Map<String, PvmTransition> getIncomeTransitions() {
        return this.incomeTransitions;
    }

    @Override
    public Map<String, PvmTransition> getOutcomeTransitions() {
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
    public PvmActivity getStartActivity() {
        return processProxy.getStartActivity();
    }
}
