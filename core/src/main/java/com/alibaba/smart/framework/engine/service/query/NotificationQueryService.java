package com.alibaba.smart.framework.engine.service.query;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.NotificationInstance;
import com.alibaba.smart.framework.engine.service.param.query.NotificationQueryParam;

/**
 * 知会抄送查询服务接口
 * 
 * @author SmartEngine Team
 */
public interface NotificationQueryService {

    /**
     * 查询知会通知列表
     *
     * @param param 查询参数
     * @return 知会通知列表
     * @deprecated Use {@code smartEngine.createNotificationQuery()} fluent API instead
     */
    @Deprecated
    List<NotificationInstance> findNotificationList(NotificationQueryParam param);

    /**
     * 统计知会通知数量
     *
     * @param param 查询参数
     * @return 知会通知数量
     * @deprecated Use {@code smartEngine.createNotificationQuery()...count()} instead
     */
    @Deprecated
    Long countNotifications(NotificationQueryParam param);

    /**
     * 统计未读通知数量
     *
     * @param receiverUserId 接收人用户ID
     * @param tenantId 租户ID
     * @return 未读通知数量
     * @deprecated Use {@code smartEngine.createNotificationQuery().receiverUserId(u).readStatus("unread").tenantId(t).count()} instead
     */
    @Deprecated
    Long countUnreadNotifications(String receiverUserId, String tenantId);

    /**
     * 根据ID查询知会通知
     *
     * @param notificationId 通知ID
     * @param tenantId 租户ID
     * @return 知会通知
     * @deprecated Use {@code smartEngine.createNotificationQuery().notificationId(id).tenantId(t).singleResult()} instead
     */
    @Deprecated
    NotificationInstance findOne(String notificationId, String tenantId);

    /**
     * 查询接收人的知会列表
     *
     * @param receiverUserId 接收人用户ID
     * @param readStatus 读取状态（可选）
     * @param tenantId 租户ID
     * @param pageOffset 分页偏移
     * @param pageSize 分页大小
     * @return 知会通知列表
     * @deprecated Use {@code smartEngine.createNotificationQuery().receiverUserId(u).readStatus(s).tenantId(t).listPage(offset, size)} instead
     */
    @Deprecated
    List<NotificationInstance> findByReceiver(String receiverUserId, String readStatus,
                                             String tenantId, Integer pageOffset, Integer pageSize);

    /**
     * 查询发送人的知会列表
     *
     * @param senderUserId 发送人用户ID
     * @param tenantId 租户ID
     * @param pageOffset 分页偏移
     * @param pageSize 分页大小
     * @return 知会通知列表
     * @deprecated Use {@code smartEngine.createNotificationQuery().senderUserId(u).tenantId(t).listPage(offset, size)} instead
     */
    @Deprecated
    List<NotificationInstance> findBySender(String senderUserId, String tenantId,
                                           Integer pageOffset, Integer pageSize);
}