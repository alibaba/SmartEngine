package com.alibaba.smart.framework.engine.instance.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.InstanceFact;

/**
 * DefaultExecutionInstance Created by ettear on 16-4-19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultExecutionInstance extends AbstractLifeCycleInstance implements ExecutionInstance {

    private String           processInstanceId;
    private InstanceFact     fact;
    private ActivityInstance activity;
    private boolean          fault;
}
