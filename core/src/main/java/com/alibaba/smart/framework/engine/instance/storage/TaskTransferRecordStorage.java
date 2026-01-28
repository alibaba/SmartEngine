package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.instance.TaskTransferRecord;

import java.util.List;

/**
 * 任务移交记录存储接口
 *
 * @author SmartEngine Team
 */
public interface TaskTransferRecordStorage {

    /**
     * 插入任务移交记录
     */
    TaskTransferRecord insert(TaskTransferRecord taskTransferRecord,
                             ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 根据任务ID查询移交记录列表
     */
    List<TaskTransferRecord> findByTaskId(Long taskInstanceId, String tenantId,
                                         ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 根据ID查询移交记录
     */
    TaskTransferRecord find(Long id, String tenantId,
                           ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 更新移交记录
     */
    TaskTransferRecord update(TaskTransferRecord taskTransferRecord,
                             ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 删除移交记录
     */
    void remove(Long id, String tenantId,
               ProcessEngineConfiguration processEngineConfiguration);
}
