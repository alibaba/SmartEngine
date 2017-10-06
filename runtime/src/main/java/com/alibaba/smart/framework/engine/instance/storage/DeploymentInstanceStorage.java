package com.alibaba.smart.framework.engine.instance.storage;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.service.param.query.DeploymentInstanceQueryParam;

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

    List<DeploymentInstance> findByPage(DeploymentInstanceQueryParam deploymentInstanceQueryParam);

    int count(DeploymentInstanceQueryParam deploymentInstanceQueryParam);

    void remove(Long id);

}
