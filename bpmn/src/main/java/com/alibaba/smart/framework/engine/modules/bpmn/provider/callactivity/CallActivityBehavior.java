package com.alibaba.smart.framework.engine.modules.bpmn.provider.callactivity;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
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
@ExtensionBinding(type = ExtensionConstant.ACTIVITY_BEHAVIOR,binding = CallActivity.class)
public class CallActivityBehavior extends AbstractActivityBehavior<CallActivity> {

    //public CallActivityBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
    //    super(extensionPointRegistry, runtimeActivity);
    //}


    public CallActivityBehavior() {
        super();
    }


    @Override
    public boolean enter(ExecutionContext context) {
        CallActivity callActivity = this.getModel();
        String processDefinitionId =  callActivity.getCalledElement();
        String processDefinitionVersion = callActivity.getCalledElementVersion();

        return this.call(context.getProcessInstance().getInstanceId(), context.getExecutionInstance().getInstanceId(),
            processDefinitionId, processDefinitionVersion, context);
    }

    //TODO ettear 与DefaultProcessCommandService的逻辑合并
    private boolean call(String parentInstanceId, String parentExecutionInstanceId, String processDefinitionId,
                         String version, ExecutionContext context) {

        ExecutionContext subProcessExecutionContext = this.extensionPointRegistry.getExtensionPoint(InstanceContextFactory.class)
            .create();
        subProcessExecutionContext.setParent(context);
        subProcessExecutionContext.setExtensionPointRegistry(this.extensionPointRegistry);
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(
            SmartEngine.class).getProcessEngineConfiguration();
        subProcessExecutionContext.setProcessEngineConfiguration(processEngineConfiguration);
        //TODO ettear 改成clone模式
        subProcessExecutionContext.setRequest(context.getRequest());

        PvmProcessDefinition pvmProcessDefinition = this.extensionPointRegistry.getExtensionPoint(
            ProcessDefinitionContainer.class).getPvmProcessDefinition(processDefinitionId, version);
        subProcessExecutionContext.setPvmProcessDefinition(pvmProcessDefinition);

        // TUNE 减少不必要的对象创建
        PvmProcessInstance pvmProcessInstance = new DefaultPvmProcessInstance();

        ProcessInstance processInstance = processInstanceFactory.create(subProcessExecutionContext);
        processInstance.setParentInstanceId(parentInstanceId);
        processInstance.setParentExecutionInstanceId(parentExecutionInstanceId);

        subProcessExecutionContext.setProcessInstance(processInstance);

        processInstance = pvmProcessInstance.start(subProcessExecutionContext);

        processInstance = CommonServiceHelper.insertAndPersist(processInstance, context.getRequest(), extensionPointRegistry);

        return InstanceStatus.completed!=processInstance.getStatus();
    }
}