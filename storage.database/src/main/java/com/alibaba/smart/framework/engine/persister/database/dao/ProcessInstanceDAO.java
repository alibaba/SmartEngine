package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.ActivityInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.ExecutionInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.ProcessInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.PaginateRequest;
import com.alibaba.smart.framework.engine.service.param.ProcessInstanceParam;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

public interface ProcessInstanceDAO {

    ProcessInstanceEntity findOne(@Param("id") Long id);

    List<ProcessInstanceEntity> find(ProcessInstanceParam processInstanceParam);


    //@Options(useGeneratedKeys = true)
    void insert(  ProcessInstanceEntity processInstanceEntity );

    int update(ProcessInstanceEntity processInstanceEntity);

    void delete(@Param("id") Long id);
}
