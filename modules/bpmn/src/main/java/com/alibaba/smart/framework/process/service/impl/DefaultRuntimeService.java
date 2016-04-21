package com.alibaba.smart.framework.process.service.impl;

import com.alibaba.smart.framework.process.model.runtime.command.ExecutionInstanceSignalCommand;
import com.alibaba.smart.framework.process.model.runtime.command.ProcessInstanceStartCommand;
import com.alibaba.smart.framework.process.model.runtime.instance.ProcessInstance;
import com.alibaba.smart.framework.process.service.RuntimeService;


public class DefaultRuntimeService implements  RuntimeService{

    @Override
    public <T> ProcessInstance start(ProcessInstanceStartCommand<T> commond) {
        
        return null;
    }

    @Override
    public <T> ProcessInstance signal(ExecutionInstanceSignalCommand<T> commond) {
        
        return null;
    }
    
}
