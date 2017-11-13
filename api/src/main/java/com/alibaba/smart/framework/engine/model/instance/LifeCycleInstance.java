package com.alibaba.smart.framework.engine.model.instance;

import java.util.Date;

/**
 * 生命周期实例 Created by ettear on 16-4-21.
 */
public interface LifeCycleInstance extends Instance {

//    InstanceStatus getStatus();
//
//    void setStatus(InstanceStatus status);

    Date getStartTime();

    void setStartTime(Date startDate);

    Date getCompleteTime();

    void setCompleteTime(Date completeTime);



}
