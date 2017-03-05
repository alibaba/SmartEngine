package com.alibaba.smart.framework.engine.pvm;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;

import java.util.Map;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface PvmActivity extends PvmInvocable<Activity> {

    Map<String, PvmTransition> getIncomeTransitions();

    Map<String, PvmTransition> getOutcomeTransitions();

    ActivityBehavior getActivityBehavior();

    void setActivityBehavior(ActivityBehavior activityBehavior);

    /**
     * 流程实例启动,节点进入 会调用此方法.
     *
     * @param context
     */
    void execute(ExecutionContext context);

    void leave(ExecutionContext context);

}
