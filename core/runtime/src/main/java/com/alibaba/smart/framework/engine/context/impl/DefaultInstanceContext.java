package com.alibaba.smart.framework.engine.context.impl;

import com.alibaba.smart.framework.engine.context.Context;
import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import lombok.Data;

/**
 * DefaultInstanceContext
 * Created by ettear on 16-4-19.
 */
@Data
public class DefaultInstanceContext implements InstanceContext{
    private Context processContext;
    private ProcessInstance processInstance;
    private ExecutionInstance currentExecution;
    private ExecutionInstance parentExecution;

}
