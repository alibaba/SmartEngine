package com.alibaba.smart.framework.engine.persister.database.dao;

import com.alibaba.smart.framework.engine.persister.database.entity.ActivityInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.spring.data.mybatis.repository.MybatisRepository;

import java.util.List;

public interface ActivityInstanceDAO extends MybatisRepository<ActivityInstanceEntity, Long> {
    List<ActivityInstanceEntity> findAllActivity(Long processInstanceId);
}
