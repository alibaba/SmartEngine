package com.alibaba.smart.framework.engine.persister.database.dao;

import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.persister.database.entity.ExecutionInstanceEntity;
import com.alibaba.spring.data.mybatis.repository.MybatisRepository;

import java.util.List;

public interface ExecutionInstanceDAO extends MybatisRepository<ExecutionInstanceEntity, Long> {

    List<ExecutionInstanceEntity> findAllExecutionList(Long processInstanceId);

}
