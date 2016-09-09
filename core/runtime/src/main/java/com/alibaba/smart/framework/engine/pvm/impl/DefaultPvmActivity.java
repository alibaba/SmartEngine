package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.impl.DefaultActivityTransitionSelectInvoker;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.invocation.message.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;

/**
 * DefaultRuntimeActivity Created by ettear on 16-4-13.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultPvmActivity extends AbstractPvmActivity<Activity> implements PvmActivity {

    private final static List<Integer>  EXECUTE_EVENTS = new ArrayList<>();

    static {

        EXECUTE_EVENTS.add(PvmEventConstant.ACTIVITY_START.getCode());
        EXECUTE_EVENTS.add(PvmEventConstant.ACTIVITY_EXECUTE.getCode());
        EXECUTE_EVENTS.add(PvmEventConstant.ACTIVITY_END.getCode());
    }

    @Override
    protected Message doInternalExecute(ExecutionContext context) {
        ExecutionInstance executionInstance = context.getCurrentExecution();
        ActivityInstance activityInstance = executionInstance.getActivity();
        
        //TODO 不同节点,应该有不同的行为, 每个节点还是需要自己的行为. 不能解决差异性问题.  优先级:高
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

        dealStep(context, activityInstance, activityExecuteMessage);
        
        return activityExecuteMessage;
    }

    private void dealStep(ExecutionContext context, ActivityInstance activityInstance, Message activityExecuteMessage) {
        // 恢复上次暂停时的执行器
        String currentStep = activityInstance.getCurrentStep();
        Iterator<Integer> executeEventIterator = EXECUTE_EVENTS.iterator();
        if (StringUtils.isNotBlank(currentStep)) {
            while (executeEventIterator.hasNext()) {
                int  event = executeEventIterator.next();
                if (event == Integer.valueOf(currentStep)) {
                    break;
                }
            }
        }
        // 从上次暂停点开始执行
        while (executeEventIterator.hasNext()) {
            int event = executeEventIterator.next();
            String eventName = PvmEventConstant.getName(event);
            Message invokerMessage = this.invokeActivity(eventName, context);
            if (invokerMessage.isFault()) {
                invokerMessage.setSuspend(true);
                activityExecuteMessage.setFault(true);
            }
            if (invokerMessage.isSuspend()) {
                activityExecuteMessage.setSuspend(true);
                break;
            }
        }
    }

    @Override
    protected Invoker createDefaultInvoker(String event) {
        if (PvmEventConstant.ACTIVITY_TRANSITION_SELECT.name().equals(event)) {
            return new DefaultActivityTransitionSelectInvoker( this);
        } else {
            return super.createDefaultInvoker(event);
        }
    }

    private Message invokeActivity(String event, ExecutionContext context) {
        Message message = this.fireEvent(event, context);
        if (null == message) {
            message = new DefaultMessage();
        }
        return message;
    }



}
