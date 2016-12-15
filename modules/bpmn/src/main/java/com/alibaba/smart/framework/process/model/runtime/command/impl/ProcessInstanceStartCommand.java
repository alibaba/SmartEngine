package com.alibaba.smart.framework.process.model.runtime.command.impl;

import com.alibaba.smart.framework.process.model.runtime.command.Command;
import lombok.Data;

@Data
public class ProcessInstanceStartCommand<T> implements Command {

    private String processDefinitionId;
    private String version;
    private String businessKey;
    private T      request;

    @Override
    public void execute() {
    }
}
