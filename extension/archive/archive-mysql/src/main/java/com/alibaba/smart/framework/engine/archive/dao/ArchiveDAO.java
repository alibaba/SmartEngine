package com.alibaba.smart.framework.engine.archive.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ArchiveDAO {

    // ========== Query archivable process instances ==========

    List<Long> findArchivableProcessIds(@Param("completedBefore") Date completedBefore,
                                        @Param("tenantId") String tenantId,
                                        @Param("limit") int limit);

    // ========== INSERT...SELECT archive operations ==========

    int archiveProcessInstances(@Param("ids") List<Long> ids, @Param("tenantId") String tenantId);

    int archiveTaskInstances(@Param("processIds") List<Long> processIds, @Param("tenantId") String tenantId);

    int archiveExecutionInstances(@Param("processIds") List<Long> processIds, @Param("tenantId") String tenantId);

    int archiveActivityInstances(@Param("processIds") List<Long> processIds, @Param("tenantId") String tenantId);

    int archiveVariableInstances(@Param("processIds") List<Long> processIds, @Param("tenantId") String tenantId);

    int archiveTaskAssigneeInstances(@Param("processIds") List<Long> processIds, @Param("tenantId") String tenantId);

    int archiveSupervisionInstances(@Param("processIds") List<Long> processIds, @Param("tenantId") String tenantId);

    int archiveNotificationInstances(@Param("processIds") List<Long> processIds, @Param("tenantId") String tenantId);

    int archiveTaskTransferRecords(@Param("processIds") List<Long> processIds, @Param("tenantId") String tenantId);

    int archiveAssigneeOperationRecords(@Param("processIds") List<Long> processIds, @Param("tenantId") String tenantId);

    int archiveProcessRollbackRecords(@Param("processIds") List<Long> processIds, @Param("tenantId") String tenantId);

    // ========== DELETE original table data ==========

    int deleteTaskTransferRecords(@Param("processIds") List<Long> processIds, @Param("tenantId") String tenantId);

    int deleteAssigneeOperationRecords(@Param("processIds") List<Long> processIds, @Param("tenantId") String tenantId);

    int deleteSupervisionInstances(@Param("processIds") List<Long> processIds, @Param("tenantId") String tenantId);

    int deleteNotificationInstances(@Param("processIds") List<Long> processIds, @Param("tenantId") String tenantId);

    int deleteProcessRollbackRecords(@Param("processIds") List<Long> processIds, @Param("tenantId") String tenantId);

    int deleteTaskAssigneeInstances(@Param("processIds") List<Long> processIds, @Param("tenantId") String tenantId);

    int deleteVariableInstances(@Param("processIds") List<Long> processIds, @Param("tenantId") String tenantId);

    int deleteTaskInstances(@Param("processIds") List<Long> processIds, @Param("tenantId") String tenantId);

    int deleteExecutionInstances(@Param("processIds") List<Long> processIds, @Param("tenantId") String tenantId);

    int deleteActivityInstances(@Param("processIds") List<Long> processIds, @Param("tenantId") String tenantId);

    int deleteProcessInstances(@Param("ids") List<Long> ids, @Param("tenantId") String tenantId);
}
