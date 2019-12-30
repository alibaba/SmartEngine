package com.alibaba.smart.framework.engine.modules.bpmn.provider.callactivity;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.callactivity.CallActivity;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmProcessInstance;
import com.alibaba.smart.framework.engine.service.command.impl.CommonServiceHelper;

/**
 * Created by 高海军 帝奇 74394 on 2017 May  16:07.
 */
@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = CallActivity.class)
public class CallActivityBehavior extends AbstractActivityBehavior<CallActivity> {

    public CallActivityBehavior() {
        super();
    }

    @Override
    public boolean enter(PvmActivity pvmActivity, ExecutionContext context) {

        super.enter(pvmActivity, context);


        CallActivity callActivity = (CallActivity)pvmActivity.getModel();

        ProcessInstance processInstance = context.getProcessInstance();
        ExecutionInstance executionInstance = context.getExecutionInstance();
        return this.call(processInstance.getInstanceId(), executionInstance.getInstanceId(),
            callActivity, context);
    }

    //TODO ettear 与DefaultProcessCommandService的逻辑合并
    private boolean call(String parentInstanceId, String parentExecutionInstanceId,CallActivity callActivity, ExecutionContext context) {

        String processDefinitionId =  callActivity.getCalledElement();
        String version = callActivity.getCalledElementVersion();

        ExecutionContext subContext = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.COMMON,InstanceContextFactory.class)
            .create();
        subContext.setParent(context);

        subContext.setProcessEngineConfiguration(processEngineConfiguration);
        subContext.setRequest(context.getRequest());

        ProcessDefinition pvmProcessDefinition = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,
            ProcessDefinitionContainer.class).getProcessDefinition(processDefinitionId, version);
        subContext.setProcessDefinition(pvmProcessDefinition);

        // TUNE 减少不必要的对象创建
        PvmProcessInstance pvmProcessInstance = new DefaultPvmProcessInstance();

        ProcessInstance subProcessInstance = processInstanceFactory.create(subContext);
        subProcessInstance.setParentInstanceId(parentInstanceId);
        subProcessInstance.setParentExecutionInstanceId(parentExecutionInstanceId);

        subContext.setProcessInstance(subProcessInstance);

        subProcessInstance = pvmProcessInstance.start(subContext);

        subProcessInstance = CommonServiceHelper.insertAndPersist(subProcessInstance, context.getRequest(), processEngineConfiguration);

        return InstanceStatus.completed!=subProcessInstance.getStatus();
    }
}