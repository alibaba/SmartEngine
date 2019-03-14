package com.alibaba.smart.framework.engine.configuration;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.Activity;

public interface TaskItemCompleteProcessor {

    void postProcessBeforeTaskItemComplete(String processInstanceId, String activityInstanceId, String taskInstanceId,
                                           List<String> taskItemInstanceId, Map<String, Object> variables, Activity activity, SmartEngine smartEngine);

    void postProcessAfterTaskItemComplete(String processInstanceId, String activityInstanceId, String taskInstanceId,
                                          List<String> taskItemInstanceId, Map<String, Object> variables, Activity activity, SmartEngine smartEngine);

    /**
     * 是否可以关闭当前主任务
     * 返回值： key=canComplete(是否可以关闭当前主任务)  key=tag(如果可以驱动到下一个节点，则当前节点的审核结果的标签值)
     * 例子：Map<String,String> map = new HashMap<>;
     *      map.put("canComplete","true");
     *      map.put("tag","pass");
     */
    Map<String,String> canCompleteCurrentMainTask(String processInstanceId, String taskInstanceId, Activity activity, SmartEngine smartEngine);

    /**
     * 根据活动实例id,获取审批通过的子业务ID
     * @param activityInstanceId
     * @return
     */
    List<String> getPassedSubBizIdByActivityInstanceId(String activityInstanceId, SmartEngine smartEngine);
}
