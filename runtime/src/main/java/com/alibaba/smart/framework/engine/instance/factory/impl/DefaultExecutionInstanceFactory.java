package com.alibaba.smart.framework.engine.instance.factory.impl;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;

/**
 * 默认执行实例工厂实现 Created by ettear on 16-4-20.
 */
public class DefaultExecutionInstanceFactory implements ExecutionInstanceFactory {


    @Override
    public ExecutionInstance create(ActivityInstance activityInstance,ExecutionContext executionContext) {
        DefaultExecutionInstance defaultExecutionInstance = new DefaultExecutionInstance();
        IdGenerator idGenerator = executionContext.getProcessEngineConfiguration().getIdGenerator();
        defaultExecutionInstance.setInstanceId(idGenerator.getId());
        defaultExecutionInstance.setProcessDefinitionActivityId(activityInstance.getProcessDefinitionActivityId());
        defaultExecutionInstance.setActivityInstanceId(activityInstance.getInstanceId());
        defaultExecutionInstance.setProcessInstanceId(activityInstance.getProcessInstanceId());
        defaultExecutionInstance.setProcessDefinitionIdAndVersion(activityInstance.getProcessDefinitionIdAndVersion());
        defaultExecutionInstance.setStartTime(DateUtil.getCurrentDate());
        defaultExecutionInstance.setActive(true);
        defaultExecutionInstance.setStatus(InstanceStatus.running);

        return defaultExecutionInstance;
    }

//    @Override
//    public ExecutionInstance recovery(ExecutionParam executionParam) {
//        DefaultExecutionInstance executionInstance = new DefaultExecutionInstance();
//        executionInstance.getModel(executionParam);
//        return executionInstance;
//    }

//    @Override
//    public String toDatabase(ExecutionInstance executionInstance) {
//        if (executionInstance instanceof DefaultExecutionInstance) {
//            return ((DefaultExecutionInstance) executionInstance).toDatabase();
//        }
//        return null;
//    }
}
