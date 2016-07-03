package com.alibaba.smart.framework.process.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Data;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.process.model.runtime.command.Command;

@Data
public class ExecutionSession {

    private Command             command;

    // TODO 命名和含义考究下
    private PvmActivity     currentRuntimeActivity;
    private ProcessInstance     processInstance;
    private Map<String, Object> extensions = new ConcurrentHashMap<>();

}
