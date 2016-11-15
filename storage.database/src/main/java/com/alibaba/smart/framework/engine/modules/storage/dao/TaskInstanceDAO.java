package com.alibaba.smart.framework.engine.modules.storage.dao;

import com.alibaba.smart.framework.engine.modules.storage.entity.TaskInstanceEntity;
import com.alibaba.spring.data.mybatis.repository.MybatisRepository;

public interface TaskInstanceDAO extends MybatisRepository<TaskInstanceEntity, Long> {

}
