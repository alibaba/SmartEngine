package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import lombok.Data;

import java.util.List;

/**
 * Default Process Instance
 * Created by ettear on 16-4-12.
 */
@Data
public class DefaultProcessInstance implements ProcessInstance {
    /**
     * Id
     */
    private String                  id;
    /**
     * Running executions
     */
    private List<ExecutionInstance> executions;
}
