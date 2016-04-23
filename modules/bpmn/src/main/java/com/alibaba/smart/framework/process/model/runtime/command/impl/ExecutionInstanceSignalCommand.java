package com.alibaba.smart.framework.process.model.runtime.command.impl;

import lombok.Data;

import com.alibaba.smart.framework.process.model.runtime.command.Command;

@Data
public class ExecutionInstanceSignalCommand<T> implements Command {

    private Long executionId; ;
    private T    request;

    @Override
    public void execute() {

    }
}
