package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.instance.impl.DefaultDeploymentInstance;
import com.alibaba.smart.framework.engine.instance.storage.DeploymentInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.DeploymentInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.DeploymentInstanceEntity;
import com.alibaba.smart.framework.engine.persister.util.SpringContextUtil;
import com.alibaba.smart.framework.engine.service.param.DeploymentInstanceParam;

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
    public DeploymentInstance insert(DeploymentInstance deploymentInstance) {
        DeploymentInstanceEntity entity = convertByDeploymentInstance(deploymentInstance);
        deploymentnstanceDAO.insert(entity);

        deploymentInstance = findById(entity.getId());

        return deploymentInstance;
    }

    @Override
    public DeploymentInstance update(DeploymentInstance deploymentInstance) {
        DeploymentInstanceEntity entity = convertByDeploymentInstance(deploymentInstance);
        deploymentnstanceDAO.update(entity);
        return deploymentInstance;
    }

    @Override
    public DeploymentInstance findById(Long id) {
        if (null == id){
            return null;
        }
        DeploymentInstanceParam param = new DeploymentInstanceParam();
        param.setId(id);
        List<DeploymentInstanceEntity> deploymentInstanceEntities = deploymentnstanceDAO.find(param);
        DeploymentInstance instance = null;
        if (!CollectionUtils.isEmpty(deploymentInstanceEntities)) {
            instance = convertByDeploymentInstanceEntity(deploymentInstanceEntities.get(0));
        }
        return instance;
    }

    @Override
    public List<DeploymentInstance> findByPage(DeploymentInstanceParam deploymentInstanceParam) {
        List<DeploymentInstanceEntity> deploymentInstanceEntities = deploymentnstanceDAO.find(deploymentInstanceParam);
        if (!CollectionUtils.isEmpty(deploymentInstanceEntities)) {
            List<DeploymentInstance> deploymentInstances = new ArrayList<DeploymentInstance>(deploymentInstanceEntities.size());
            for (DeploymentInstanceEntity entity : deploymentInstanceEntities) {
                DeploymentInstance instance = convertByDeploymentInstanceEntity(entity);
                deploymentInstances.add(instance);
            }
            return deploymentInstances;
        }
        return null;
    }

    @Override
    public int count(DeploymentInstanceParam deploymentInstanceParam) {
        return deploymentnstanceDAO.count(deploymentInstanceParam);
    }

    @Override
    public void remove(Long id) {

    }


    //private DeploymentInstanceParam convertParamByDeploymentInstance(DeploymentInstance deploymentInstance, Integer pageOffSide, Integer pageSize){
    //    DeploymentInstanceParam param = new DeploymentInstanceParam();
    //    if (null != deploymentInstance) {
    //        param.setDeploymentStatus(deploymentInstance.getDeploymentStatus());
    //        param.setDeploymentUserId(deploymentInstance.getDeploymentUserId());
    //        param.setLogicStatus(deploymentInstance.getLogicStatus());
    //        param.setProcessDefinitionName(deploymentInstance.getProcessDefinitionName());
    //        param.setProcessDefinitionType(deploymentInstance.getProcessDefinitionType());
    //        param.setProcessDefinitionVersion(deploymentInstance.getProcessDefinitionVersion());
    //        param.setId(deploymentInstance.getInstanceId());
    //    }
    //    param.setPageOffSide(pageOffSide);
    //    param.setPageSize(pageSize);
    //    return param;
    //}

    private DeploymentInstance convertByDeploymentInstanceEntity(DeploymentInstanceEntity entity) {
        DeploymentInstance deploymentInstance = new DefaultDeploymentInstance();

        deploymentInstance.setInstanceId(entity.getId());
        deploymentInstance.setLogicStatus(entity.getLogicStatus());
        deploymentInstance.setDeploymentStatus(entity.getDeploymentStatus());
        deploymentInstance.setDeploymentUserId(entity.getDeploymentUserId());
        deploymentInstance.setProcessDefinitionContent(entity.getProcessDefinitionContent());
        deploymentInstance.setProcessDefinitionDesc(entity.getProcessDefinitionDesc());
        deploymentInstance.setProcessDefinitionName(entity.getProcessDefinitionName());
        deploymentInstance.setProcessDefinitionType(entity.getProcessDefinitionType());
        deploymentInstance.setProcessDefinitionId(entity.getProcessDefinitionId());
        deploymentInstance.setProcessDefinitionVersion(entity.getProcessDefinitionVersion());
        deploymentInstance.setStartTime(entity.getGmtCreate());
        deploymentInstance.setCompleteTime(entity.getGmtModified());
        return deploymentInstance;
    }

    private DeploymentInstanceEntity convertByDeploymentInstance(DeploymentInstance deploymentInstance) {
        DeploymentInstanceEntity deploymentInstanceEntity = new DeploymentInstanceEntity();
        deploymentInstanceEntity.setLogicStatus(deploymentInstance.getLogicStatus());
        deploymentInstanceEntity.setDeploymentStatus(deploymentInstance.getDeploymentStatus());
        deploymentInstanceEntity.setDeploymentUserId(deploymentInstance.getDeploymentUserId());
        deploymentInstanceEntity.setProcessDefinitionContent(deploymentInstance.getProcessDefinitionContent());
        deploymentInstanceEntity.setProcessDefinitionDesc(deploymentInstance.getProcessDefinitionDesc());
        deploymentInstanceEntity.setProcessDefinitionName(deploymentInstance.getProcessDefinitionName());
        deploymentInstanceEntity.setProcessDefinitionType(deploymentInstance.getProcessDefinitionType());
        deploymentInstanceEntity.setProcessDefinitionId(deploymentInstance.getProcessDefinitionId());
        deploymentInstanceEntity.setProcessDefinitionVersion(deploymentInstance.getProcessDefinitionVersion());
        return deploymentInstanceEntity;
    }
}
