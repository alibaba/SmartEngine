package com.alibaba.smart.framework.engine.instance;

/**
 * 执行实例
 * Created by ettear on 16-4-18.
 */
public interface ExecutionInstance extends Instance {

    String getParentId();

    void setParentId(String parentId);

    String getProcessInstanceId();

    void setProcessInstanceId(String processInstanceId);

    ActivityInstance getActivity();

    void setActivity(ActivityInstance activityInstance);

    boolean isSuspend();

    void setSuspend(boolean suspend);

    boolean isFault();

    void setFault(boolean fault);
}
