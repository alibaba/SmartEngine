package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.common.util.IdConverter;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.NotificationConstant;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.storage.NotificationInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.NotificationInstance;
import com.alibaba.smart.framework.engine.persister.database.builder.NotificationInstanceBuilder;
import com.alibaba.smart.framework.engine.persister.database.dao.NotificationInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.NotificationInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.NotificationQueryParam;

/**
 * Notification instance relational database storage implementation.
 *
 * @author SmartEngine Team
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = NotificationInstanceStorage.class)
public class RelationshipDatabaseNotificationInstanceStorage implements NotificationInstanceStorage {

    @Override
    public NotificationInstance insert(NotificationInstance notificationInstance,
                                       ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = getDAO(processEngineConfiguration);

        NotificationInstanceEntity entity = NotificationInstanceBuilder
                .buildEntityFromNotificationInstance(notificationInstance);

        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());

        notificationInstanceDAO.insert(entity);

        // Set generated ID back to instance
        notificationInstance.setInstanceId(IdConverter.toString(entity.getId()));
        return notificationInstance;
    }

    @Override
    public NotificationInstance update(NotificationInstance notificationInstance,
                                       ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = getDAO(processEngineConfiguration);

        NotificationInstanceEntity entity = NotificationInstanceBuilder
                .buildEntityFromNotificationInstance(notificationInstance);

        entity.setGmtModified(DateUtil.getCurrentDate());

        notificationInstanceDAO.update(entity);

        return notificationInstance;
    }

    @Override
    public NotificationInstance find(String notificationId, String tenantId,
                                     ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = getDAO(processEngineConfiguration);

        Long id = IdConverter.toLong(notificationId, "notificationId");
        NotificationInstanceEntity entity = notificationInstanceDAO.findOne(id, tenantId);

        return NotificationInstanceBuilder.buildNotificationInstanceFromEntity(entity);
    }

    @Override
    public List<NotificationInstance> findNotificationList(NotificationQueryParam param,
                                                           ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = getDAO(processEngineConfiguration);

        List<NotificationInstanceEntity> entityList = notificationInstanceDAO.findByQuery(param);

        return buildInstanceList(entityList);
    }

    @Override
    public Long countNotifications(NotificationQueryParam param,
                                   ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = getDAO(processEngineConfiguration);

        Integer count = notificationInstanceDAO.countByQuery(param);
        return count != null ? count.longValue() : 0L;
    }

    @Override
    public Long countUnreadNotifications(String receiverUserId, String tenantId,
                                         ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = getDAO(processEngineConfiguration);

        Integer count = notificationInstanceDAO.countByReceiver(receiverUserId,
                NotificationConstant.ReadStatus.UNREAD, tenantId);
        return count != null ? count.longValue() : 0L;
    }

    @Override
    public List<NotificationInstance> findByReceiver(String receiverUserId, String readStatus,
                                                     String tenantId, Integer pageOffset, Integer pageSize,
                                                     ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = getDAO(processEngineConfiguration);

        List<NotificationInstanceEntity> entityList = notificationInstanceDAO
                .findByReceiver(receiverUserId, readStatus, tenantId, pageOffset, pageSize);

        return buildInstanceList(entityList);
    }

    @Override
    public List<NotificationInstance> findBySender(String senderUserId, String tenantId,
                                                   Integer pageOffset, Integer pageSize,
                                                   ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = getDAO(processEngineConfiguration);

        List<NotificationInstanceEntity> entityList = notificationInstanceDAO
                .findBySender(senderUserId, tenantId, pageOffset, pageSize);

        return buildInstanceList(entityList);
    }

    @Override
    public int markAsRead(String notificationId, String tenantId,
                          ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = getDAO(processEngineConfiguration);

        Long id = IdConverter.toLong(notificationId, "notificationId");
        return notificationInstanceDAO.markAsRead(id, tenantId);
    }

    @Override
    public int batchMarkAsRead(List<String> notificationIds, String tenantId,
                               ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = getDAO(processEngineConfiguration);

        List<Long> ids = IdConverter.toLongList(notificationIds);
        return notificationInstanceDAO.batchMarkAsRead(ids, tenantId);
    }

    @Override
    public void remove(String notificationId, String tenantId,
                       ProcessEngineConfiguration processEngineConfiguration) {
        NotificationInstanceDAO notificationInstanceDAO = getDAO(processEngineConfiguration);

        Long id = IdConverter.toLong(notificationId, "notificationId");
        notificationInstanceDAO.delete(id, tenantId);
    }

    /**
     * Get DAO from configuration.
     */
    private NotificationInstanceDAO getDAO(ProcessEngineConfiguration processEngineConfiguration) {
        return (NotificationInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("notificationInstanceDAO");
    }

    /**
     * Build instance list from entity list.
     */
    private List<NotificationInstance> buildInstanceList(List<NotificationInstanceEntity> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }

        List<NotificationInstance> instanceList = new ArrayList<>(entityList.size());
        for (NotificationInstanceEntity entity : entityList) {
            NotificationInstance instance = NotificationInstanceBuilder.buildNotificationInstanceFromEntity(entity);
            if (instance != null) {
                instanceList.add(instance);
            }
        }
        return instanceList;
    }
}
