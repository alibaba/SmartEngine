package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.instance.AssigneeOperationRecord;

import java.util.List;

/**
 * 加签减签操作记录存储接口
 *
 * @author SmartEngine Team
 */
public interface AssigneeOperationRecordStorage {

    /**
     * 插入加签减签操作记录
     */
    AssigneeOperationRecord insert(AssigneeOperationRecord assigneeOperationRecord,
                                   ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 根据任务ID查询操作记录列表
     */
    List<AssigneeOperationRecord> findByTaskId(Long taskInstanceId, String tenantId,
                                               ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 根据ID查询操作记录
     */
    AssigneeOperationRecord find(Long id, String tenantId,
                                ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 更新操作记录
     */
    AssigneeOperationRecord update(AssigneeOperationRecord assigneeOperationRecord,
                                   ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 删除操作记录
     */
    void remove(Long id, String tenantId,
               ProcessEngineConfiguration processEngineConfiguration);
}
