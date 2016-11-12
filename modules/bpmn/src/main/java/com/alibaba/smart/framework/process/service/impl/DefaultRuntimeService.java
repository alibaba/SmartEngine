package com.alibaba.smart.framework.process.service.impl;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.process.behavior.ActivityBehavior;
import com.alibaba.smart.framework.process.behavior.util.ActivityBehaviorRegister;
import com.alibaba.smart.framework.process.model.runtime.command.impl.ExecutionInstanceSignalCommand;
import com.alibaba.smart.framework.process.model.runtime.command.impl.ProcessInstanceStartCommand;
import com.alibaba.smart.framework.process.service.RuntimeService;
import com.alibaba.smart.framework.process.session.ExecutionSession;

public class DefaultRuntimeService implements RuntimeService {

    @Override
    public ProcessInstance start(ProcessInstanceStartCommand<?> command) {

        ExecutionSession executionSession = null;// ThreadLocalUtil.get();
        if (null == executionSession) {
            executionSession = new ExecutionSession();
        }
        executionSession.setCommand(command);
//        ThreadLocalUtil.set(executionSession);

        String processDefinitionId = command.getProcessDefinitionId();
        String version = command.getBusinessKey();

        // TODO 与流程定义的区别,改成PVMxxxx

        PvmProcessDefinition runtimeProcess =null;

        // TODO debug 时看数据结构有点怪
        PvmActivity startActivity = runtimeProcess.getStartActivity();
        executionSession.setCurrentRuntimeActivity(startActivity);

        String activityClassName = startActivity.getModel().getClass().getName();

        ActivityBehavior activityBehavior = ActivityBehaviorRegister.getActivityBehavior(activityClassName);

//        activityBehavior.execute();

//        ProcessInstance processInstance = ThreadLocalUtil.get().getProcessInstance();

        // FIXME 整体的clear session finally
        return null;
    }

    @Override
    public void signal(ExecutionInstanceSignalCommand<?> command) {

    }

}
