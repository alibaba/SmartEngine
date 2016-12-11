package com.alibaba.smart.framework.engine.persister.database.dao;

import com.alibaba.smart.framework.engine.persister.database.entity.ProcessInstanceEntity;
import com.alibaba.spring.data.mybatis.repository.MybatisRepository;

public interface ProcessInstanceDAO extends MybatisRepository<ProcessInstanceEntity, Long> {

}
