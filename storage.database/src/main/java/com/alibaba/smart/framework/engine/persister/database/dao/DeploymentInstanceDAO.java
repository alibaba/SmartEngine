package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.DeploymentInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.DeploymentInstanceParam;

import org.apache.ibatis.annotations.Param;

/**
 * Created by yueyu.yr on 2017/9/22.
 *
 * @author yueyu.yr
 * @date 2017/09/22
 */
public interface DeploymentInstanceDAO {
    DeploymentInstanceEntity findOne(@Param("id") Long id);

    List<DeploymentInstanceEntity> find(DeploymentInstanceParam param);

    Long insert(DeploymentInstanceEntity deploymentInstanceEntity);

    int delete(Long id);

    int update(DeploymentInstanceEntity taskAssigneeEntity);


    int count(DeploymentInstanceParam param);
}
