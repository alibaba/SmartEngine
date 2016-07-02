package com.alibaba.smart.framework.engine.model;

import java.util.List;

/**
 * 活动实例 Created by ettear on 16-4-18.
 */
public interface ActivityInstance extends LifeCycleInstance {

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

    /**
     * 获取当前阶段
     * 
     * @return 当前阶段
     */
    String getCurrentStep();

    /**
     * 设置当前阶段
     * 
     * @param step 当前阶段
     */
    void setCurrentStep(String step);

    /**
     * 获取任务实例
     * 
     * @return 任务实例
     */
    TaskInstance getTask();

    /**
     * 设置任务实例
     * 
     * @param taskInstance 任务实例
     */
    void setTask(TaskInstance taskInstance);
}
