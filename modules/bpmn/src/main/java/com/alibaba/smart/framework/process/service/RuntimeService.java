package com.alibaba.smart.framework.process.service;

import com.alibaba.smart.framework.process.model.runtime.command.impl.ExecutionInstanceSignalCommand;
import com.alibaba.smart.framework.process.model.runtime.command.impl.ProcessInstanceStartCommand;
import com.alibaba.smart.framework.process.model.runtime.instance.ProcessInstance;

public interface RuntimeService {

    public <T> ProcessInstance start(ProcessInstanceStartCommand<T> command);
    
    public <T> void signal(ExecutionInstanceSignalCommand<T> command);

}
