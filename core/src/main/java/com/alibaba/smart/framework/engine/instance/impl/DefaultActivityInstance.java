package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/** 默认活动实例 Created by ettear on 16-4-19. */
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultActivityInstance extends AbstractLifeCycleInstance implements ActivityInstance {

    private static final long serialVersionUID = -3395004618384899752L;
    private String processDefinitionIdAndVersion;
    private String processDefinitionActivityId;
    private String processInstanceId;
    //    private List<TransitionInstance> incomeTransitions = new ArrayList<>();
    private List<ExecutionInstance> executionInstanceList;

    private String blockId;

    //    @Override
    //    public void addIncomeTransition(TransitionInstance transitionInstance) {
    //        this.incomeTransitions.add(transitionInstance);
    //    }

    @Override
    public String toString() {
        return this.getInstanceId() + ":" + this.getProcessDefinitionActivityId();
    }
}
