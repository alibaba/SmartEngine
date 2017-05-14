package com.alibaba.smart.framework.engine.modules.bpmn.provider.callactivity;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.callactivity.CallActivity;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmProcessInstance;
import com.alibaba.smart.framework.engine.service.command.impl.CommonServiceHelper;

/**
 * Created by 高海军 帝奇 74394 on 2017 May  16:07.
 */
public class CallActivityBehavior extends AbstractActivityBehavior<CallActivity> implements ActivityBehavior<CallActivity> {

    public CallActivityBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    @Override
    public void buildInstanceRelationShip(PvmActivity pvmActivity, ExecutionContext executionContext) {
        ProcessInstance parentProcessInstance = executionContext.getProcessInstance();

        ActivityInstance parentActivityInstance = super.activityInstanceFactory.create(pvmActivity, executionContext);
        ExecutionInstance parentExecutionInstance = super.executionInstanceFactory.create(parentActivityInstance,  executionContext);

        parentActivityInstance.setExecutionInstance(parentExecutionInstance);
        parentProcessInstance.addNewActivityInstance(parentActivityInstance);


        //BE AWARE:set parentProcessInstance
        executionContext.setParentProcessInstance(parentProcessInstance);

        CallActivity callActivity =   (CallActivity)pvmActivity.getModel();

        String processDefinitionId =  callActivity.getCalledElement();
        String processDefinitionVersion = callActivity.getCalledElementVersion();


        ProcessDefinitionContainer processDefinitionContainer = executionContext.getExtensionPointRegistry().getExtensionPoint(ProcessDefinitionContainer.class);

        PvmProcessDefinition pvmProcessDefinition = processDefinitionContainer.get(processDefinitionId, processDefinitionVersion);
        executionContext.setPvmProcessDefinition(pvmProcessDefinition);

        PvmProcessInstance pvmProcessInstance = new DefaultPvmProcessInstance();

        ProcessInstance processInstance = pvmProcessInstance.start(executionContext);


        CommonServiceHelper.insertAndPersist(processInstance, executionContext.getRequest(),executionContext.getExtensionPointRegistry());

        //BE AWARE: 代码执行此处时,子流程已经开始启动,对应的子流程环节已经执行完毕,子流程数据也完成了持久化。此时,子流程状态可能处于running或者completed。
        // 那么,对应的父流程也需要返回出去。所以,这里将父流程实例返回放到context里面,供外部调用。
        ProcessInstance parentProcessInstance1 = executionContext.getParentProcessInstance();

        //BE AWARE: 如果此时,子流程是全自动型节点,那么parentProcessInstance1应该为空。因为此时代码时序需要执行片段了。
        if(null!= parentProcessInstance1){
            executionContext.setProcessInstance(parentProcessInstance1);
        }

    }

    @Override
    public boolean needSuspend(ExecutionContext context) {
        return false;
    }
}