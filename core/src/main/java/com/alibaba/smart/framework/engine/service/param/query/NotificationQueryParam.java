package com.alibaba.smart.framework.engine.service.param.query;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
     * Process instance ID list
     */
    private List<? extends Serializable> processInstanceIdList;

    /**
     * Task instance ID list
     */
    private List<? extends Serializable> taskInstanceIdList;

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

}
