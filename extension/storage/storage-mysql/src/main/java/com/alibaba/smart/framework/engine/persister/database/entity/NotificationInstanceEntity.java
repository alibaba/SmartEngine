package com.alibaba.smart.framework.engine.persister.database.entity;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 知会抄送实体类
 * 
 * @author SmartEngine Team
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NotificationInstanceEntity extends BaseProcessEntity {

    private Long processInstanceId;

    private Long taskInstanceId;

    private String senderUserId;

    private String receiverUserId;

    private String notificationType;

    private String title;

    private String content;

    private String readStatus;

    private Date readTime;
}