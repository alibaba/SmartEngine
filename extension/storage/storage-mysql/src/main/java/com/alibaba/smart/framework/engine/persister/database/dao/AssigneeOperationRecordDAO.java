package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.AssigneeOperationRecordEntity;

import org.apache.ibatis.annotations.Param;

/**
 * AssigneeOperationRecordDAO interface
 *
 * @author SmartEngine Team
 */
public interface AssigneeOperationRecordDAO {

    void insert(AssigneeOperationRecordEntity entity);

    AssigneeOperationRecordEntity select(@Param("id") Long id, @Param("tenantId") String tenantId);

    List<AssigneeOperationRecordEntity> selectByTaskInstanceId(@Param("taskInstanceId") Long taskInstanceId, @Param("tenantId") String tenantId);

    void update(AssigneeOperationRecordEntity entity);

    void delete(@Param("id") Long id, @Param("tenantId") String tenantId);
}
