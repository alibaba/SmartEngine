package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.RollbackRecordEntity;

import org.apache.ibatis.annotations.Param;

/**
 * RollbackRecordDAO interface
 *
 * @author SmartEngine Team
 */
public interface RollbackRecordDAO {

    void insert(RollbackRecordEntity entity);

    RollbackRecordEntity select(@Param("id") Long id, @Param("tenantId") String tenantId);

    List<RollbackRecordEntity> selectByProcessInstanceId(@Param("processInstanceId") Long processInstanceId, @Param("tenantId") String tenantId);

    void update(RollbackRecordEntity entity);

    void delete(@Param("id") Long id, @Param("tenantId") String tenantId);
}
