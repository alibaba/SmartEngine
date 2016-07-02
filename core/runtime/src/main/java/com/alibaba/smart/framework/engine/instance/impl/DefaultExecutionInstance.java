package com.alibaba.smart.framework.engine.instance.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.model.ActivityInstance;
import com.alibaba.smart.framework.engine.model.ExecutionInstance;

/**
 * DefaultExecutionInstance Created by ettear on 16-4-19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultExecutionInstance extends AbstractLifeCycleInstance implements ExecutionInstance {

    private static final long serialVersionUID = 2323809298485587299L;
    private String           processInstanceId;
//    private InstanceFact     fact;
    private ActivityInstance activity;
    private boolean          fault;
}
