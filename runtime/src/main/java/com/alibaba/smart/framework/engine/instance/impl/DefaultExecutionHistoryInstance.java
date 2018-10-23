package com.alibaba.smart.framework.engine.instance.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.ExecutionHistoryInstance;
import com.alibaba.smart.framework.engine.model.instance.TransitionInstance;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ettear
 * Created by ettear on 11/10/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultExecutionHistoryInstance extends AbstractLifeCycleInstance implements ExecutionHistoryInstance {

    private String parentProcessInstanceId;

    private String parentExecutionInstanceId;

    private String processDefinitionIdAndVersion;

    private String processInstanceId;

    private String processDefinitionActivityId;

    private String activityInstanceId;

    private List<TransitionInstance> incomeTransitions = new ArrayList<TransitionInstance>();

    @Override
    public void addIncomeTransition(TransitionInstance transitionInstance) {
        this.incomeTransitions.add(transitionInstance);
    }
}
