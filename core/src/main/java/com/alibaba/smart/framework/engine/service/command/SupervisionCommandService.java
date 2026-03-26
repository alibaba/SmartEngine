package com.alibaba.smart.framework.engine.service.command;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.SupervisionInstance;

/**
 * 督办管理命令服务接口
 * 
 * @author SmartEngine Team
 */
public interface SupervisionCommandService {

    /**
     * 发起督办
     * 
     * @param taskInstanceId 任务实例ID
     * @param supervisorUserId 督办人用户ID
     * @param reason 督办原因
     * @param supervisionType 督办类型
     * @param tenantId 租户ID
     * @return 督办实例
     */
    SupervisionInstance createSupervision(String taskInstanceId, String supervisorUserId, 
                                        String reason, String supervisionType, String tenantId);

    /**
     * 关闭督办
     * 
     * @param supervisionId 督办ID
     * @param tenantId 租户ID
     */
    void closeSupervision(String supervisionId, String tenantId);

    /**
     * 批量督办
     * 
     * @param taskInstanceIds 任务实例ID列表
     * @param supervisorUserId 督办人用户ID
     * @param reason 督办原因
     * @param supervisionType 督办类型
     * @param tenantId 租户ID
     * @return 督办实例列表
     */
    List<SupervisionInstance> batchCreateSupervision(List<String> taskInstanceIds, 
                                                   String supervisorUserId, String reason, 
                                                   String supervisionType, String tenantId);

    /**
     * 根据任务完成自动关闭督办
     * 
     * @param taskInstanceId 任务实例ID
     * @param tenantId 租户ID
     */
    void autoCloseSupervisionByTask(String taskInstanceId, String tenantId);
}