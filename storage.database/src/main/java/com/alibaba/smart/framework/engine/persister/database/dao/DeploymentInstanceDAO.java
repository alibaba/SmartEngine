package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.DeploymentInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.param.DeploymentInstanceParam;

/**
 * Created by yueyu.yr on 2017/9/22.
 *
 * @author yueyu.yr
 * @date 2017/09/22
 */
public interface DeploymentInstanceDAO {

    Long insert(DeploymentInstanceEntity deploymentInstanceEntity);

    int delete(Long id);

    int update(DeploymentInstanceEntity taskAssigneeEntity);

    List<DeploymentInstanceEntity> find(DeploymentInstanceParam param);

    int count(DeploymentInstanceParam param);
}
