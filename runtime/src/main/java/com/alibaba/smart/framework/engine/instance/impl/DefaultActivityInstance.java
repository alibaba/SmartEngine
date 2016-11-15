package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.model.instance.*;
import com.alibaba.smart.framework.engine.param.ActivityParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认活动实例 Created by ettear on 16-4-19.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultActivityInstance extends AbstractLifeCycleInstance implements ActivityInstance, DatabaseMod<DefaultActivityInstance, ActivityParam> {

    private static final long serialVersionUID = -3395004618384899752L;
    private String activityId;
    private String processInstanceId;
    private List<TransitionInstance> incomeTransitions = new ArrayList<>();
    private String currentStep;
    private TaskInstance taskInstance;
    private ExecutionInstance executionInstance;

    @Override
    public void addIncomeTransition(TransitionInstance transitionInstance) {
        this.incomeTransitions.add(transitionInstance);
    }

    @Override
    public String toDatabase() {
        return null;
    }

    @Override
    public DefaultActivityInstance getModel(ActivityParam param) {
        this.setProcessInstanceId(param.getProcessId());
        this.setActivityId(param.getActivityId());
        this.setCurrentStep(param.getCurrentStep());
        return this;
    }
}
