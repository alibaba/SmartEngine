package com.alibaba.smart.framework.process.service.impl;

import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;
import com.alibaba.smart.framework.process.context.ProcessContext;
import com.alibaba.smart.framework.process.context.ProcessContextHolder;
import com.alibaba.smart.framework.process.model.runtime.command.impl.ExecutionInstanceSignalCommand;
import com.alibaba.smart.framework.process.model.runtime.command.impl.ProcessInstanceStartCommand;
import com.alibaba.smart.framework.process.model.runtime.instance.ProcessInstance;
import com.alibaba.smart.framework.process.service.RuntimeService;

public class DefaultRuntimeService implements RuntimeService {

    @Override
    public <T> ProcessInstance start(ProcessInstanceStartCommand<T> command) {

        String processDefinitionId = command.getProcessDefinitionId();
        String version = command.getBusinessKey();
        ProcessContext processContext = ProcessContextHolder.get();
        
        //TODO 与流程定义的区别
        RuntimeProcess runtimeProcess =       processContext.get(processDefinitionId, version);
        RuntimeActivity startActivity =    runtimeProcess.getStartActivity();
        
        
//        runtimeProcess.getModel().get

        return null;
    }

    @Override
    public <T> void signal(ExecutionInstanceSignalCommand<T> command) {

    }

}
