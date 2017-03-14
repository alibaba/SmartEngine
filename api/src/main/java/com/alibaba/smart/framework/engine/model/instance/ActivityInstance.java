package com.alibaba.smart.framework.engine.model.instance;

import java.util.Date;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface ActivityInstance extends Instance {

    String getProcessDefinitionIdAndVersion();

    void setProcessDefinitionIdAndVersion(String processDefinitionIdAndVersion);


    /**
     * 获取活动ID
     *
     * @return 活动ID
     */
    String getActivityId();

    /**
     * 设置活动ID
     *
     * @param activityId 活动ID
     */
    void setActivityId(String activityId);

    /**
     * 获取流程实例ID
     *
     * @return 流程实例ID
     */
    Long getProcessInstanceId();

    /**
     * 设置流程实例ID
     *
     * @param processInstanceId 流程实例ID
     */
    void setProcessInstanceId(Long processInstanceId);

    void setExecutionInstance(ExecutionInstance executionInstance);

    ExecutionInstance getExecutionInstance();
//
//    /**
//     * 获取进入活动的关联
//     *
//     * @return 进入活动的关联
//     */
//    List<TransitionInstance> getIncomeTransitions();
//
//    /**
//     * 添加进入活动的关联
//     *
//     * @param transitionInstance 进入活动的关联
//     */
//    void addIncomeTransition(TransitionInstance transitionInstance);


    Date getStartDate();

    void setStartDate(Date startDate);

    Date getCompleteDate();

    void setCompleteDate(Date completeDate);

    Long getBlockId();

    void setBlockId(Long blockId);
}