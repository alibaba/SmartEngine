package com.alibaba.smart.framework.engine.model.instance;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
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
