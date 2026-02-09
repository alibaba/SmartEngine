package com.alibaba.smart.framework.engine.bpmn.behavior.event;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.event.IntermediateCatchEvent;
import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.EventBasedGateway;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = IntermediateCatchEvent.class)
public class IntermediateCatchEventBehavior extends AbstractActivityBehavior<IntermediateCatchEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntermediateCatchEventBehavior.class);

    public IntermediateCatchEventBehavior() {
        super();
    }

    @Override
    public boolean enter(ExecutionContext context, PvmActivity pvmActivity) {
        super.enter(context, pvmActivity);
        // Pause and wait for external signal
        return true;
    }

    @Override
    public void execute(ExecutionContext context, PvmActivity pvmActivity) {
        // When signaled, cancel sibling branches if upstream is EventBasedGateway
        cancelSiblingBranchesIfEventBased(context, pvmActivity);

        // Continue normal execution
        super.execute(context, pvmActivity);
    }

    private void cancelSiblingBranchesIfEventBased(ExecutionContext context, PvmActivity pvmActivity) {
        // a. Find the source node of the incoming transition
        Map<String, PvmTransition> incomeTransitions = pvmActivity.getIncomeTransitions();
        if (incomeTransitions.size() != 1) {
            return;
        }
        PvmActivity sourceActivity = incomeTransitions.values().iterator().next().getSource();

        // b. Check if source is an EventBasedGateway
        if (!(sourceActivity.getModel() instanceof EventBasedGateway)) {
            return;
        }

        // c. Collect all sibling catch event activity IDs
        Set<String> siblingActivityIds = new HashSet<String>();
        String currentActivityId = pvmActivity.getModel().getId();
        for (PvmTransition outgoing : sourceActivity.getOutcomeTransitions().values()) {
            String targetId = outgoing.getTarget().getModel().getId();
            if (!targetId.equals(currentActivityId)) {
                siblingActivityIds.add(targetId);
            }
        }

        if (siblingActivityIds.isEmpty()) {
            return;
        }

        // d. Find active execution instances and cancel those on sibling branches
        ProcessInstance processInstance = context.getProcessInstance();
        List<ExecutionInstance> activeExecutions = executionInstanceStorage.findActiveExecution(
            processInstance.getInstanceId(), processInstance.getTenantId(), processEngineConfiguration);

        for (ExecutionInstance ei : activeExecutions) {
            if (siblingActivityIds.contains(ei.getProcessDefinitionActivityId())) {
                LOGGER.debug("EventBasedGateway: canceling sibling execution instance {} on activity {}",
                    ei.getInstanceId(), ei.getProcessDefinitionActivityId());
                MarkDoneUtil.markDoneExecutionInstance(ei, executionInstanceStorage, processEngineConfiguration);
            }
        }
    }
}
