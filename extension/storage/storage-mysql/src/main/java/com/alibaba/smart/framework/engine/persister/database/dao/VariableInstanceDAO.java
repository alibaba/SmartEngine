package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.VariableInstanceEntity;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VariableInstanceDAO  {


     VariableInstanceEntity findOne(@Param("id") Long id);

     List<VariableInstanceEntity> findList(@Param("processInstanceId") Long processInstanceId,@Param("executionInstanceId") Long executionInstanceId);

     List<VariableInstanceEntity>  findAll(@Param("processInstanceId") Long processInstanceId);

     void insert(  VariableInstanceEntity variableInstanceEntity );

     int update(VariableInstanceEntity variableInstanceEntity);

     void delete(@Param("id") Long id);
}
