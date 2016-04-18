package com.alibaba.smart.framework.process.model.runtime.command;

import lombok.Data;

@Data
public class TaskInstanceCommonActionCommand<T> {

    private Long taskId;
    private Long userId;
    private T      request;
}
