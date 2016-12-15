package com.alibaba.smart.framework.process.model.runtime.command.impl;

import com.alibaba.smart.framework.process.model.runtime.command.Command;
import lombok.Data;

@Data
public class ExecutionInstanceSignalCommand<T> implements Command {

    private Long executionId; ;
    private T    request;

    @Override
    public void execute() {

    }
}
