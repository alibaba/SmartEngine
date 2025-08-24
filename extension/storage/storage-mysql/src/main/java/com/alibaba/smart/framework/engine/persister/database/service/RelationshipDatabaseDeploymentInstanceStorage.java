package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultDeploymentInstance;
import com.alibaba.smart.framework.engine.instance.storage.DeploymentInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.persister.database.builder.DeploymentInstanceBuilder;
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
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = DeploymentInstanceStorage.class)
public class RelationshipDatabaseDeploymentInstanceStorage implements DeploymentInstanceStorage {

    @Override
    public DeploymentInstance insert(DeploymentInstance deploymentInstance,
                                     ProcessEngineConfiguration processEngineConfiguration) {
        DeploymentInstanceEntity entity = DeploymentInstanceBuilder.convertByInstance(deploymentInstance);
        DeploymentInstanceDAO deploymentInstanceDAO = (DeploymentInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("deploymentInstanceDAO");

        deploymentInstanceDAO.insert(entity);

        Long entityId = entity.getId();

        // 当数据库表id 是非自增时，需要以传入的 id 值为准
        if(0L == entityId){
            entityId = Long.valueOf(deploymentInstance.getInstanceId());
        }

        deploymentInstance = findById(entityId.toString(),deploymentInstance.getTenantId(), processEngineConfiguration);

        return deploymentInstance;
    }

    @Override
    public DeploymentInstance update(DeploymentInstance deploymentInstance,
                                     ProcessEngineConfiguration processEngineConfiguration) {
        DeploymentInstanceEntity entity = DeploymentInstanceBuilder.convertByInstance(deploymentInstance);
        DeploymentInstanceDAO deploymentnstanceDAO = (DeploymentInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("deploymentInstanceDAO");

        deploymentnstanceDAO.update(entity);
        deploymentInstance = findById(entity.getId().toString(),deploymentInstance.getTenantId(), processEngineConfiguration);
        return deploymentInstance;
    }

    @Override
    public DeploymentInstance findById(String id,String tenantId,
                                       ProcessEngineConfiguration processEngineConfiguration) {
        if (null == id){
            return null;
        }
        DeploymentInstanceDAO deploymentnstanceDAO = (DeploymentInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("deploymentInstanceDAO");

        DeploymentInstanceEntity entity = deploymentnstanceDAO.findOne(Long.valueOf(id),tenantId);
        if (entity == null) {
            return null;
        }
        return DeploymentInstanceBuilder.convertByEntity(entity);
    }

    @Override
    public List<DeploymentInstance> findByPage(DeploymentInstanceQueryParam deploymentInstanceQueryParam,
                                               ProcessEngineConfiguration processEngineConfiguration) {
        DeploymentInstanceDAO deploymentnstanceDAO = (DeploymentInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("deploymentInstanceDAO");

        List<DeploymentInstanceEntity> deploymentInstanceEntities = deploymentnstanceDAO.findByPage(
            deploymentInstanceQueryParam);
        if (!CollectionUtils.isEmpty(deploymentInstanceEntities)) {
            List<DeploymentInstance> deploymentInstances = new ArrayList<DeploymentInstance>(deploymentInstanceEntities.size());
            for (DeploymentInstanceEntity entity : deploymentInstanceEntities) {
                DeploymentInstance instance = DeploymentInstanceBuilder.convertByEntity(entity);
                deploymentInstances.add(instance);
            }
            return deploymentInstances;
        }
        return null;
    }

    @Override
    public int count(DeploymentInstanceQueryParam deploymentInstanceQueryParam,
                     ProcessEngineConfiguration processEngineConfiguration) {
        DeploymentInstanceDAO deploymentnstanceDAO = (DeploymentInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("deploymentInstanceDAO");

        return deploymentnstanceDAO.count(deploymentInstanceQueryParam);
    }

    @Override
    public void remove(String id,String tenantId,
                       ProcessEngineConfiguration processEngineConfiguration) {
        DeploymentInstanceDAO deploymentnstanceDAO = (DeploymentInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("deploymentInstanceDAO");

        deploymentnstanceDAO.delete(Long.valueOf(id),tenantId);
    }

}
