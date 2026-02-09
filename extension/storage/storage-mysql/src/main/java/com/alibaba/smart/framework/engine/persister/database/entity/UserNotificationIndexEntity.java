package com.alibaba.smart.framework.engine.persister.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserNotificationIndexEntity extends BaseProcessEntity {

    private String receiverUserId;
    private Long notificationId;
    private Long processInstanceId;
    private String notificationType;
    private String title;
    private String readStatus;
}
