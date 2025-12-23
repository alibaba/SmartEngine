package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.constant.NotificationConstant;
import com.alibaba.smart.framework.engine.exception.NotificationException;
import com.alibaba.smart.framework.engine.exception.ValidationException;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.storage.NotificationInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.NotificationInstance;
import com.alibaba.smart.framework.engine.service.param.query.NotificationQueryParam;
import com.alibaba.smart.framework.engine.service.query.NotificationQueryService;

/**
 * 知会通知查询服务默认实现
 * 
 * @author SmartEngine Team
 */
@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = NotificationQueryService.class)
public class DefaultNotificationQueryService implements NotificationQueryService, LifeCycleHook, ProcessEngineConfigurationAware {

    private ProcessEngineConfiguration processEngineConfiguration;
    private NotificationInstanceStorage notificationInstanceStorage;

    @Override
    public void start() {
        AnnotationScanner annotationScanner = this.processEngineConfiguration.getAnnotationScanner();
        this.notificationInstanceStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON, NotificationInstanceStorage.class);
    }

    @Override
    public void stop() {
        // 清理资源
    }

    @Override
    public NotificationInstance findOne(String notificationId, String tenantId) {
        if (notificationId == null) {
            throw new ValidationException("NotificationId cannot be null");
        }

        try {
            return notificationInstanceStorage.find(notificationId, tenantId, processEngineConfiguration);
        } catch (Exception e) {
            throw new NotificationException("Failed to find notification: " + notificationId, e);
        }
    }

    @Override
    public List<NotificationInstance> findNotificationList(NotificationQueryParam param) {
        if (param == null) {
            throw new ValidationException("NotificationQueryParam cannot be null");
        }

        try {
            return notificationInstanceStorage.findNotificationList(param, processEngineConfiguration);
        } catch (Exception e) {
            throw new NotificationException("Failed to find notification list", e);
        }
    }

    @Override
    public Long countNotifications(NotificationQueryParam param) {
        if (param == null) {
            throw new ValidationException("NotificationQueryParam cannot be null");
        }

        try {
            return notificationInstanceStorage.countNotifications(param, processEngineConfiguration);
        } catch (Exception e) {
            throw new NotificationException("Failed to count notifications", e);
        }
    }

    @Override
    public Long countUnreadNotifications(String receiverUserId, String tenantId) {
        if (receiverUserId == null) {
            throw new ValidationException("ReceiverUserId cannot be null");
        }

        try {
            return notificationInstanceStorage.countUnreadNotifications(receiverUserId, tenantId, processEngineConfiguration);
        } catch (Exception e) {
            throw new NotificationException("Failed to count unread notifications for user: " + receiverUserId, e);
        }
    }

    @Override
    public List<NotificationInstance> findByReceiver(String receiverUserId, String readStatus, 
                                                   String tenantId, Integer pageOffset, Integer pageSize) {
        if (receiverUserId == null) {
            throw new ValidationException("ReceiverUserId cannot be null");
        }

        try {
            return notificationInstanceStorage.findByReceiver(receiverUserId, readStatus, tenantId, 
                    pageOffset != null ? pageOffset : 0, 
                    pageSize != null ? pageSize : 20, 
                    processEngineConfiguration);
        } catch (Exception e) {
            throw new NotificationException("Failed to find notifications by receiver: " + receiverUserId, e);
        }
    }

    @Override
    public List<NotificationInstance> findBySender(String senderUserId, String tenantId, 
                                                 Integer pageOffset, Integer pageSize) {
        if (senderUserId == null) {
            throw new ValidationException("SenderUserId cannot be null");
        }

        try {
            return notificationInstanceStorage.findBySender(senderUserId, tenantId, 
                    pageOffset != null ? pageOffset : 0, 
                    pageSize != null ? pageSize : 20, 
                    processEngineConfiguration);
        } catch (Exception e) {
            throw new NotificationException("Failed to find notifications by sender: " + senderUserId, e);
        }
    }

    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
}