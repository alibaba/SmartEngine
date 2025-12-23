package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.RollbackRecordEntity;

/**
 * 流程回退记录DAO接口
 * 
 * @author SmartEngine Team
 */
public interface RollbackRecordDAO {

    void insert(RollbackRecordEntity entity);

    RollbackRecordEntity select(Long id, String tenantId);

    List<RollbackRecordEntity> selectByProcessInstanceId(Long processInstanceId, String tenantId);

    void update(RollbackRecordEntity entity);

    void delete(Long id, String tenantId);
}