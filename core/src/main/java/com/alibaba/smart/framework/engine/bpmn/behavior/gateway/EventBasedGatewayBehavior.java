package com.alibaba.smart.framework.engine.bpmn.behavior.gateway;

import java.util.Collection;

import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.EventBasedGateway;
import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.helper.CommonGatewayHelper;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.event.EventConstant;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = EventBasedGateway.class)
public class EventBasedGatewayBehavior extends AbstractActivityBehavior<EventBasedGateway> {

    public EventBasedGatewayBehavior() {
        super();
    }

    @Override
    public boolean enter(ExecutionContext context, PvmActivity pvmActivity) {
        // 1. Create ActivityInstance + ExecutionInstance
        super.enter(context, pvmActivity);

        // 2. Mark gateway's own execution instance as inactive
        context.getExecutionInstance().setActive(false);

        // 3. Fire activity start event
        fireEvent(context, pvmActivity, EventConstant.ACTIVITY_START);

        // 4. Fork: enter each outgoing branch (each target is an IntermediateCatchEvent that will pause)
        Collection<PvmTransition> values = pvmActivity.getOutcomeTransitions().values();
        CommonGatewayHelper.leaveAndConcurrentlyForkIfNeeded(context, pvmActivity, values);

        return true;
    }
}
