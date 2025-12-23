package com.alibaba.smart.framework.engine.service.param.query;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 知会通知查询参数
 * 
 * @author SmartEngine Team
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NotificationQueryParam extends BaseQueryParam {

    /**
     * 发送人用户ID
     */
    private String senderUserId;

    /**
     * 接收人用户ID
     */
    private String receiverUserId;

    /**
     * 流程实例ID列表
     */
    private List<String> processInstanceIdList;

    /**
     * 任务实例ID列表
     */
    private List<String> taskInstanceIdList;

    /**
     * 通知类型
     */
    private String notificationType;

    /**
     * 读取状态
     */
    private String readStatus;

    /**
     * 通知开始时间
     */
    private Date notificationStartTime;

    /**
     * 通知结束时间
     */
    private Date notificationEndTime;
}