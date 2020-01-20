package com.alibaba.smart.framework.engine.model.instance;

import java.util.List;

/**
 * @author ettear
 * Created by ettear on 18/08/2017.
 */
public interface ExecutionHistoryInstance extends LifeCycleInstance {
    String getParentProcessInstanceId();

    void setParentProcessInstanceId(String parentProcessInstanceId);


    String getParentExecutionInstanceId();

    void setParentExecutionInstanceId(String parentExecutionInstanceId);

    String getProcessDefinitionIdAndVersion();

    void setProcessDefinitionIdAndVersion(String processDefinitionIdAndVersion);


    String getProcessInstanceId();

    void setProcessInstanceId(String processInstanceId);

    String getProcessDefinitionActivityId();

    void setProcessDefinitionActivityId(String activityId);


    String getActivityInstanceId();

    void setActivityInstanceId(String activityInstanceId);

    /**
     * 获取进入活动的关联
     *
     * @return 进入活动的关联
     */
    List<TransitionInstance> getIncomeTransitions();

    /**
     * 添加进入活动的关联
     *
     * @param transitionInstance 进入活动的关联
     */
    void addIncomeTransition(TransitionInstance transitionInstance);
}
