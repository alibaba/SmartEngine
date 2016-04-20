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
import com.alibaba.smart.framework.engine.runtime.RuntimeTransition;
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

    private Map<String, RuntimeTransition> incomeTransitions  = new ConcurrentHashMap<>();
    private Map<String, RuntimeTransition> outcomeTransitions = new ConcurrentHashMap<>();

    @Override
    public Message execute(InstanceContext context) {
        ExecutionInstance executionInstance = context.getCurrentExecution();
        Message activityExecuteMessage = new DefaultMessage();
        if (executionInstance.isSuspend()) {
            activityExecuteMessage.setSuspend(true);
            return activityExecuteMessage;
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
            Message invokerMessage = this.invokeActivity(event, context);
            if (invokerMessage.isSuspend()) {
                activityExecuteMessage.setSuspend(true);
                break;
            }
        }
        return activityExecuteMessage;
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

    //Getter & Setter
    public void addIncomeTransition(String transitionId, RuntimeTransition income) {
        this.incomeTransitions.put(transitionId, income);
    }

    public void addOutcomeTransition(String transitionId, RuntimeTransition outcome) {
        this.outcomeTransitions.put(transitionId, outcome);
    }
}
