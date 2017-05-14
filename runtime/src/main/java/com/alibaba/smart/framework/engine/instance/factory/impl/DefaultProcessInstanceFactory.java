package com.alibaba.smart.framework.engine.instance.factory.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.common.id.generator.IdGenerator;
import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;

/**
 * 默认流程实例工厂实现 Created by ettear on 16-4-20.
 */
public class DefaultProcessInstanceFactory implements ProcessInstanceFactory {

    @Override
    public ProcessInstance create( ExecutionContext executionContext) {
        PvmProcessDefinition pvmProcessDefinition = executionContext.getPvmProcessDefinition();
        DefaultProcessInstance defaultProcessInstance = new DefaultProcessInstance();
        IdGenerator idGenerator = executionContext.getProcessEngineConfiguration().getIdGenerator();

        defaultProcessInstance.setInstanceId(idGenerator.getId());
        defaultProcessInstance.setStatus(InstanceStatus.running);
        defaultProcessInstance.setStartDate(DateUtil.getCurrentDate());
        defaultProcessInstance.setProcessDefinitionIdAndVersion(pvmProcessDefinition.getUri());

        ProcessInstance parentProcessInstance = executionContext.getParentProcessInstance();
        if(null != parentProcessInstance){
            defaultProcessInstance.setParentInstanceId(parentProcessInstance.getInstanceId());

            List<ActivityInstance> parentNewActivityInstances = parentProcessInstance.getNewActivityInstances();

            ExecutionInstance parentExecutionInstance = parentNewActivityInstances.get(parentNewActivityInstances.size()-1).getExecutionInstance();

            //在子流程中,parentExecutionInstance 必须不能为空。 可以考虑加上断言。
            defaultProcessInstance.setParentExecutionInstanceId(parentExecutionInstance.getInstanceId());

        }

        return defaultProcessInstance;
    }




}
