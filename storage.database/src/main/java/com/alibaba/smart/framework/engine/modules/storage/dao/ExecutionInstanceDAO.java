package com.alibaba.smart.framework.engine.modules.storage.dao;

import com.alibaba.smart.framework.engine.modules.storage.entity.ExecutionInstanceEntity;
import com.alibaba.spring.data.mybatis.repository.MybatisRepository;

public interface ExecutionInstanceDAO extends MybatisRepository<ExecutionInstanceEntity, Long> {

}
