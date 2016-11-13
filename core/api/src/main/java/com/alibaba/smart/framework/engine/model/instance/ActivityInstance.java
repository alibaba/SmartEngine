package com.alibaba.smart.framework.engine.model.instance;

import java.util.Date;
import java.util.List;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface ActivityInstance extends Instance{

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
    String getProcessInstanceId();

    /**
     * 设置流程实例ID
     * 
     * @param processInstanceId 流程实例ID
     */
    void setProcessInstanceId(String processInstanceId);

    void setExecutionInstance(ExecutionInstance executionInstance);

    ExecutionInstance getExecutionInstance();

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

//    /**
//     * 获取当前阶段
//     *
//     * @return 当前阶段
//     */
//    String getCurrentStep();
//
//    /**
//     * 设置当前阶段
//     *
//     * @param step 当前阶段
//     */
//    void setCurrentStep(String step);

    /**
     * 获取任务实例
     * 
     * @return 任务实例
     */
    TaskInstance getTaskInstance();

    /**
     * 设置任务实例
     * 
     * @param taskInstance 任务实例
     */
    void setTaskInstance(TaskInstance taskInstance);

    Date getStartDate();

    void setStartDate(Date startDate);

    Date getCompleteDate();

    void setCompleteDate(Date completeDate);
}
