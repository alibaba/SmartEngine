package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.ActivityInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.ExecutionInstanceEntity;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

public interface ExecutionInstanceDAO  {

    List<ExecutionInstanceEntity> findActiveExecution(Long processInstanceId);

    List<ExecutionInstanceEntity> findAllExecutionList(Long processInstanceId);

    ExecutionInstanceEntity findOne(@Param("id") Long id);


    //@Options(useGeneratedKeys = true)
    void insert(  ExecutionInstanceEntity executionInstanceEntity );

    int update(ExecutionInstanceEntity executionInstanceEntity);

    void delete(@Param("id") Long id);
}
