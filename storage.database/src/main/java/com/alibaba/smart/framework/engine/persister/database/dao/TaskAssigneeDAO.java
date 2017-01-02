package com.alibaba.smart.framework.engine.persister.database.dao;

import com.alibaba.smart.framework.engine.persister.database.entity.TaskAssigneeEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.spring.data.mybatis.repository.MybatisRepository;

import java.util.List;

public interface TaskAssigneeDAO extends MybatisRepository<TaskAssigneeEntity, Long> {

     List<TaskAssigneeEntity> findPendingTask(String  assigneeId);

     List<TaskAssigneeEntity> findSameTask(Long  taskInstanceId);


}
