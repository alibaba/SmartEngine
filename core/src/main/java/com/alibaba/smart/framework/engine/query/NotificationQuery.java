package com.alibaba.smart.framework.engine.query;

import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.NotificationInstance;

/**
 * Fluent query interface for NotificationInstance.
 * Provides type-safe method chaining for building notification queries.
 *
 * <p>Example usage:
 * <pre>{@code
 * List<NotificationInstance> notifications = smartEngine.createNotificationQuery()
 *     .receiverUserId("user001")
 *     .readStatus("unread")
 *     .orderByCreateTime().desc()
 *     .listPage(0, 10);
 * }</pre>
 *
 * @author SmartEngine Team
 */
public interface NotificationQuery extends ProcessBoundQuery<NotificationQuery, NotificationInstance> {

    // ============ Filter conditions ============

    /**
     * Filter by notification instance ID.
     *
     * @param notificationId the notification instance ID
     * @return this query for method chaining
     */
    NotificationQuery notificationId(String notificationId);

    /**
     * Filter by sender user ID.
     *
     * @param senderUserId the sender user ID
     * @return this query for method chaining
     */
    NotificationQuery senderUserId(String senderUserId);

    /**
     * Filter by receiver user ID.
     *
     * @param receiverUserId the receiver user ID
     * @return this query for method chaining
     */
    NotificationQuery receiverUserId(String receiverUserId);

    /**
     * Conditionally filter by receiver user ID.
     *
     * @param condition      if true, the filter is applied
     * @param receiverUserId the receiver user ID
     * @return this query for method chaining
     */
    NotificationQuery receiverUserId(boolean condition, String receiverUserId);

    /**
     * Filter by task instance ID.
     *
     * @param taskInstanceId the task instance ID
     * @return this query for method chaining
     */
    NotificationQuery taskInstanceId(String taskInstanceId);

    /**
     * Filter by multiple task instance IDs.
     *
     * @param taskInstanceIds the list of task instance IDs
     * @return this query for method chaining
     */
    NotificationQuery taskInstanceIdIn(List<String> taskInstanceIds);

    /**
     * Filter by notification type.
     *
     * @param notificationType the notification type
     * @return this query for method chaining
     */
    NotificationQuery notificationType(String notificationType);

    /**
     * Conditionally filter by notification type.
     *
     * @param condition        if true, the filter is applied
     * @param notificationType the notification type
     * @return this query for method chaining
     */
    NotificationQuery notificationType(boolean condition, String notificationType);

    /**
     * Filter by read status.
     *
     * @param readStatus the read status
     * @return this query for method chaining
     */
    NotificationQuery readStatus(String readStatus);

    /**
     * Conditionally filter by read status.
     *
     * @param condition  if true, the filter is applied
     * @param readStatus the read status
     * @return this query for method chaining
     */
    NotificationQuery readStatus(boolean condition, String readStatus);

    /**
     * Filter by notification time range (start).
     *
     * @param startTime the start of notification time range
     * @return this query for method chaining
     */
    NotificationQuery notificationTimeAfter(Date startTime);

    /**
     * Filter by notification time range (end).
     *
     * @param endTime the end of notification time range
     * @return this query for method chaining
     */
    NotificationQuery notificationTimeBefore(Date endTime);

    // ============ Ordering ============

    /**
     * Order by notification instance ID.
     *
     * @return this query for method chaining (call asc() or desc() next)
     */
    NotificationQuery orderByNotificationId();

    /**
     * Order by read time.
     *
     * @return this query for method chaining (call asc() or desc() next)
     */
    NotificationQuery orderByReadTime();
}
