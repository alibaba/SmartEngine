package com.alibaba.smart.framework.engine.instance.factory.impl;

import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultExecutionInstance;
import com.alibaba.smart.framework.engine.instance.util.InstanceIdUtil;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.param.ExecutionParam;
import com.alibaba.smart.framework.engine.util.DateUtil;

/**
 * 默认执行实例工厂实现 Created by ettear on 16-4-20.
 */
public class DefaultExecutionInstanceFactory implements ExecutionInstanceFactory {


    @Override
    public ExecutionInstance create(ActivityInstance activityInstance) {
        DefaultExecutionInstance defaultExecutionInstance = new DefaultExecutionInstance();
        defaultExecutionInstance.setInstanceId(InstanceIdUtil.simpleId());
        defaultExecutionInstance.setActivityId(activityInstance.getActivityId());
        defaultExecutionInstance.setActivityInstanceId(activityInstance.getInstanceId());
        defaultExecutionInstance.setProcessInstanceId(activityInstance.getProcessInstanceId());
        defaultExecutionInstance.setProcessDefinitionIdAndVersion(activityInstance.getProcessDefinitionIdAndVersion());
        defaultExecutionInstance.setStartDate(DateUtil.getCurrentDate());

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
