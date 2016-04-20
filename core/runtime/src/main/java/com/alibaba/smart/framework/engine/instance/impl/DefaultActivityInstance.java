package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.TaskInstance;
import com.alibaba.smart.framework.engine.instance.TransitionInstance;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by ettear on 16-4-19.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultActivityInstance extends AbstractInstance implements ActivityInstance {
    private String activityId;
    private String processInstanceId;
    private List<TransitionInstance> incomeTransitions;
    private String currentStep;
    private TaskInstance task;
}
