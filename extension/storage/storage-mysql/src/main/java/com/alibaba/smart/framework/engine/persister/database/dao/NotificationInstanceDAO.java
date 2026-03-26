package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.NotificationInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.NotificationQueryParam;

import org.apache.ibatis.annotations.Param;

/**
 * 知会抄送DAO接口
 * 
 * @author SmartEngine Team
 */
public interface NotificationInstanceDAO {

    /**
     * 插入知会记录
     */
    void insert(NotificationInstanceEntity notificationInstanceEntity);

    /**
     * 更新知会记录
     */
    int update(@Param("notificationInstanceEntity") NotificationInstanceEntity notificationInstanceEntity);

    /**
     * 根据ID查询知会记录
     */
    NotificationInstanceEntity findOne(@Param("id") Long id, @Param("tenantId") String tenantId);

    /**
     * 根据接收人查询知会列表
     */
    List<NotificationInstanceEntity> findByReceiver(@Param("receiverUserId") String receiverUserId,
                                                   @Param("readStatus") String readStatus,
                                                   @Param("tenantId") String tenantId,
                                                   @Param("pageOffset") Integer pageOffset,
                                                   @Param("pageSize") Integer pageSize);

    /**
     * 统计接收人的知会数量
     */
    Integer countByReceiver(@Param("receiverUserId") String receiverUserId,
                           @Param("readStatus") String readStatus,
                           @Param("tenantId") String tenantId);

    /**
     * 根据发送人查询知会列表
     */
    List<NotificationInstanceEntity> findBySender(@Param("senderUserId") String senderUserId,
                                                 @Param("tenantId") String tenantId,
                                                 @Param("pageOffset") Integer pageOffset,
                                                 @Param("pageSize") Integer pageSize);

    /**
     * 统计发送人的知会数量
     */
    Integer countBySender(@Param("senderUserId") String senderUserId,
                         @Param("tenantId") String tenantId);

    /**
     * 标记为已读
     */
    int markAsRead(@Param("id") Long id, @Param("tenantId") String tenantId);

    /**
     * 批量标记为已读
     */
    int batchMarkAsRead(@Param("ids") List<Long> ids, @Param("tenantId") String tenantId);

    /**
     * 删除知会记录
     */
    void delete(@Param("id") Long id, @Param("tenantId") String tenantId);

    /**
     * 根据查询参数综合查询知会记录
     */
    List<NotificationInstanceEntity> findByQuery(NotificationQueryParam param);

    /**
     * 根据查询参数统计知会记录数量
     */
    Integer countByQuery(NotificationQueryParam param);
}