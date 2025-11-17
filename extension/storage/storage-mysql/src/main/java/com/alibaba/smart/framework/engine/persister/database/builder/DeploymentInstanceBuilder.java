package com.alibaba.smart.framework.engine.persister.database.builder;


import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.instance.impl.DefaultDeploymentInstance;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.persister.database.entity.DeploymentInstanceEntity;

import java.util.Date;

/**
 * @author yanricheng@163.com
 * @date 2025/05/015
 */
public final class DeploymentInstanceBuilder {

    public static DeploymentInstance convertByEntity(DeploymentInstanceEntity entity) {
        DeploymentInstance deploymentInstance = new DefaultDeploymentInstance();

        deploymentInstance.setInstanceId(entity.getId().toString());
        deploymentInstance.setLogicStatus(entity.getLogicStatus());
        deploymentInstance.setDeploymentStatus(entity.getDeploymentStatus());
        deploymentInstance.setDeploymentUserId(entity.getDeploymentUserId());
        deploymentInstance.setProcessDefinitionContent(entity.getProcessDefinitionContent());
        deploymentInstance.setProcessDefinitionDesc(entity.getProcessDefinitionDesc());
        deploymentInstance.setProcessDefinitionName(entity.getProcessDefinitionName());
        deploymentInstance.setProcessDefinitionType(entity.getProcessDefinitionType());
        deploymentInstance.setProcessDefinitionCode(entity.getProcessDefinitionCode());
        deploymentInstance.setProcessDefinitionId(entity.getProcessDefinitionId());
        deploymentInstance.setProcessDefinitionVersion(entity.getProcessDefinitionVersion());
        deploymentInstance.setStartTime(entity.getGmtCreate());
        deploymentInstance.setCompleteTime(entity.getGmtModified());
        deploymentInstance.setExtension(entity.getExtension());
        deploymentInstance.setTenantId(entity.getTenantId());
        return deploymentInstance;
    }

    public static DeploymentInstanceEntity convertByInstance(DeploymentInstance deploymentInstance) {
        DeploymentInstanceEntity deploymentInstanceEntity = new DeploymentInstanceEntity();
        deploymentInstanceEntity.setLogicStatus(deploymentInstance.getLogicStatus());
        deploymentInstanceEntity.setDeploymentStatus(deploymentInstance.getDeploymentStatus());
        deploymentInstanceEntity.setDeploymentUserId(deploymentInstance.getDeploymentUserId());
        deploymentInstanceEntity.setProcessDefinitionContent(deploymentInstance.getProcessDefinitionContent());
        deploymentInstanceEntity.setProcessDefinitionDesc(deploymentInstance.getProcessDefinitionDesc());
        deploymentInstanceEntity.setProcessDefinitionName(deploymentInstance.getProcessDefinitionName());
        deploymentInstanceEntity.setProcessDefinitionType(deploymentInstance.getProcessDefinitionType());
        deploymentInstanceEntity.setProcessDefinitionCode(deploymentInstance.getProcessDefinitionCode());
        deploymentInstanceEntity.setProcessDefinitionId(deploymentInstance.getProcessDefinitionId());
        deploymentInstanceEntity.setProcessDefinitionVersion(deploymentInstance.getProcessDefinitionVersion());
        String deploymentInstanceInstanceId = deploymentInstance.getInstanceId();

        if (null != deploymentInstanceInstanceId) {
            deploymentInstanceEntity.setId(Long.valueOf(deploymentInstanceInstanceId));
        }

        Date currentDate = DateUtil.getCurrentDate();
        deploymentInstanceEntity.setGmtCreate(currentDate);
        deploymentInstanceEntity.setGmtModified(currentDate);
        deploymentInstanceEntity.setExtension(deploymentInstance.getExtension());
        deploymentInstanceEntity.setTenantId(deploymentInstance.getTenantId());

        return deploymentInstanceEntity;
    }

    //private DeploymentInstanceQueryParam convertParamByDeploymentInstance(DeploymentInstance deploymentInstance, Integer pageOffset, Integer pageSize){
    //    DeploymentInstanceQueryParam param = new DeploymentInstanceQueryParam();
    //    if (null != deploymentInstance) {
    //        param.setDeploymentStatus(deploymentInstance.getDeploymentStatus());
    //        param.setDeploymentUserId(deploymentInstance.getDeploymentUserId());
    //        param.setLogicStatus(deploymentInstance.getLogicStatus());
    //        param.setProcessDefinitionName(deploymentInstance.getProcessDefinitionName());
    //        param.setProcessDefinitionType(deploymentInstance.getProcessDefinitionType());
    //        param.setProcessDefinitionVersion(deploymentInstance.getProcessDefinitionVersion());
    //        param.setId(deploymentInstance.getInstanceId());
    //    }
    //    param.setPageOffset(pageOffset);
    //    param.setPageSize(pageSize);
    //    return param;
    //}
}
