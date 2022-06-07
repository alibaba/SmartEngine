package com.alibaba.smart.framework.engine.instance.factory.impl;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;

/**
 * 默认执行实例工厂实现 Created by ettear on 16-4-20.
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = ExecutionInstanceFactory.class)

public class DefaultExecutionInstanceFactory implements ExecutionInstanceFactory {


    @Override
    public ExecutionInstance create(ActivityInstance activityInstance,ExecutionContext executionContext) {
        DefaultExecutionInstance defaultExecutionInstance = new DefaultExecutionInstance();
        IdGenerator idGenerator = executionContext.getProcessEngineConfiguration().getIdGenerator();
        idGenerator.generate(defaultExecutionInstance);
        defaultExecutionInstance.setProcessDefinitionActivityId(activityInstance.getProcessDefinitionActivityId());
        defaultExecutionInstance.setActivityInstanceId(activityInstance.getInstanceId());
        defaultExecutionInstance.setProcessInstanceId(activityInstance.getProcessInstanceId());
        defaultExecutionInstance.setProcessDefinitionIdAndVersion(activityInstance.getProcessDefinitionIdAndVersion());
        defaultExecutionInstance.setStartTime(DateUtil.getCurrentDate());
        defaultExecutionInstance.setActive(true);

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
