package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.instance.Instance;
import lombok.Data;

import java.util.Date;

/**
 * 抽象实例
 * Created by ettear on 16-4-19.
 */
@Data
public abstract class AbstractInstance implements Instance {

    /**
     * 实例Id
     */
    private String instanceId;
    /**
     * 状态
     */
    private String status;
    /**
     * 开始时间
     */
    private Date   startDate;
    /**
     * 结束时间
     */
    private Date   completeDate;

}
