package com.alibaba.smart.framework.engine.service.param.query;

import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.IdConverter;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Notification query parameter.
 *
 * @author SmartEngine Team
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NotificationQueryParam extends BaseQueryParam {

    /**
     * Sender user ID
     */
    private String senderUserId;

    /**
     * Receiver user ID
     */
    private String receiverUserId;

    /**
     * Process instance ID list (String type for API compatibility)
     */
    private List<String> processInstanceIdList;

    /**
     * Task instance ID list (String type for API compatibility)
     */
    private List<String> taskInstanceIdList;

    /**
     * Notification type
     */
    private String notificationType;

    /**
     * Read status
     */
    private String readStatus;

    /**
     * Notification start time
     */
    private Date notificationStartTime;

    /**
     * Notification end time
     */
    private Date notificationEndTime;

    // ============ Long type getters for MyBatis (avoid CAST in SQL) ============

    /**
     * Get task instance ID list as Long type.
     * Used by MyBatis to avoid CAST operation in SQL.
     *
     * @return Long type ID list, or null if source is null
     */
    public List<Long> getTaskInstanceIdListAsLong() {
        return IdConverter.toLongList(taskInstanceIdList);
    }

    /**
     * Get process instance ID list as Long type.
     * Used by MyBatis to avoid CAST operation in SQL.
     *
     * @return Long type ID list, or null if source is null
     */
    public List<Long> getProcessInstanceIdListAsLong() {
        return IdConverter.toLongList(processInstanceIdList);
    }
}
