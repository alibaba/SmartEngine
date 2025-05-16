package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.ActivityInstanceEntity;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityInstanceDAO {
    List<ActivityInstanceEntity> findAllActivity(@Param("processInstanceId") Long processInstanceId,@Param("tenantId") String tenantId);

    ActivityInstanceEntity findOne(@Param("id") Long id, @Param("tenantId") String tenantId);


    void insert(ActivityInstanceEntity activityInstanceEntity );

    Long delete(@Param("id") Long id, @Param("tenantId") String tenantId);


}
