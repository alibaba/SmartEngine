package com.alibaba.smart.framework.process.service;

import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.process.model.runtime.command.impl.ExecutionInstanceSignalCommand;
import com.alibaba.smart.framework.process.model.runtime.command.impl.ProcessInstanceStartCommand;

public interface RuntimeService {

    public   ProcessInstance start(ProcessInstanceStartCommand<?> command);
    
    public <T> void signal(ExecutionInstanceSignalCommand<T> command);

}
