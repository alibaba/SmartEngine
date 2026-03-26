package com.alibaba.smart.framework.engine.constant;

/**
 * 督办相关常量
 * 
 * @author SmartEngine Team
 */
public interface SupervisionConstant {

    /**
     * 督办类型
     */
    interface SupervisionType {
        String URGE = "urge";       // 催办
        String TRACK = "track";     // 跟踪
        String REMIND = "remind";   // 提醒
    }

    /**
     * 督办状态
     */
    interface SupervisionStatus {
        String ACTIVE = "active";   // 活跃
        String CLOSED = "closed";   // 已关闭
    }
}