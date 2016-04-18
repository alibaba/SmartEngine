package com.alibaba.smart.framework.process.model.runtime.command;

import lombok.Data;

@Data
public class ExecutionInstanceSignalCommand<T> {

    private Long executionId; ;
    private T    request;
}
