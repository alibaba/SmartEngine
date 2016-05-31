package com.alibaba.smart.framework.engine.modules.storage.dao;

import com.alibaba.smart.framework.engine.modules.storage.entity.ProcessInstanceEntity;
import com.alibaba.spring.data.mybatis.repository.MybatisRepository;


public interface ProcessInstanceDAO  extends MybatisRepository<ProcessInstanceEntity, Long>{

}
