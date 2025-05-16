package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.DeploymentInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.DeploymentInstanceQueryParam;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by yueyu.yr on 2017/9/22.
 *
 * @author yueyu.yr
 * @date 2017/09/22
 */
@Repository
public interface DeploymentInstanceDAO {
    DeploymentInstanceEntity findOne(@Param("id") Long id, @Param("tenantId") String tenantId);

    List<DeploymentInstanceEntity> findByPage(DeploymentInstanceQueryParam param);

    void insert(DeploymentInstanceEntity deploymentInstanceEntity);

    int delete( @Param("id")Long id, @Param("tenantId") String tenantId);

    int update(DeploymentInstanceEntity taskAssigneeEntity);


    int count(DeploymentInstanceQueryParam param);
}
