package com.alibaba.smart.framework.process.service;

import com.alibaba.smart.framework.process.model.runtime.command.ExecutionInstanceSignalCommand;
import com.alibaba.smart.framework.process.model.runtime.command.ProcessInstanceStartCommand;
import com.alibaba.smart.framework.process.model.runtime.instance.ProcessInstance;

public interface RuntimeService {

    public <T> ProcessInstance start(ProcessInstanceStartCommand<T> commond);
    
    public <T> ProcessInstance signal(ExecutionInstanceSignalCommand<T> commond);

}
