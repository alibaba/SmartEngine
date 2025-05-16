package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.ExecutionInstanceEntity;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionInstanceDAO  {

    List<ExecutionInstanceEntity> findActiveExecution(@Param("processInstanceId") Long processInstanceId, @Param("tenantId") String tenantId);

    List<ExecutionInstanceEntity> findAllExecutionList( @Param("processInstanceId")Long processInstanceId, @Param("tenantId") String tenantId);

    List<ExecutionInstanceEntity> findByActivityInstanceId(
            @Param("processInstanceId") Long processInstanceId,
            @Param("activityInstanceId") Long activityInstanceId,
            @Param("tenantId") String tenantId);


    ExecutionInstanceEntity findOne(@Param("id") Long id,@Param("tenantId") String tenantId);

    ExecutionInstanceEntity findWithShading(@Param("id") Long id,@Param("processInstanceId") Long processInstanceId,@Param("tenantId") String tenantId);


    void insert(  ExecutionInstanceEntity executionInstanceEntity );

    int update(ExecutionInstanceEntity executionInstanceEntity);

    void delete(@Param("id") Long id,@Param("tenantId") String tenantId);
}
