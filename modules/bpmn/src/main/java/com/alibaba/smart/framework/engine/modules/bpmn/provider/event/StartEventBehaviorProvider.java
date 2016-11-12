package com.alibaba.smart.framework.engine.modules.bpmn.provider.event;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.StartEvent;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.process.AbstractBpmnActivityBehaviorProvider;
import com.alibaba.smart.framework.engine.provider.ActivityBehaviorProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class StartEventBehaviorProvider extends AbstractBpmnActivityBehaviorProvider<StartEvent> implements ActivityBehaviorProvider<StartEvent> {

    public StartEventBehaviorProvider(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }
    
    @Override
    public Invoker createInvoker(String event) {
        // TODO Auto-generated method stub
        return super.createInvoker(event);
    }


    @Override
    public void execute(PvmActivity runtimeActivity, ExecutionContext executionContext) {

        ExtensionPointRegistry extensionPointRegistry = executionContext.getExtensionPointRegistry();
        ProcessInstanceFactory processInstanceFactory= extensionPointRegistry.getExtensionPoint(ProcessInstanceFactory.class) ;
        ExecutionInstanceFactory executionInstanceFactory= extensionPointRegistry.getExtensionPoint(ExecutionInstanceFactory.class) ;
        ActivityInstanceFactory activityInstanceFactory = extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);


        ProcessInstance processInstance = processInstanceFactory.create();
        ExecutionInstance executionInstance = executionInstanceFactory.create();
        executionInstance.setProcessInstanceId(processInstance.getInstanceId());
        processInstance.addExecution(executionInstance);

        String processInstanceId = processInstance.getInstanceId();
        processInstance.setStatus(InstanceStatus.running);

        String startActivityId = runtimeActivity.getModel().getId();

        ActivityInstance activityInstance = activityInstanceFactory.create();
        activityInstance.setProcessInstanceId(processInstanceId);
        activityInstance.setActivityId(startActivityId);

        //TODO 触发流程启动事件
//        this.fireEvent(PvmEventConstant.PROCESS_START.name(), executionContext);
        // 从开始节点开始执行

    }

}
