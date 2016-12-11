package com.alibaba.smart.framework.engine.persister.database.dao;

import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.spring.data.mybatis.repository.MybatisRepository;

public interface TaskInstanceDAO extends MybatisRepository<TaskInstanceEntity, Long> {

}
