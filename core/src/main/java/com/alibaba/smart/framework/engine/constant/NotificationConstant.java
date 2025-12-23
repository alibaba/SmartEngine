package com.alibaba.smart.framework.engine.constant;

/**
 * 知会通知相关常量
 * 
 * @author SmartEngine Team
 */
public interface NotificationConstant {

    /**
     * 通知类型
     */
    interface NotificationType {
        String CC = "cc";           // 抄送
        String INFORM = "inform";   // 知会
    }

    /**
     * 读取状态
     */
    interface ReadStatus {
        String UNREAD = "unread";   // 未读
        String READ = "read";       // 已读
    }
}