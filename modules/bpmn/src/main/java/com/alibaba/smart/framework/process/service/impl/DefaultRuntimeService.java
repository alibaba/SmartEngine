package com.alibaba.smart.framework.process.service.impl;

import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcess;
import com.alibaba.smart.framework.process.behavior.ActivityBehavior;
import com.alibaba.smart.framework.process.behavior.util.ActivityBehaviorRegister;
import com.alibaba.smart.framework.process.context.ProcessContext;
import com.alibaba.smart.framework.process.context.ProcessContextHolder;
import com.alibaba.smart.framework.process.model.runtime.command.impl.ExecutionInstanceSignalCommand;
import com.alibaba.smart.framework.process.model.runtime.command.impl.ProcessInstanceStartCommand;
import com.alibaba.smart.framework.process.service.RuntimeService;
import com.alibaba.smart.framework.process.session.ExecutionSession;
import com.alibaba.smart.framework.process.session.util.ThreadLocalExecutionSessionUtil;

public class DefaultRuntimeService implements RuntimeService {

    @Override
    public ProcessInstance start(ProcessInstanceStartCommand<?> command) {

        ExecutionSession executionSession = ThreadLocalExecutionSessionUtil.get();
        if (null == executionSession) {
            executionSession = new ExecutionSession();
        }
        executionSession.setCommand(command);
        ThreadLocalExecutionSessionUtil.set(executionSession);

        String processDefinitionId = command.getProcessDefinitionId();
        String version = command.getBusinessKey();
        ProcessContext processContext = ProcessContextHolder.get();

        // TODO 与流程定义的区别,改成PVMxxxx

        PvmProcess runtimeProcess = processContext.get(processDefinitionId, version);

        // TODO debug 时看数据结构有点怪
        PvmActivity startActivity = runtimeProcess.getStartActivity();
        executionSession.setCurrentRuntimeActivity(startActivity);

        String activityClassName = startActivity.getModel().getClass().getName();

        ActivityBehavior activityBehavior = ActivityBehaviorRegister.getActivityBehavior(activityClassName);

        activityBehavior.execute();

        ProcessInstance processInstance = ThreadLocalExecutionSessionUtil.get().getProcessInstance();

        // FIXME 整体的clear session finally
        return processInstance;
    }

    @Override
    public void signal(ExecutionInstanceSignalCommand<?> command) {

    }

}
