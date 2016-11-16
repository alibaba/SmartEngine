package com.alibaba.smart.framework.engine.modules.bpmn.provider.event;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.EndEvent;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.util.DateUtil;

public class EndEventBehavior extends AbstractActivityBehavior<EndEvent> implements ActivityBehavior<EndEvent> {

    public EndEventBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    @Override
    public void buildInstanceRelationShip(PvmActivity runtimeActivity, ExecutionContext context) {
        ProcessInstance processInstance = context.getProcessInstance();
        processInstance.setStatus(InstanceStatus.completed);
        processInstance.setCompleteDate(DateUtil.getCurrentDate());

    }

    @Override
    public boolean needSuspend() {
        return true;
    }
}
