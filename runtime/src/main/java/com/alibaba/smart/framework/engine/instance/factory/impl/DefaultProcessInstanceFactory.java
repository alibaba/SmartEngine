package com.alibaba.smart.framework.engine.instance.factory.impl;

import java.util.Map;

import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;

/**
 * 默认流程实例工厂实现 Created by ettear on 16-4-20.
 */
public class DefaultProcessInstanceFactory implements ProcessInstanceFactory {

    @Override
    public ProcessInstance create(ExecutionContext executionContext) {
        PvmProcessDefinition pvmProcessDefinition = executionContext.getPvmProcessDefinition();
        DefaultProcessInstance defaultProcessInstance = new DefaultProcessInstance();
        IdGenerator idGenerator = executionContext.getProcessEngineConfiguration().getIdGenerator();

        defaultProcessInstance.setInstanceId(idGenerator.getId());
        defaultProcessInstance.setStatus(InstanceStatus.running);
        defaultProcessInstance.setStartTime(DateUtil.getCurrentDate());

        defaultProcessInstance.setProcessDefinitionIdAndVersion(pvmProcessDefinition.getUri());
        defaultProcessInstance.setProcessDefinitionId(pvmProcessDefinition.getId());
        defaultProcessInstance.setProcessDefinitionVersion(pvmProcessDefinition.getVersion());

        Map<String, Object> request = executionContext.getRequest();
        if (null != request) {
            String startUserId = (String)request.get(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_START_USER_ID);
            defaultProcessInstance.setStartUserId(startUserId);

            String processDefinitionType = (String)request.get(RequestMapSpecialKeyConstant.PROCESS_DEFINITION_TYPE);
            defaultProcessInstance.setProcessDefinitionType(processDefinitionType);

            String bizUniqueId = (String)request.get(RequestMapSpecialKeyConstant.PROCESS_BIZ_UNIQUE_ID);
            defaultProcessInstance.setBizUniqueId(bizUniqueId);

            String title = (String)request.get(RequestMapSpecialKeyConstant.PROCESS_TITLE);
            defaultProcessInstance.setTitle(title);
        }

        return defaultProcessInstance;
    }

}
