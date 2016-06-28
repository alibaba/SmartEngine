package com.alibaba.smart.framework.engine.instance.impl;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.TaskInstance;
import com.alibaba.smart.framework.engine.instance.TransitionInstance;

/**
 * 默认活动实例 Created by ettear on 16-4-19.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultActivityInstance extends AbstractLifeCycleInstance implements ActivityInstance {

    private static final long serialVersionUID = -3395004618384899752L;
    private String                   activityId;
    private String                   processInstanceId;
    private List<TransitionInstance> incomeTransitions = new ArrayList<>();
    private String                   currentStep;
    private TaskInstance             task;

    @Override
    public void addIncomeTransition(TransitionInstance transitionInstance) {
        this.incomeTransitions.add(transitionInstance);
    }
}
