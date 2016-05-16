package com.alibaba.smart.framework.process.model.runtime.command.impl;

import lombok.Data;

import com.alibaba.smart.framework.process.model.runtime.command.Command;

@Data
public class TaskInstanceCommonActionCommand<T> implements Command {

    private Long taskId;
    private Long userId;
    private T    request;

    @Override
    public void execute() {

    }
}
