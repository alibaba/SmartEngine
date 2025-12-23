package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.ProcessRollbackRecordEntity;

import org.apache.ibatis.annotations.Param;

/**
 * 流程回退记录DAO接口
 * 
 * @author SmartEngine Team
 */
public interface ProcessRollbackRecordDAO {

    /**
     * 插入回退记录
     */
    void insert(ProcessRollbackRecordEntity processRollbackRecordEntity);

    /**
     * 根据ID查询回退记录
     */
    ProcessRollbackRecordEntity findOne(@Param("id") Long id, @Param("tenantId") String tenantId);

    /**
     * 根据流程实例ID查询回退记录
     */
    List<ProcessRollbackRecordEntity> findByProcessInstanceId(@Param("processInstanceId") Long processInstanceId,
                                                             @Param("tenantId") String tenantId);

    /**
     * 根据任务ID查询回退记录
     */
    List<ProcessRollbackRecordEntity> findByTaskId(@Param("taskInstanceId") Long taskInstanceId,
                                                  @Param("tenantId") String tenantId);

    /**
     * 根据操作人查询回退记录
     */
    List<ProcessRollbackRecordEntity> findByOperator(@Param("operatorUserId") String operatorUserId,
                                                    @Param("tenantId") String tenantId,
                                                    @Param("pageOffset") Integer pageOffset,
                                                    @Param("pageSize") Integer pageSize);

    /**
     * 统计操作人的回退记录数量
     */
    Integer countByOperator(@Param("operatorUserId") String operatorUserId,
                           @Param("tenantId") String tenantId);

    /**
     * 删除回退记录
     */
    void delete(@Param("id") Long id, @Param("tenantId") String tenantId);
}