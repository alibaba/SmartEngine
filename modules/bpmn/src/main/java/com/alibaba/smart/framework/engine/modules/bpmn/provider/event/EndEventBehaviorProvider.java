package com.alibaba.smart.framework.engine.modules.bpmn.provider.event;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.EndEvent;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.process.AbstractBpmnActivityBehaviorProvider;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.util.DateUtil;

public class EndEventBehaviorProvider extends AbstractBpmnActivityBehaviorProvider<EndEvent> implements ActivityBehavior<EndEvent> {

    public EndEventBehaviorProvider(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    @Override
    public void execute(PvmActivity runtimeActivity, ExecutionContext context) {
      ProcessInstance processInstance=  context.getProcessInstance();
        processInstance.setStatus(InstanceStatus.completed);
        processInstance.setCompleteDate(DateUtil.getCurrentDate());

        context.setNeedPause(true);
    }
}
