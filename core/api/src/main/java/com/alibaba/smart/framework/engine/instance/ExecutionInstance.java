package com.alibaba.smart.framework.engine.instance;

/**
 * 执行实例
 * Created by ettear on 16-4-18.
 */
public interface ExecutionInstance extends LifeCycleInstance {

    String getProcessInstanceId();

    void setProcessInstanceId(String processInstanceId);

    ActivityInstance getActivity();

    void setActivity(ActivityInstance activityInstance);

    boolean isFault();

    void setFault(boolean fault);
}
