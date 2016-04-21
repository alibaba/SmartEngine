package com.alibaba.smart.framework.engine.instance;

import java.util.Date;

/**
 * 生命周期实例
 * Created by ettear on 16-4-21.
 */
public interface LifeCycleInstance extends Instance {
    InstanceStatus getStatus();

    void setStatus(InstanceStatus status);

    Date getStartDate();

    void setStartDate(Date startDate);

    Date getCompleteDate();

    void setCompleteDate(Date completeDate);

    boolean isSuspend();

    //void setSuspend(boolean suspend);


}
