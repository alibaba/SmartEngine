package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.UserNotificationIndexEntity;

import org.apache.ibatis.annotations.Param;

public interface UserNotificationIndexDAO {

    void insert(UserNotificationIndexEntity entity);

    void deleteByNotificationId(@Param("notificationId") Long notificationId, @Param("tenantId") String tenantId);

    void updateReadStatus(@Param("notificationId") Long notificationId, @Param("readStatus") String readStatus, @Param("tenantId") String tenantId);

    void batchUpdateReadStatus(@Param("notificationIds") List<Long> notificationIds, @Param("readStatus") String readStatus, @Param("tenantId") String tenantId);

    List<UserNotificationIndexEntity> findByReceiver(@Param("receiverUserId") String receiverUserId, @Param("readStatus") String readStatus, @Param("tenantId") String tenantId, @Param("pageOffset") Integer pageOffset, @Param("pageSize") Integer pageSize);

    Integer countByReceiver(@Param("receiverUserId") String receiverUserId, @Param("readStatus") String readStatus, @Param("tenantId") String tenantId);
}
