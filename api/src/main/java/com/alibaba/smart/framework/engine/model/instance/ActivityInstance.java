package com.alibaba.smart.framework.engine.model.instance;

import java.util.Date;
import java.util.List;

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
    String getProcessDefinitionActivityId();

    /**
     * 设置活动ID
     *
     */
    void setProcessDefinitionActivityId(String processDefinitionActivityId);

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

    void setExecutionInstanceList(List<ExecutionInstance> executionInstanceList);

    List<ExecutionInstance> getExecutionInstanceList();
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


    Date getStartTime();

    void setStartTime(Date startTime);

    Date getCompleteTime();

    void setCompleteTime(Date completeTime);

    String getBlockId();

    void setBlockId(String blockId);
}
