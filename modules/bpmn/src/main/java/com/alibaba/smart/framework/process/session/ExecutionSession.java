package com.alibaba.smart.framework.process.session;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.process.model.runtime.command.Command;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class ExecutionSession {

    private Command             command;

    // TODO 命名和含义考究下
    private PvmActivity     currentRuntimeActivity;
    private ProcessInstance     processInstance;
    private Map<String, Object> extensions = new ConcurrentHashMap<>();

}
