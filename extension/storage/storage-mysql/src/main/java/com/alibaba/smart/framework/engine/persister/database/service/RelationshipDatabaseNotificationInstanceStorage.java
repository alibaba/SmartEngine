package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.NotificationConstant;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultNotificationInstance;
import com.alibaba.smart.framework.engine.instance.storage.NotificationInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.NotificationInstance;
import com.alibaba.smart.framework.engine.persister.database.builder.NotificationInstanceBuilder;
import com.alibaba.smart.framework.engine.persister.database.dao.NotificationInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.NotificationInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.NotificationQueryParam;

/**
 * 知会通知实例关系数据库存储实现
 * 
 * @author SmartEngine Team
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = NotificationInstanceStorage.class)
public class RelationshipDatabaseNotificationInstanceStorage implements NotificationInstanceStorage {

    @Override
    public NotificationInstance insert(NotificationInstance notificationInstance,
                                      ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = (NotificationInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("notificationInstanceDAO");
        
        NotificationInstanceEntity notificationInstanceEntity = NotificationInstanceBuilder
                .buildEntityFromNotificationInstance(notificationInstance);
        
        notificationInstanceEntity.setGmtCreate(DateUtil.getCurrentDate());
        notificationInstanceEntity.setGmtModified(DateUtil.getCurrentDate());
        
        notificationInstanceDAO.insert(notificationInstanceEntity);
        
        notificationInstance.setInstanceId(notificationInstanceEntity.getId().toString());
        return notificationInstance;
    }

    @Override
    public NotificationInstance update(NotificationInstance notificationInstance,
                                      ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = (NotificationInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("notificationInstanceDAO");
        
        NotificationInstanceEntity notificationInstanceEntity = NotificationInstanceBuilder
                .buildEntityFromNotificationInstance(notificationInstance);
        
        notificationInstanceEntity.setGmtModified(DateUtil.getCurrentDate());
        
        notificationInstanceDAO.update(notificationInstanceEntity);
        
        return notificationInstance;
    }

    @Override
    public NotificationInstance find(String notificationId, String tenantId,
                                    ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = (NotificationInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("notificationInstanceDAO");
        
        NotificationInstanceEntity notificationInstanceEntity = notificationInstanceDAO
                .findOne(Long.valueOf(notificationId), tenantId);
        
        if (notificationInstanceEntity == null) {
            return null;
        }
        
        return NotificationInstanceBuilder.buildNotificationInstanceFromEntity(notificationInstanceEntity);
    }

    @Override
    public List<NotificationInstance> findNotificationList(NotificationQueryParam param,
                                                          ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = (NotificationInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("notificationInstanceDAO");
        
        List<NotificationInstanceEntity> notificationInstanceEntityList;
        
        if (param.getReceiverUserId() != null) {
            notificationInstanceEntityList = notificationInstanceDAO
                    .findByReceiver(param.getReceiverUserId(), param.getReadStatus(), param.getTenantId(),
                                  param.getPageOffset(), param.getPageSize());
        } else if (param.getSenderUserId() != null) {
            notificationInstanceEntityList = notificationInstanceDAO
                    .findBySender(param.getSenderUserId(), param.getTenantId(),
                                param.getPageOffset(), param.getPageSize());
        } else {
            // 默认查询逻辑，可以根据需要扩展
            notificationInstanceEntityList = new ArrayList<>();
        }
        
        List<NotificationInstance> notificationInstanceList = new ArrayList<>(notificationInstanceEntityList.size());
        for (NotificationInstanceEntity notificationInstanceEntity : notificationInstanceEntityList) {
            NotificationInstance notificationInstance = NotificationInstanceBuilder
                    .buildNotificationInstanceFromEntity(notificationInstanceEntity);
            notificationInstanceList.add(notificationInstance);
        }
        
        return notificationInstanceList;
    }

    @Override
    public Long countNotifications(NotificationQueryParam param,
                                  ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = (NotificationInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("notificationInstanceDAO");
        
        Integer count;
        
        if (param.getReceiverUserId() != null) {
            count = notificationInstanceDAO.countByReceiver(param.getReceiverUserId(), 
                                                          param.getReadStatus(), param.getTenantId());
        } else if (param.getSenderUserId() != null) {
            count = notificationInstanceDAO.countBySender(param.getSenderUserId(), param.getTenantId());
        } else {
            count = 0;
        }
        
        return count != null ? count.longValue() : 0L;
    }

    @Override
    public Long countUnreadNotifications(String receiverUserId, String tenantId,
                                        ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = (NotificationInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("notificationInstanceDAO");
        
        Integer count = notificationInstanceDAO.countByReceiver(receiverUserId, 
                                                              NotificationConstant.ReadStatus.UNREAD, tenantId);
        return count != null ? count.longValue() : 0L;
    }

    @Override
    public List<NotificationInstance> findByReceiver(String receiverUserId, String readStatus,
                                                    String tenantId, Integer pageOffset, Integer pageSize,
                                                    ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = (NotificationInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("notificationInstanceDAO");
        
        List<NotificationInstanceEntity> notificationInstanceEntityList = notificationInstanceDAO
                .findByReceiver(receiverUserId, readStatus, tenantId, pageOffset, pageSize);
        
        List<NotificationInstance> notificationInstanceList = new ArrayList<>(notificationInstanceEntityList.size());
        for (NotificationInstanceEntity notificationInstanceEntity : notificationInstanceEntityList) {
            NotificationInstance notificationInstance = NotificationInstanceBuilder
                    .buildNotificationInstanceFromEntity(notificationInstanceEntity);
            notificationInstanceList.add(notificationInstance);
        }
        
        return notificationInstanceList;
    }

    @Override
    public List<NotificationInstance> findBySender(String senderUserId, String tenantId,
                                                  Integer pageOffset, Integer pageSize,
                                                  ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = (NotificationInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("notificationInstanceDAO");
        
        List<NotificationInstanceEntity> notificationInstanceEntityList = notificationInstanceDAO
                .findBySender(senderUserId, tenantId, pageOffset, pageSize);
        
        List<NotificationInstance> notificationInstanceList = new ArrayList<>(notificationInstanceEntityList.size());
        for (NotificationInstanceEntity notificationInstanceEntity : notificationInstanceEntityList) {
            NotificationInstance notificationInstance = NotificationInstanceBuilder
                    .buildNotificationInstanceFromEntity(notificationInstanceEntity);
            notificationInstanceList.add(notificationInstance);
        }
        
        return notificationInstanceList;
    }

    @Override
    public int markAsRead(String notificationId, String tenantId,
                         ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = (NotificationInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("notificationInstanceDAO");
        
        return notificationInstanceDAO.markAsRead(Long.valueOf(notificationId), tenantId);
    }

    @Override
    public int batchMarkAsRead(List<String> notificationIds, String tenantId,
                              ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = (NotificationInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("notificationInstanceDAO");
        
        List<Long> ids = new ArrayList<>(notificationIds.size());
        for (String notificationId : notificationIds) {
            ids.add(Long.valueOf(notificationId));
        }
        
        return notificationInstanceDAO.batchMarkAsRead(ids, tenantId);
    }

    @Override
    public void remove(String notificationId, String tenantId,
                      ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = (NotificationInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("notificationInstanceDAO");
        
        notificationInstanceDAO.delete(Long.valueOf(notificationId), tenantId);
    }
}