package com.alibaba.smart.framework.engine.instance.factory.impl;

import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultProcessInstance;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * 默认流程实例工厂实现 Created by ettear on 16-4-20.
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = ProcessInstanceFactory.class)

public class DefaultProcessInstanceFactory implements ProcessInstanceFactory {

    @Override
    public ProcessInstance create(ExecutionContext executionContext) {
        ProcessDefinition   processDefinition = executionContext.getProcessDefinition();
        DefaultProcessInstance defaultProcessInstance = new DefaultProcessInstance();
        IdGenerator idGenerator = executionContext.getProcessEngineConfiguration().getIdGenerator();

        defaultProcessInstance.setInstanceId(idGenerator.getId());
        defaultProcessInstance.setStatus(InstanceStatus.running);
        defaultProcessInstance.setStartTime(DateUtil.getCurrentDate());

        defaultProcessInstance.setProcessDefinitionIdAndVersion(processDefinition.getIdAndVersion());
        defaultProcessInstance.setProcessDefinitionId(processDefinition.getId());
        defaultProcessInstance.setProcessDefinitionVersion(processDefinition.getVersion());

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

            String comment = (String)request.get(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_COMMENT);
            defaultProcessInstance.setComment(comment);
        }

        return defaultProcessInstance;
    }

}
