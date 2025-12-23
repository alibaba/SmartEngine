package com.alibaba.smart.framework.engine.instance.storage;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.instance.SupervisionInstance;
import com.alibaba.smart.framework.engine.service.param.query.SupervisionQueryParam;

/**
 * 督办实例存储接口
 * 
 * @author SmartEngine Team
 */
public interface SupervisionInstanceStorage {

    /**
     * 插入督办实例
     */
    SupervisionInstance insert(SupervisionInstance supervisionInstance,
                              ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 更新督办实例
     */
    SupervisionInstance update(SupervisionInstance supervisionInstance,
                              ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 根据ID查询督办实例
     */
    SupervisionInstance find(String supervisionId, String tenantId,
                            ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 查询督办实例列表
     */
    List<SupervisionInstance> findSupervisionList(SupervisionQueryParam param,
                                                 ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 统计督办实例数量
     */
    Long countSupervision(SupervisionQueryParam param,
                         ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 查询任务的活跃督办记录
     */
    List<SupervisionInstance> findActiveSupervisionByTask(String taskInstanceId, String tenantId,
                                                         ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 关闭督办记录
     */
    int closeSupervision(String supervisionId, String tenantId,
                        ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 根据任务ID批量关闭督办记录
     */
    int closeSupervisionByTask(String taskInstanceId, String tenantId,
                              ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 删除督办实例
     */
    void remove(String supervisionId, String tenantId,
               ProcessEngineConfiguration processEngineConfiguration);
}