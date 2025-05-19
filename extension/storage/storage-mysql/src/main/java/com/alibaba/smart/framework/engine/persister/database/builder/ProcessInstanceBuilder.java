package com.alibaba.smart.framework.engine.persister.database.builder;

import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.instance.impl.DefaultProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.database.entity.ProcessInstanceEntity;

/**
 * @author yanricheng@163.com
 * @date 2025/05/015
 */
public final class ProcessInstanceBuilder {


    public static ProcessInstanceEntity buildEntityFromInstance(ProcessInstance processInstance) {
        ProcessInstanceEntity processInstanceEntityToBePersisted = new ProcessInstanceEntity();
        processInstanceEntityToBePersisted.setId(Long.valueOf(processInstance.getInstanceId()));

        processInstanceEntityToBePersisted.setGmtCreate(processInstance.getStartTime());

        if (null != processInstance.getCompleteTime()) {
            processInstanceEntityToBePersisted.setGmtModified(processInstance.getCompleteTime());
        } else {
            processInstanceEntityToBePersisted.setGmtModified(processInstance.getStartTime());
        }

        String parentInstanceId = processInstance.getParentInstanceId();
        if (null != parentInstanceId) {
            processInstanceEntityToBePersisted.setParentProcessInstanceId(Long.valueOf(parentInstanceId));
        }

        String parentExecutionInstanceId = processInstance.getParentExecutionInstanceId();
        if (null != parentExecutionInstanceId) {
            processInstanceEntityToBePersisted.setParentExecutionInstanceId(Long.valueOf(parentExecutionInstanceId));
        }


        processInstanceEntityToBePersisted.setStatus(processInstance.getStatus().name());
        processInstanceEntityToBePersisted.setProcessDefinitionIdAndVersion(processInstance.getProcessDefinitionIdAndVersion());
        processInstanceEntityToBePersisted.setStartUserId(processInstance.getStartUserId());
        processInstanceEntityToBePersisted.setProcessDefinitionType(processInstance.getProcessDefinitionType());
        processInstanceEntityToBePersisted.setBizUniqueId(processInstance.getBizUniqueId());
        processInstanceEntityToBePersisted.setReason(processInstance.getReason());
        processInstanceEntityToBePersisted.setTitle(processInstance.getTitle());
        processInstanceEntityToBePersisted.setComment(processInstance.getComment());
        processInstanceEntityToBePersisted.setTag(processInstance.getTag());
        processInstanceEntityToBePersisted.setTenantId(processInstance.getTenantId());
        return processInstanceEntityToBePersisted;
    }


    public static void buildInstanceFromEntity(ProcessInstance processInstance, ProcessInstanceEntity processInstanceEntity) {
        processInstance.setProcessDefinitionIdAndVersion(processInstanceEntity.getProcessDefinitionIdAndVersion());

        buildCommon(processInstance, processInstanceEntity);

        processInstance.setInstanceId(processInstanceEntity.getId().toString());
        processInstance.setStartTime(processInstanceEntity.getGmtCreate());
        processInstance.setProcessDefinitionType(processInstanceEntity.getProcessDefinitionType());
        processInstance.setBizUniqueId(processInstanceEntity.getBizUniqueId());
        processInstance.setReason(processInstanceEntity.getReason());
        processInstance.setTitle(processInstanceEntity.getTitle());
        processInstance.setTag(processInstanceEntity.getTag());
        processInstance.setComment(processInstanceEntity.getComment());
        processInstance.setTenantId(processInstanceEntity.getTenantId());
    }

    public static void buildCommon(ProcessInstance processInstance, ProcessInstanceEntity processInstanceEntity) {
        Long parentProcessInstanceId = processInstanceEntity.getParentProcessInstanceId();
        if (null != parentProcessInstanceId) {
            processInstance.setParentInstanceId(parentProcessInstanceId.toString());
        }

        Long parentExecutionInstanceId = processInstanceEntity.getParentExecutionInstanceId();
        if (null != parentExecutionInstanceId) {
            processInstance.setParentExecutionInstanceId(parentExecutionInstanceId.toString());
        }
    }

    public static ProcessInstance buildProcessInstanceFromEntity(ProcessInstanceEntity processInstanceEntity) {
        if (processInstanceEntity == null) {
            return null;
        }
        ProcessInstance processInstance = new DefaultProcessInstance();

        buildCommon(processInstance, processInstanceEntity);

        InstanceStatus processStatus = InstanceStatus.valueOf(processInstanceEntity.getStatus());
        processInstance.setStatus(processStatus);
        processInstance.setStartTime(processInstanceEntity.getGmtCreate());
        String processDefinitionIdAndVersion = processInstanceEntity.getProcessDefinitionIdAndVersion();
        processInstance.setProcessDefinitionIdAndVersion(processDefinitionIdAndVersion);
        processInstance.setProcessDefinitionId(StringUtil.substringBefore(processDefinitionIdAndVersion, ":"));
        processInstance.setProcessDefinitionVersion(StringUtil.substringAfter(processDefinitionIdAndVersion, ":"));
        processInstance.setSuspend(InstanceStatus.suspended.equals(processStatus));
        processInstance.setStartUserId(processInstanceEntity.getStartUserId());
        processInstance.setProcessDefinitionType(processInstanceEntity.getProcessDefinitionType());
        processInstance.setReason(processInstanceEntity.getReason());
        processInstance.setBizUniqueId(processInstanceEntity.getBizUniqueId());

        //TUNE 还是叫做更新时间比较好一点,是否完成等 还是根据status 去判断.
        processInstance.setCompleteTime(processInstanceEntity.getGmtModified());
        processInstance.setInstanceId(processInstanceEntity.getId().toString());
        processInstance.setTag(processInstanceEntity.getTag());
        processInstance.setTitle(processInstanceEntity.getTitle());
        processInstance.setComment(processInstanceEntity.getComment());
        processInstance.setTenantId(processInstanceEntity.getTenantId());
        return processInstance;
    }
}
