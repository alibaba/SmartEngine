package com.alibaba.smart.framework.engine.persister.database.builder;


import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTransitionInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.TransitionInstance;
import com.alibaba.smart.framework.engine.persister.database.entity.ExecutionInstanceEntity;

import java.util.Date;

/**
 * @author yanricheng@163.com
 * @date 2025/05/015
 */
public final class ExecutionInstanceBuilder {


    public static ExecutionInstanceEntity buildExecutionInstanceEntity(ExecutionInstance executionInstance) {
        ExecutionInstanceEntity executionInstanceEntity = new ExecutionInstanceEntity();
        executionInstanceEntity.setId(Long.valueOf(executionInstance.getInstanceId()));
        executionInstanceEntity.setActive(executionInstance.isActive());
        executionInstanceEntity.setProcessDefinitionIdAndVersion(executionInstance.getProcessDefinitionIdAndVersion());
        executionInstanceEntity.setProcessInstanceId(Long.valueOf(executionInstance.getProcessInstanceId()));
        executionInstanceEntity.setActivityInstanceId(Long.valueOf(executionInstance.getActivityInstanceId()));
        executionInstanceEntity.setProcessDefinitionActivityId(executionInstance.getProcessDefinitionActivityId());

        String blockId = executionInstance.getBlockId();
        if (StringUtil.isNotEmpty(blockId)) {
            executionInstanceEntity.setBlockId(Long.valueOf(blockId));
        }

        Date currentDate = DateUtil.getCurrentDate();
        executionInstanceEntity.setGmtCreate(currentDate);
        executionInstanceEntity.setGmtModified(currentDate);
        executionInstanceEntity.setTenantId(executionInstance.getTenantId());

        //TransitionInstance incomeTransition=executionInstance.getIncomeTransition();
        //if(null!=incomeTransition){
        //    executionInstanceEntity.setIncomeTransitionId(incomeTransition.getTransitionId());
        //    executionInstanceEntity.setIncomeActivityInstanceId(Long.valueOf(incomeTransition.getSourceActivityInstanceId()));
        //
        //
        //}
        return executionInstanceEntity;
    }

    public static ExecutionInstance buildExecutionInstance(ExecutionInstance executionInstance, ExecutionInstanceEntity executionInstanceEntity) {
        executionInstance.setInstanceId(executionInstanceEntity.getId().toString());
        executionInstance.setProcessDefinitionIdAndVersion(executionInstanceEntity.getProcessDefinitionIdAndVersion());
        executionInstance.setProcessInstanceId(executionInstanceEntity.getProcessInstanceId().toString());
        executionInstance.setActivityInstanceId(executionInstanceEntity.getActivityInstanceId().toString());
        executionInstance.setProcessDefinitionActivityId(executionInstanceEntity.getProcessDefinitionActivityId());
        executionInstance.setActive(executionInstanceEntity.isActive());
        executionInstance.setStartTime(executionInstanceEntity.getGmtCreate());
        executionInstance.setCompleteTime(executionInstanceEntity.getGmtModified());
        executionInstance.setTenantId(executionInstanceEntity.getTenantId());

        Long blockId = executionInstanceEntity.getBlockId();
        if (null != blockId) {
            executionInstance.setBlockId(blockId + "");
        }

        String incomeTransitionId = executionInstanceEntity.getIncomeTransitionId();
        Long incomeActivityInstanceId = executionInstanceEntity.getIncomeActivityInstanceId();
        if (null != incomeTransitionId || null != incomeActivityInstanceId) {
            TransitionInstance incomeTransition = new DefaultTransitionInstance();
            incomeTransition.setTransitionId(incomeTransitionId);
            incomeTransition.setSourceActivityInstanceId(incomeActivityInstanceId.toString());
            //executionInstance.setIncomeTransition(incomeTransition);
        }
        return executionInstance;
    }
}
