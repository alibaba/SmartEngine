package com.alibaba.smart.framework.engine.configuration;

import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;

import java.util.List;
import java.util.Map;

/**
 * Created by 高海军 帝奇 74394 on 2017 January  11:13.
 */
public interface TaskAssigneeDispatcher {

    List<TaskAssigneeCandidateInstance> getTaskAssigneeCandidateInstance(Activity activity,Map<String,Object> request);

}
