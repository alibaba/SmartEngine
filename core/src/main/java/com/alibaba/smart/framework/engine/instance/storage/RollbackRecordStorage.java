package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.instance.RollbackRecord;

import java.util.List;

/**
 * 流程回退记录存储接口
 *
 * @author SmartEngine Team
 */
public interface RollbackRecordStorage {

    /**
     * 插入回退记录
     */
    RollbackRecord insert(RollbackRecord rollbackRecord,
                         ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 根据流程实例ID查询回退记录列表
     */
    List<RollbackRecord> findByProcessInstanceId(Long processInstanceId, String tenantId,
                                                 ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 根据ID查询回退记录
     */
    RollbackRecord find(Long id, String tenantId,
                       ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 更新回退记录
     */
    RollbackRecord update(RollbackRecord rollbackRecord,
                         ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 删除回退记录
     */
    void remove(Long id, String tenantId,
               ProcessEngineConfiguration processEngineConfiguration);
}
