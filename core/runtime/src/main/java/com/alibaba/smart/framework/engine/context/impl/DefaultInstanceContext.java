package com.alibaba.smart.framework.engine.context.impl;

import lombok.Data;

import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.model.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.ProcessInstance;

/**
 * DefaultInstanceContext Created by ettear on 16-4-19.
 */
@Data
public class DefaultInstanceContext implements InstanceContext {

    private ProcessInstance   processInstance;
    private ExecutionInstance currentExecution;

}
