package com.alibaba.smart.framework.engine.model.instance;

import java.util.List;

/**
 * @author ettear
 * Created by ettear on 18/08/2017.
 */
public interface ExecutionHistoryInstance extends LifeCycleInstance {
    Long getParentProcessInstanceId();

    void setParentProcessInstanceId(Long parentProcessInstanceId);


    Long getParentExecutionInstanceId();

    void setParentExecutionInstanceId(Long parentExecutionInstanceId);

    String getProcessDefinitionIdAndVersion();

    void setProcessDefinitionIdAndVersion(String processDefinitionIdAndVersion);


    Long getProcessInstanceId();

    void setProcessInstanceId(Long processInstanceId);

    String getProcessDefinitionActivityId();

    void setProcessDefinitionActivityId(String activityId);


    Long getActivityInstanceId();

    void setActivityInstanceId(Long activityInstanceId);

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
