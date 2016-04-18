package com.alibaba.smart.framework.process.model.runtime.command;

import lombok.Data;

@Data
public class TaskInstanceCreateCommand<T> {

    private Long taskId;
    private Long userId;
    private T    request;
}
