package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.TaskTransferRecordEntity;

import org.apache.ibatis.annotations.Param;

/**
 * TaskTransferRecordDAO interface
 *
 * @author SmartEngine Team
 */
public interface TaskTransferRecordDAO {

    void insert(TaskTransferRecordEntity entity);

    TaskTransferRecordEntity select(@Param("id") Long id, @Param("tenantId") String tenantId);

    List<TaskTransferRecordEntity> selectByTaskInstanceId(@Param("taskInstanceId") Long taskInstanceId, @Param("tenantId") String tenantId);

    void update(TaskTransferRecordEntity entity);

    void delete(@Param("id") Long id, @Param("tenantId") String tenantId);
}
