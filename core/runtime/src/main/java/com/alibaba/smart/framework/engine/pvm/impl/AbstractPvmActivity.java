package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.invocation.message.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * Created by ettear on 16-4-21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractPvmActivity<M extends Activity> extends AbstractPvmInvocable<Activity> implements PvmActivity {

    private Map<String, PvmTransition> incomeTransitions  = new ConcurrentHashMap<>();
    private Map<String, PvmTransition> outcomeTransitions = new ConcurrentHashMap<>();

    @Override
    public Message execute(ExecutionContext context) {
        ExecutionInstance executionInstance = context.getCurrentExecution();
        ActivityInstance activityInstance = executionInstance.getActivity();

        if (InstanceStatus.completed == executionInstance.getStatus()) {// 执行实例已完成，返回
            activityInstance.setStatus(InstanceStatus.completed);
            executionInstance.setStatus(InstanceStatus.running);
            return new DefaultMessage();
        }

        // 重置状态
        executionInstance.setStatus(InstanceStatus.running);
        executionInstance.setFault(false);

        Message activityExecuteMessage = this.doInternalExecute(context);
        // 执行失败
        if (activityExecuteMessage.isFault()) {
            executionInstance.setFault(true);
        }

        if (activityExecuteMessage.isSuspend()) {
            // 执行暂停
            activityInstance.setStatus(InstanceStatus.suspended);
            executionInstance.setStatus(InstanceStatus.suspended);
        } else {
            // 正常执行完成
            activityInstance.setStatus(InstanceStatus.completed);
            executionInstance.setStatus(InstanceStatus.running);
        }
        return activityExecuteMessage;

    }
    
    
    @Override
    public Message signal(ExecutionContext context) {
        return null;
    }

    protected abstract Message doInternalExecute(ExecutionContext context);


    // Getter & Setter
    public void addIncomeTransition(String transitionId, PvmTransition income) {
        this.incomeTransitions.put(transitionId, income);
    }

    public void addOutcomeTransition(String transitionId, PvmTransition outcome) {
        this.outcomeTransitions.put(transitionId, outcome);
    }
}
