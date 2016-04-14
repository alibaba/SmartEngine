package com.alibaba.smart.framework.process.model.runtime.command;

import lombok.Data;

@Data
public class ProcessInstanceStartCommand<T> {

    private String processDefinitionId;
    private String businessKey;
    private T      request;
}
