package com.alibaba.smart.framework.engine.service.query;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  17:15.
 */
public interface DeploymentInstanceQueryService {

    DeploymentInstance findOne(Long deploymentId);

    //FIXME pagesize

    List<DeploymentInstance> findActiveDeploymentList(String createUserId);


}
