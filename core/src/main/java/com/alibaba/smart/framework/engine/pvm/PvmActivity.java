package com.alibaba.smart.framework.engine.pvm;

import java.util.Map;

import com.alibaba.smart.framework.engine.behavior.ActivityBehavior;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.Activity;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface PvmActivity extends PvmElement<Activity> {

    Map<String, PvmTransition> getIncomeTransitions();

    Map<String, PvmTransition> getOutcomeTransitions();

    /**
     * 流程实例启动,节点进入 会调用此方法.
     *
     * @param context
     */
    void enter(ExecutionContext context);

    void execute(ExecutionContext context);

    void setBehavior(ActivityBehavior activityBehavior);

    ActivityBehavior getBehavior();

}
