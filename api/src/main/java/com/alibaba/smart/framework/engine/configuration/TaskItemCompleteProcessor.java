package com.alibaba.smart.framework.engine.configuration;

import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.Activity;

public interface TaskItemCompleteProcessor {

    void PostProcessBeforeTaskItemComplete(String processInstanceId, String activityInstanceId, String taskInstanceId,
                                           String taskItemInstanceId, Map<String, Object> variables, Activity activity, SmartEngine smartEngine);

    void PostProcessAfterTaskItemComplete(String processInstanceId, String activityInstanceId, String taskInstanceId,
                                          String taskItemInstanceId, Map<String, Object> variables, Activity activity, SmartEngine smartEngine);

    /**
     * 是否可以关闭当前主任务
     * 返回值： key=canDrive(是否可以关闭当前主任务)  key=tag(如果可以驱动到下一个节点，则当前节点的审核结果的标签值)
     * 例子：Map<String,String> map = new HashMap<>;
     *      map.put("canDrive","true");
     *      map.put("tag","pass");
     */
    Map<String,String> canCompleteCurrentMainTask(String processInstanceId, String taskInstanceId, Activity activity, SmartEngine smartEngine);
}
