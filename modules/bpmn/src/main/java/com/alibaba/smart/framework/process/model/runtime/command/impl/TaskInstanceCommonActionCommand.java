package com.alibaba.smart.framework.process.model.runtime.command.impl;

import com.alibaba.smart.framework.process.model.runtime.command.Command;
import lombok.Data;

@Data
public class TaskInstanceCommonActionCommand<T> implements Command {

    private Long taskId;
    private Long userId;
    private T    request;

    @Override
    public void execute() {

    }
}
