package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 默认活动实例 Created by ettear on 16-4-19.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultActivityInstance extends AbstractLifeCycleInstance implements ActivityInstance {

    private static final long serialVersionUID = -3395004618384899752L;
    private String processDefinitionIdAndVersion;
    private String activityId;
    private Long processInstanceId;
//    private List<TransitionInstance> incomeTransitions = new ArrayList<>();
    private ExecutionInstance executionInstance;

    private Long blockId;


//    @Override
//    public void addIncomeTransition(TransitionInstance transitionInstance) {
//        this.incomeTransitions.add(transitionInstance);
//    }


}
