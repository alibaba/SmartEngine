package com.alibaba.smart.framework.engine.instance.factory.impl;

import com.alibaba.smart.framework.engine.common.id.generator.IdGenerator;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultProcessInstance;
import com.alibaba.smart.framework.engine.instance.util.DefaultIdGenerator;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.common.util.DateUtil;

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

        return defaultProcessInstance;
    }




}
