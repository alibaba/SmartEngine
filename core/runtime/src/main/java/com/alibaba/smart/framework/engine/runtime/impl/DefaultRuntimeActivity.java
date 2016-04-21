package com.alibaba.smart.framework.engine.runtime.impl;

import com.alibaba.smart.framework.engine.assembly.Activity;
import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.instance.TaskInstance;
import com.alibaba.smart.framework.engine.invocation.AtomicOperationEvent;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.Message;
import com.alibaba.smart.framework.engine.invocation.impl.DefaultActivityTransitionSelectInvoker;
import com.alibaba.smart.framework.engine.invocation.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * DefaultRuntimeActivity
 * Created by ettear on 16-4-13.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultRuntimeActivity extends AbstractRuntimeActivity<Activity>
        implements RuntimeActivity {

    private final static List<String> EXECUTE_EVENTS = new ArrayList<>();

    static {
        EXECUTE_EVENTS.add(AtomicOperationEvent.ACTIVITY_START.name());
        EXECUTE_EVENTS.add(AtomicOperationEvent.ACTIVITY_EXECUTE.name());
        EXECUTE_EVENTS.add(AtomicOperationEvent.ACTIVITY_END.name());
    }

    @Override
    protected Message doExecute(InstanceContext context) {
        ExecutionInstance executionInstance = context.getCurrentExecution();
        ActivityInstance activityInstance = executionInstance.getActivity();
        TaskInstance taskInstance = activityInstance.getTask();

        if (null != taskInstance && InstanceStatus.completed != taskInstance.getStatus()) {//任务未完成，直接暂停
            activityInstance.setStatus(InstanceStatus.suspended);
            Message message = new DefaultMessage();
            message.setSuspend(true);
            return message;
        }

        //重置状态
        executionInstance.setStatus(InstanceStatus.running);

        Message activityExecuteMessage = new DefaultMessage();

        //恢复上次暂停时的执行器
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
        //从上次暂停点开始执行
        while (executeEventIterator.hasNext()) {
            String event = executeEventIterator.next();
            Message invokerMessage = this.invokeActivity(event, context);
            if (invokerMessage.isFault()) {
                invokerMessage.setSuspend(true);
                activityExecuteMessage.setFault(true);
            }
            if (invokerMessage.isSuspend()) {
                activityExecuteMessage.setSuspend(true);
                break;
            }
        }
        return activityExecuteMessage;
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
        return message;
    }
}
