package com.alibaba.smart.framework.process.model.runtime.command;

import lombok.Data;

@Data
public class ProcessInstanceSignalCommand<T> {

    private Long executionId; ;
    private T    request;
}
