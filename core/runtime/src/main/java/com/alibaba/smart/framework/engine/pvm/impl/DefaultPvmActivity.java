package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.impl.DefaultActivityTransitionSelectInvoker;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.invocation.message.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;

/**
 * DefaultRuntimeActivity Created by ettear on 16-4-13.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultPvmActivity extends AbstractPvmActivity<Activity> implements PvmActivity {

    private final static List<String> EXECUTE_EVENTS = new ArrayList<>();

    static {
        EXECUTE_EVENTS.add(PvmEventConstant.ACTIVITY_START.name());
        EXECUTE_EVENTS.add(PvmEventConstant.ACTIVITY_EXECUTE.name());
        EXECUTE_EVENTS.add(PvmEventConstant.ACTIVITY_END.name());
    }

    @Override
    protected Message doInternalExecute(InstanceContext context) {
        ExecutionInstance executionInstance = context.getCurrentExecution();
        ActivityInstance activityInstance = executionInstance.getActivity();
        TaskInstance taskInstance = activityInstance.getTask();

        if (null != taskInstance && InstanceStatus.completed != taskInstance.getStatus()) {// 任务未完成，直接暂停
            activityInstance.setStatus(InstanceStatus.suspended);
            Message message = new DefaultMessage();
            message.setSuspend(true);
            return message;
        }

        // 重置状态
        executionInstance.setStatus(InstanceStatus.running);

        Message activityExecuteMessage = new DefaultMessage();

        // 恢复上次暂停时的执行器
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
        // 从上次暂停点开始执行
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
        if (PvmEventConstant.ACTIVITY_TRANSITION_SELECT.name().equals(event)) {
            return new DefaultActivityTransitionSelectInvoker(this.getExtensionPointRegistry(), this);
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

    @Override
    public String toString() {
        return super.getModel() + ",id is " + super.getId();
    }

}
