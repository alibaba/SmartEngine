package com.alibaba.smart.framework.engine.model.instance;

/**
 * 执行实例 Created by ettear on 16-4-18.
 */
public interface ExecutionInstance extends LifeCycleInstance {

    String getProcessInstanceId();

    void setProcessInstanceId(String processInstanceId);

//    InstanceFact getFact();
//
//    void setFact(InstanceFact fact);

    ActivityInstance getActivity();

    void setActivity(ActivityInstance activityInstance);

    //通过异常来返回异常状态,fault 不应该侵入ExecutionInstance
//    boolean isFault();
//
//    void setFault(boolean fault);

    String toDatabase();


}
