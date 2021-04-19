package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.ExecutionInstanceEntity;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionInstanceDAO  {

    List<ExecutionInstanceEntity> findActiveExecution(Long processInstanceId);

    List<ExecutionInstanceEntity> findAllExecutionList(Long processInstanceId);

    List<ExecutionInstanceEntity> findByActivityInstanceId(@Param("processInstanceId") Long processInstanceId,@Param("activityInstanceId") Long activityInstanceId);


    ExecutionInstanceEntity findOne(@Param("id") Long id);

    ExecutionInstanceEntity findWithShading(@Param("id") Long id,@Param("processInstanceId") Long processInstanceId);


    void insert(  ExecutionInstanceEntity executionInstanceEntity );

    int update(ExecutionInstanceEntity executionInstanceEntity);

    void delete(@Param("id") Long id);
}
