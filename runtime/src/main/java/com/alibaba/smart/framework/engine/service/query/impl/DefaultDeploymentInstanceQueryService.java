package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.service.param.PaginateRequest;
import com.alibaba.smart.framework.engine.service.query.DeploymentInstanceQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  07:48.
 */
public class DefaultDeploymentInstanceQueryService implements DeploymentInstanceQueryService {

    @Override
    public DeploymentInstance findOne(Long deploymentId) {
        return null;
    }

    @Override
    public List<DeploymentInstance> findActiveDeploymentList(Long deployUserId, PaginateRequest paginateRequest) {
        return null;
    }

    @Override
    public Integer queryActiveDeploymentCount(Long deployUserId) {
        return null;
    }

}
