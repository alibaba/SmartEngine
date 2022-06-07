package com.alibaba.smart.framework.engine.instance.factory.impl;

import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.common.util.IdAndVersionUtil;
import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.util.ObjUtil;

/**
 * 默认流程实例工厂实现 Created by ettear on 16-4-20.
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = ProcessInstanceFactory.class)

public class DefaultProcessInstanceFactory implements ProcessInstanceFactory {

    @Override
    public ProcessInstance create( ProcessEngineConfiguration processEngineConfiguration,String processDefinitionId, String processDefinitionVersion, Map<String, Object> request) {
        DefaultProcessInstance defaultProcessInstance = new DefaultProcessInstance();
        IdGenerator idGenerator = processEngineConfiguration.getIdGenerator();

        idGenerator.generate(defaultProcessInstance);
        defaultProcessInstance.setStatus(InstanceStatus.running);
        defaultProcessInstance.setStartTime(DateUtil.getCurrentDate());

        defaultProcessInstance.setProcessDefinitionIdAndVersion(IdAndVersionUtil.buildProcessDefinitionKey(processDefinitionId,processDefinitionVersion));
        defaultProcessInstance.setProcessDefinitionId(processDefinitionId);
        defaultProcessInstance.setProcessDefinitionVersion(processDefinitionVersion);

        if (null != request) {
            String startUserId = ObjUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_START_USER_ID));
            defaultProcessInstance.setStartUserId(startUserId);

            String processDefinitionType = ObjUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.PROCESS_DEFINITION_TYPE));
            defaultProcessInstance.setProcessDefinitionType(processDefinitionType);

            String bizUniqueId = ObjUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.PROCESS_BIZ_UNIQUE_ID));
            defaultProcessInstance.setBizUniqueId(bizUniqueId);

            String title = ObjUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.PROCESS_TITLE));
            defaultProcessInstance.setTitle(title);

            String comment = ObjUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_COMMENT));
            defaultProcessInstance.setComment(comment);
        }

        return defaultProcessInstance;
    }

    @Override
    public ProcessInstance createChild(ProcessEngineConfiguration processEngineConfiguration,
                                       String processDefinitionId, String processDefinitionVersion,
                                       Map<String, Object> request, String parentInstanceId,
                                       String parentExecutionInstanceId) {
        ProcessInstance childProcessInstance = this.create(processEngineConfiguration,   processDefinitionId,processDefinitionVersion,
            request);
        childProcessInstance.setParentInstanceId(parentInstanceId);
        childProcessInstance.setParentExecutionInstanceId(parentExecutionInstanceId);

        return childProcessInstance;
    }

}
