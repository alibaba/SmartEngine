package com.alibaba.smart.framework.engine.instance;

import java.util.List;

/**
 * 活动实例
 * Created by ettear on 16-4-18.
 */
public interface ActivityInstance extends Instance{
    String getActivityId();
    String getProcessInstanceId();
    List<TransitionInstance> getIncomeTransitions();
    String getCurrentStep();
    TaskInstance getTask();
    void setTask(TaskInstance taskInstance);
}
