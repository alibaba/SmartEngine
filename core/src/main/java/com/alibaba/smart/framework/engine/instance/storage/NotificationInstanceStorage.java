package com.alibaba.smart.framework.engine.instance.storage;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.instance.NotificationInstance;
import com.alibaba.smart.framework.engine.service.param.query.NotificationQueryParam;

/**
 * 知会通知实例存储接口
 * 
 * @author SmartEngine Team
 */
public interface NotificationInstanceStorage {

    /**
     * 插入知会通知实例
     */
    NotificationInstance insert(NotificationInstance notificationInstance,
                               ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 更新知会通知实例
     */
    NotificationInstance update(NotificationInstance notificationInstance,
                               ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 根据ID查询知会通知实例
     */
    NotificationInstance find(String notificationId, String tenantId,
                             ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 查询知会通知实例列表
     */
    List<NotificationInstance> findNotificationList(NotificationQueryParam param,
                                                   ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 统计知会通知实例数量
     */
    Long countNotifications(NotificationQueryParam param,
                           ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 统计未读通知数量
     */
    Long countUnreadNotifications(String receiverUserId, String tenantId,
                                 ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 查询接收人的知会列表
     */
    List<NotificationInstance> findByReceiver(String receiverUserId, String readStatus,
                                             String tenantId, Integer pageOffset, Integer pageSize,
                                             ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 查询发送人的知会列表
     */
    List<NotificationInstance> findBySender(String senderUserId, String tenantId,
                                           Integer pageOffset, Integer pageSize,
                                           ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 标记为已读
     */
    int markAsRead(String notificationId, String tenantId,
                  ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 批量标记为已读
     */
    int batchMarkAsRead(List<String> notificationIds, String tenantId,
                       ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 删除知会通知实例
     */
    void remove(String notificationId, String tenantId,
               ProcessEngineConfiguration processEngineConfiguration);
}