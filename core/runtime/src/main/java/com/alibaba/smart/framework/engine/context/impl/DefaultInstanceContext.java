package com.alibaba.smart.framework.engine.context.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import lombok.Data;

import java.util.Map;

/**
 * DefaultInstanceContext Created by ettear on 16-4-19.
 */
@Data
public class DefaultInstanceContext implements ExecutionContext {

    private ProcessInstance   processInstance;
    private ExecutionInstance currentExecution;
    private PvmProcessDefinition pvmProcessDefinition;
    
    private Map<String,Object > request;

    private boolean push = false;

    @Override
    public void changePushMod(boolean push) {
        this.push = push;
    }
}
