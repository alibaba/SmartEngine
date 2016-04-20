package com.alibaba.smart.framework.engine.runtime.impl;

import com.alibaba.smart.framework.engine.assembly.Activity;
import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.invocation.AtomicOperationEvent;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.Message;
import com.alibaba.smart.framework.engine.invocation.impl.DefaultActivityTransitionSelectInvoker;
import com.alibaba.smart.framework.engine.invocation.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeSequenceFlow;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DefaultRuntimeActivity
 * Created by ettear on 16-4-13.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultRuntimeActivity<M extends Activity> extends AbstractRuntimeInvocable<M>
        implements RuntimeActivity<M> {

    private final static List<String> EXECUTE_EVENTS = new ArrayList<>();

    static {
        EXECUTE_EVENTS.add(AtomicOperationEvent.ACTIVITY_START.name());
        EXECUTE_EVENTS.add(AtomicOperationEvent.ACTIVITY_EXECUTE.name());
        EXECUTE_EVENTS.add(AtomicOperationEvent.ACTIVITY_END.name());

    }

    private Map<String, RuntimeSequenceFlow> incomeSequenceFlows  = new ConcurrentHashMap<>();
    private Map<String, RuntimeSequenceFlow> outcomeSequenceFlows = new ConcurrentHashMap<>();

    public void addIncomeSequenceFlows(String sequenceId, RuntimeSequenceFlow income) {
        incomeSequenceFlows.put(sequenceId, income);
    }

    public void addOutcomeSequenceFlows(String sequenceId, RuntimeSequenceFlow outcome) {
        outcomeSequenceFlows.put(sequenceId, outcome);
    }

    @Override
    public boolean execute(InstanceContext context) {
        ExecutionInstance executionInstance = context.getCurrentExecution();
        if (executionInstance.isSuspend()) {
            return true;
        }
        ActivityInstance activityInstance = executionInstance.getActivity();
        String currentStep = activityInstance.getCurrentStep();

        Iterator<String> executeEventIterator = EXECUTE_EVENTS.iterator();
        if (StringUtils.isNotBlank(currentStep)) {
            while (executeEventIterator.hasNext()) {
                String event = executeEventIterator.next();
                if (StringUtils.equals(event, currentStep)) {
                    break;
                }
            }
        }
        while (executeEventIterator.hasNext()) {
            String event = executeEventIterator.next();
            Message message = this.invokeActivity(event, context);
            if (message.isSuspend()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isStartActivity() {
        return this.getModel().isStartActivity();
    }

    @Override
    protected Invoker createDefaultInvoker(String event) {
        if (AtomicOperationEvent.ACTIVITY_TRANSITION_SELECT.name().equals(event)) {
            DefaultActivityTransitionSelectInvoker invoker = new DefaultActivityTransitionSelectInvoker();
            invoker.setRuntimeActivity(this);
            invoker.setExtensionPointRegistry(this.getExtensionPointRegistry());
            return invoker;
        } else {
            return super.createDefaultInvoker(event);
        }
    }

    private Message invokeActivity(String event, InstanceContext context) {
        Message message = this.invoke(event, context);
        if (null == message) {
            message = new DefaultMessage();
        }
        ExecutionInstance executionInstance = context.getCurrentExecution();
        if (message.isSuspend()) {
            executionInstance.setSuspend(true);
        }
        if (message.isFault()) {
            message.setSuspend(true);
            executionInstance.setSuspend(true);
            executionInstance.setFault(true);
        }
        return message;
    }
}
