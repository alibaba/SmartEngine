package com.alibaba.smart.framework.engine.modules.bpmn.provider.callactivity;

import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.callactivity.CallActivity;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmProcessInstance;
import com.alibaba.smart.framework.engine.service.command.impl.CommonServiceHelper;

/**
 * Created by 高海军 帝奇 74394 on 2017 May  16:07.
 */
public class CallActivityBehavior extends AbstractActivityBehavior<CallActivity> {

    public CallActivityBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    @Override
    public boolean enter(ExecutionContext context) {
        CallActivity callActivity = this.getModel();
        String processDefinitionId =  callActivity.getCalledElement();
        String processDefinitionVersion = callActivity.getCalledElementVersion();

        return this.call(context.getProcessInstance().getInstanceId(), context.getExecutionInstance().getInstanceId(),
            processDefinitionId, processDefinitionVersion, context.getRequest());
    }

    //TODO ettear 与DefaultProcessCommandService的逻辑合并
    private boolean call(Long parentInstanceId, Long parentExecutionInstanceId, String processDefinitionId,
                         String version, Map<String, Object> request) {

        ExecutionContext executionContext = this.extensionPointRegistry.getExtensionPoint(InstanceContextFactory.class)
            .create();
        executionContext.setExtensionPointRegistry(this.extensionPointRegistry);
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(
            SmartEngine.class).getProcessEngineConfiguration();
        executionContext.setProcessEngineConfiguration(processEngineConfiguration);
        executionContext.setRequest(request);

        PvmProcessDefinition pvmProcessDefinition = this.extensionPointRegistry.getExtensionPoint(
            ProcessDefinitionContainer.class).getPvmProcessDefinition(processDefinitionId, version);
        executionContext.setPvmProcessDefinition(pvmProcessDefinition);

        // TUNE 减少不必要的对象创建
        PvmProcessInstance pvmProcessInstance = new DefaultPvmProcessInstance();

        ProcessInstance processInstance = processInstanceFactory.create(executionContext);
        processInstance.setParentInstanceId(parentInstanceId);
        processInstance.setParentExecutionInstanceId(parentExecutionInstanceId);

        executionContext.setProcessInstance(processInstance);

        processInstance = pvmProcessInstance.start(executionContext);

        processInstance = CommonServiceHelper.insertAndPersist(processInstance, request, extensionPointRegistry);

        return InstanceStatus.completed!=processInstance.getStatus();
    }
}