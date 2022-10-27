package com.alibaba.smart.framework.engine.instance.impl;

import java.util.Date;

import com.alibaba.smart.framework.engine.model.instance.LifeCycleInstance;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 抽象实例 Created by ettear on 16-4-19.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class AbstractLifeCycleInstance extends AbstractInstance implements LifeCycleInstance {

    private static final long serialVersionUID = -170898515347885220L;

    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date completeTime;


}
