package com.alibaba.smart.framework.engine.persister.database.dao;

import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.spring.data.mybatis.repository.MybatisRepository;

import java.util.List;

public interface VariableInstanceDAO extends MybatisRepository<TaskInstanceEntity, Long> {

     List<TaskInstanceEntity> findPendingTask(Long processInstanceId);

}
