package com.alibaba.smart.framework.process.service;

import com.alibaba.smart.framework.process.model.runtime.command.ProcessInstanceStartCommand;
import com.alibaba.smart.framework.process.model.runtime.instance.ProcessInstance;

public interface RuntimeService {

    public <T> ProcessInstance startProcessInstance(ProcessInstanceStartCommand<T> commond);
}
