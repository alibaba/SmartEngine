package com.alibaba.smart.framework.engine.instance.impl;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.LifeCycleInstance;

/**
 * 抽象实例 Created by ettear on 16-4-19.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractLifeCycleInstance extends AbstractInstance implements LifeCycleInstance {

    private static final long serialVersionUID = -170898515347885220L;
    /**
     * 状态
     */
    private InstanceStatus    status           = InstanceStatus.running;
    /**
     * 开始时间
     */
    private Date              startDate;
    /**
     * 结束时间
     */
    private Date              completeDate;

    // private boolean suspend;

    @Override
    public boolean isSuspend() {
        return InstanceStatus.suspended == this.status;
    }
}
