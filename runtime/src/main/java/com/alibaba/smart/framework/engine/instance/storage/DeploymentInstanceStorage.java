package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;

/**
 * Created by yueyu.yr on 2017/9/22.
 *
 * @author yueyu.yr
 * @date 2017/09/22
 */
public interface DeploymentInstanceStorage {

    DeploymentInstance insert(DeploymentInstance deploymentInstance);

    DeploymentInstance update(DeploymentInstance deploymentInstance);

    DeploymentInstance find(DeploymentInstance deploymentInstance);

    DeploymentInstance findByPage(DeploymentInstance deploymentInstance,int pageOffSide, int pageSize);

    int count(DeploymentInstance deploymentInstance);

    void remove(Long id);

}