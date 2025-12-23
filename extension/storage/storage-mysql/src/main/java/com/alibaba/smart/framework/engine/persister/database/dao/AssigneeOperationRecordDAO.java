package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.AssigneeOperationRecordEntity;

/**
 * 加签减签操作记录DAO接口
 * 
 * @author SmartEngine Team
 */
public interface AssigneeOperationRecordDAO {

    void insert(AssigneeOperationRecordEntity entity);

    AssigneeOperationRecordEntity select(Long id, String tenantId);

    List<AssigneeOperationRecordEntity> selectByTaskInstanceId(Long taskInstanceId, String tenantId);

    void update(AssigneeOperationRecordEntity entity);

    void delete(Long id, String tenantId);
}