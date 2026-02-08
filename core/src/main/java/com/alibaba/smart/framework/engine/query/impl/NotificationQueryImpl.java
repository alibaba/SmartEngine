package com.alibaba.smart.framework.engine.query.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.storage.NotificationInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.NotificationInstance;
import com.alibaba.smart.framework.engine.query.NotificationQuery;
import com.alibaba.smart.framework.engine.service.param.query.NotificationQueryParam;

/**
 * Implementation of NotificationQuery fluent API.
 *
 * @author SmartEngine Team
 */
public class NotificationQueryImpl extends AbstractProcessBoundQuery<NotificationQuery, NotificationInstance>
        implements NotificationQuery {

    private NotificationInstanceStorage notificationInstanceStorage;

    // Filter conditions
    private String notificationId;
    private String senderUserId;
    private String receiverUserId;
    private String taskInstanceId;
    private List<String> taskInstanceIds;
    private String notificationType;
    private String readStatus;
    private Date notificationStartTime;
    private Date notificationEndTime;

    public NotificationQueryImpl(ProcessEngineConfiguration processEngineConfiguration) {
        super(processEngineConfiguration);
        this.notificationInstanceStorage = processEngineConfiguration.getAnnotationScanner()
                .getExtensionPoint(ExtensionConstant.COMMON, NotificationInstanceStorage.class);
    }

    // ============ Filter conditions ============

    @Override
    public NotificationQuery notificationId(String notificationId) {
        this.notificationId = notificationId;
        return this;
    }

    @Override
    public NotificationQuery senderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
        return this;
    }

    @Override
    public NotificationQuery receiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId;
        return this;
    }

    @Override
    public NotificationQuery receiverUserId(boolean condition, String receiverUserId) {
        if (condition) {
            this.receiverUserId = receiverUserId;
        }
        return this;
    }

    @Override
    public NotificationQuery taskInstanceId(String taskInstanceId) {
        this.taskInstanceId = taskInstanceId;
        return this;
    }

    @Override
    public NotificationQuery taskInstanceIdIn(List<String> taskInstanceIds) {
        this.taskInstanceIds = taskInstanceIds;
        return this;
    }

    @Override
    public NotificationQuery notificationType(String notificationType) {
        this.notificationType = notificationType;
        return this;
    }

    @Override
    public NotificationQuery notificationType(boolean condition, String notificationType) {
        if (condition) {
            this.notificationType = notificationType;
        }
        return this;
    }

    @Override
    public NotificationQuery readStatus(String readStatus) {
        this.readStatus = readStatus;
        return this;
    }

    @Override
    public NotificationQuery readStatus(boolean condition, String readStatus) {
        if (condition) {
            this.readStatus = readStatus;
        }
        return this;
    }

    @Override
    public NotificationQuery notificationTimeAfter(Date startTime) {
        this.notificationStartTime = startTime;
        return this;
    }

    @Override
    public NotificationQuery notificationTimeBefore(Date endTime) {
        this.notificationEndTime = endTime;
        return this;
    }

    // ============ Ordering ============

    @Override
    public NotificationQuery orderByNotificationId() {
        return orderBy("id", "id");
    }

    @Override
    public NotificationQuery orderByReadTime() {
        return orderBy("readTime", "read_time");
    }

    // ============ Execution ============

    @Override
    protected List<NotificationInstance> executeList() {
        NotificationQueryParam param = buildQueryParam();
        return notificationInstanceStorage.findNotificationList(param, processEngineConfiguration);
    }

    @Override
    protected long executeCount() {
        NotificationQueryParam param = buildQueryParam();
        Long count = notificationInstanceStorage.countNotifications(param, processEngineConfiguration);
        return count != null ? count : 0L;
    }

    /**
     * Build NotificationQueryParam from the fluent query settings.
     */
    private NotificationQueryParam buildQueryParam() {
        NotificationQueryParam param = new NotificationQueryParam();

        // Set ID filter
        if (notificationId != null) {
            param.setId(Long.parseLong(notificationId));
        }

        // Set user filters
        param.setSenderUserId(senderUserId);
        param.setReceiverUserId(receiverUserId);

        // Set task instance filter
        if (taskInstanceId != null) {
            List<String> ids = new ArrayList<>();
            ids.add(taskInstanceId);
            param.setTaskInstanceIdList(ids);
        } else if (taskInstanceIds != null && !taskInstanceIds.isEmpty()) {
            param.setTaskInstanceIdList(taskInstanceIds);
        }

        // Set process instance filter (from base class)
        List<String> processInstanceIdList = buildProcessInstanceIdList();
        if (processInstanceIdList != null) {
            param.setProcessInstanceIdList(processInstanceIdList);
        }

        // Set other filters
        param.setNotificationType(notificationType);
        param.setReadStatus(readStatus);
        param.setNotificationStartTime(notificationStartTime);
        param.setNotificationEndTime(notificationEndTime);

        // Set pagination
        param.setPageOffset(pageOffset);
        param.setPageSize(pageSize);
        param.setTenantId(tenantId);

        // Set order by specs
        param.setOrderBySpecs(orderBySpecs);

        return param;
    }
}
