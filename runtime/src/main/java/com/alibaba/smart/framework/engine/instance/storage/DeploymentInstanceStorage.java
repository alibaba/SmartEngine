package com.alibaba.smart.framework.engine.instance.storage;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.service.param.DeploymentInstanceParam;

/**
 * Created by yueyu.yr on 2017/9/22.
 *
 * @author yueyu.yr
 * @date 2017/09/22
 */
public interface DeploymentInstanceStorage {

    DeploymentInstance insert(DeploymentInstance deploymentInstance);

    DeploymentInstance update(DeploymentInstance deploymentInstance);

    DeploymentInstance findById(Long id);

    List<DeploymentInstance> findByPage(DeploymentInstanceParam deploymentInstanceParam);

    int count(DeploymentInstanceParam deploymentInstanceParam);

    void remove(Long id);

}
