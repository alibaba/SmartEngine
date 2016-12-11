package com.alibaba.smart.framework.engine.persister.database.dao;

import com.alibaba.smart.framework.engine.persister.database.entity.ActivityInstanceEntity;
import com.alibaba.spring.data.mybatis.repository.MybatisRepository;

public interface ActivityInstanceDAO extends MybatisRepository<ActivityInstanceEntity, Long> {

}
