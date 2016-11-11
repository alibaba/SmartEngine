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
 * @author 高海军 帝奇  2016.11.11   TODO 看下存在性
 * @author ettear 2016.04.13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractPvmActivity<M extends Activity> extends AbstractPvmInvocable<Activity> implements PvmActivity {


    //TODO 这个类现在看价值不大了,看看把各个 instance 赋值逻辑给统一起来。
    private Map<String, PvmTransition> incomeTransitions  = new ConcurrentHashMap<>();
    private Map<String, PvmTransition> outcomeTransitions = new ConcurrentHashMap<>();

//    @Override
//    public Message execute(ExecutionContext context) {
//        ExecutionInstance executionInstance = context.getCurrentExecution();
//
//        // 重置状态
//        executionInstance.setStatus(InstanceStatus.running);
//
//        Message activityExecuteMessage = this.doInternalExecute(context);
//
//        return activityExecuteMessage;
//
//    }
//
    
    @Override
    public Message signal(ExecutionContext context) {
        return null;
    }

//    protected abstract Message doInternalExecute(ExecutionContext context);


    // Getter & Setter
    public void addIncomeTransition(String transitionId, PvmTransition income) {
        this.incomeTransitions.put(transitionId, income);
    }

    public void addOutcomeTransition(String transitionId, PvmTransition outcome) {
        this.outcomeTransitions.put(transitionId, outcome);
    }
}
