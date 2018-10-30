package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.instance.impl.DefaultDeploymentInstance;
import com.alibaba.smart.framework.engine.instance.storage.DeploymentInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.persister.common.util.SpringContextUtil;
import com.alibaba.smart.framework.engine.persister.database.dao.DeploymentInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.DeploymentInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.DeploymentInstanceQueryParam;

import org.springframework.util.CollectionUtils;

/**
 * Created by yueyu.yr on 2017/9/22.
 *
 * @author yueyu.yr
 * @date 2017/09/22
 */
public class RelationshipDatabaseDeploymentInstanceStorage implements DeploymentInstanceStorage {

    DeploymentInstanceDAO deploymentnstanceDAO = (DeploymentInstanceDAO)SpringContextUtil.getBean("deploymentInstanceDAO");

    @Override
    public DeploymentInstance insert(DeploymentInstance deploymentInstance,
                                     ProcessEngineConfiguration processEngineConfiguration) {
        DeploymentInstanceEntity entity = convertByInstance(deploymentInstance);
        deploymentnstanceDAO.insert(entity);

        deploymentInstance = findById(entity.getId().toString(), processEngineConfiguration);

        return deploymentInstance;
    }

    @Override
    public DeploymentInstance update(DeploymentInstance deploymentInstance,
                                     ProcessEngineConfiguration processEngineConfiguration) {
        DeploymentInstanceEntity entity = convertByInstance(deploymentInstance);
        deploymentnstanceDAO.update(entity);
        deploymentInstance = findById(entity.getId().toString(), processEngineConfiguration);
        return deploymentInstance;
    }

    @Override
    public DeploymentInstance findById(String id,
                                       ProcessEngineConfiguration processEngineConfiguration) {
        if (null == id){
            return null;
        }

        DeploymentInstanceEntity entity = deploymentnstanceDAO.findOne(Long.valueOf(id));
        DeploymentInstance deploymentInstance = convertByEntity(entity);

        return deploymentInstance;
    }

    @Override
    public List<DeploymentInstance> findByPage(DeploymentInstanceQueryParam deploymentInstanceQueryParam,
                                               ProcessEngineConfiguration processEngineConfiguration) {
        List<DeploymentInstanceEntity> deploymentInstanceEntities = deploymentnstanceDAO.findByPage(
            deploymentInstanceQueryParam);
        if (!CollectionUtils.isEmpty(deploymentInstanceEntities)) {
            List<DeploymentInstance> deploymentInstances = new ArrayList<DeploymentInstance>(deploymentInstanceEntities.size());
            for (DeploymentInstanceEntity entity : deploymentInstanceEntities) {
                DeploymentInstance instance = convertByEntity(entity);
                deploymentInstances.add(instance);
            }
            return deploymentInstances;
        }
        return null;
    }

    @Override
    public int count(DeploymentInstanceQueryParam deploymentInstanceQueryParam,
                     ProcessEngineConfiguration processEngineConfiguration) {
        return deploymentnstanceDAO.count(deploymentInstanceQueryParam);
    }

    @Override
    public void remove(String id,
                       ProcessEngineConfiguration processEngineConfiguration) {
        deploymentnstanceDAO.delete(Long.valueOf(id));
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

    private DeploymentInstance convertByEntity(DeploymentInstanceEntity entity) {
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
        return deploymentInstance;
    }

    private DeploymentInstanceEntity convertByInstance(DeploymentInstance deploymentInstance) {
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

        if(null != deploymentInstanceInstanceId){
            deploymentInstanceEntity.setId(Long.valueOf(deploymentInstanceInstanceId));
        }

        return deploymentInstanceEntity;
    }
}
