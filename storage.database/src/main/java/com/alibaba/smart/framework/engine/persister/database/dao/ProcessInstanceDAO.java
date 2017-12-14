package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.ProcessInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;

import org.apache.ibatis.annotations.Param;

public interface ProcessInstanceDAO {

    ProcessInstanceEntity findOne(@Param("id") Long id);

    ProcessInstanceEntity findOneForUpdate(@Param("id") Long id);

    List<ProcessInstanceEntity> find(ProcessInstanceQueryParam processInstanceQueryParam);

    Long count(ProcessInstanceQueryParam processInstanceQueryParam);

    //@Options(useGeneratedKeys = true)
    void insert(  ProcessInstanceEntity processInstanceEntity );

    //@Options(useGeneratedKeys = true)
    int insertIgnore(  ProcessInstanceEntity processInstanceEntity );

    int update(ProcessInstanceEntity processInstanceEntity);

    void delete(@Param("id") Long id);

    void tryLock(Long id);
}
