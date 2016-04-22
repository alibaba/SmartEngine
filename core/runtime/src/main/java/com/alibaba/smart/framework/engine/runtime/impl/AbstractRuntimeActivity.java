package com.alibaba.smart.framework.engine.runtime.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.assembly.Activity;
import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.invocation.Message;
import com.alibaba.smart.framework.engine.invocation.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeTransition;

/**
 *
 * Created by ettear on 16-4-21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractRuntimeActivity<M extends Activity> extends AbstractRuntimeInvocable<Activity>
        implements RuntimeActivity {


    private Map<String, RuntimeTransition> incomeTransitions  = new ConcurrentHashMap<>();
    private Map<String, RuntimeTransition> outcomeTransitions = new ConcurrentHashMap<>();


    @Override
    public Message execute(InstanceContext context) {
        ExecutionInstance executionInstance = context.getCurrentExecution();
        ActivityInstance activityInstance = executionInstance.getActivity();

        if (InstanceStatus.completed == executionInstance.getStatus()) {//执行实例已完成，返回
            activityInstance.setStatus(InstanceStatus.completed);
            executionInstance.setStatus(InstanceStatus.running);
            return new DefaultMessage();
        }


        //重置状态
        executionInstance.setStatus(InstanceStatus.running);
        executionInstance.setFault(false);


        Message activityExecuteMessage=this.doExecute(context);
        //执行失败
        if (activityExecuteMessage.isFault()) {
            executionInstance.setFault(true);
        }

        if (activityExecuteMessage.isSuspend()) {
            //执行暂停
            activityInstance.setStatus(InstanceStatus.suspended);
            executionInstance.setStatus(InstanceStatus.suspended);
        } else {
            //正常执行完成
            activityInstance.setStatus(InstanceStatus.completed);
            executionInstance.setStatus(InstanceStatus.running);
        }
        return activityExecuteMessage;

    }
    protected abstract Message doExecute(InstanceContext context);

        @Override
    public boolean isStartActivity() {
        return this.getModel().isStartActivity();
    }


    //Getter & Setter
    public void addIncomeTransition(String transitionId, RuntimeTransition income) {
        this.incomeTransitions.put(transitionId, income);
    }

    public void addOutcomeTransition(String transitionId, RuntimeTransition outcome) {
        this.outcomeTransitions.put(transitionId, outcome);
    }
}
