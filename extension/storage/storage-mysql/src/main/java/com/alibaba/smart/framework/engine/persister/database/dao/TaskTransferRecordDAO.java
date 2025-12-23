package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.TaskTransferRecordEntity;

/**
 * 任务移交记录DAO接口
 * 
 * @author SmartEngine Team
 */
public interface TaskTransferRecordDAO {

    void insert(TaskTransferRecordEntity entity);

    TaskTransferRecordEntity select(Long id, String tenantId);

    List<TaskTransferRecordEntity> selectByTaskInstanceId(Long taskInstanceId, String tenantId);

    void update(TaskTransferRecordEntity entity);

    void delete(Long id, String tenantId);
}