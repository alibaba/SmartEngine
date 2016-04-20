package com.alibaba.smart.framework.engine.runtime.impl;

import com.alibaba.smart.framework.engine.assembly.Activity;
import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.invocation.AtomicOperationEvent;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.Message;
import com.alibaba.smart.framework.engine.invocation.impl.DefaultActivityTransitionSelectInvoker;
import com.alibaba.smart.framework.engine.invocation.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeSequenceFlow;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DefaultRuntimeActivity
 * Created by ettear on 16-4-13.
 */
@Data
public class DefaultRuntimeActivity<M extends Activity> extends AbstractRuntimeInvocable<M>
        implements RuntimeActivity<M> {

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
        Message startMessage = this.invokeActivity(AtomicOperationEvent.ACTIVITY_START.name(),
                                                   context);
        if (startMessage.isSuspend()) {
            return true;
        }
        Message executeMessage = this.invokeActivity(AtomicOperationEvent.ACTIVITY_EXECUTE.name(),
                                                     context);
        if (executeMessage.isSuspend()) {
            return true;
        }
        Message endMessage = this.invokeActivity(AtomicOperationEvent.ACTIVITY_END.name(), context);
        if (endMessage.isSuspend()) {
            return true;
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
