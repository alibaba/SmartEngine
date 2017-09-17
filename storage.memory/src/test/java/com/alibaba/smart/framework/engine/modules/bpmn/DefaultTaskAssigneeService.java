package com.alibaba.smart.framework.engine.modules.bpmn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.common.service.TaskAssigneeService;
import com.alibaba.smart.framework.engine.constant.AssigneeTypeConstant;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;

/**
 * Created by 高海军 帝奇 74394 on 2017 January  18:03.
 */
public class DefaultTaskAssigneeService implements TaskAssigneeService {

    @Override
    public void persistTaskAssignee(TaskInstance taskInstance, TaskAssigneeInstance taskAssigneeInstance,
                                    Map<String, Object> variables) {

    }

    @Override
    public void complete(Long taskInstanceId) {

    }

    @Override
    public List<TaskAssigneeCandidateInstance> getTaskAssigneeCandidateInstance(Activity activity) {
        List<TaskAssigneeCandidateInstance> taskAssigneeCandidateInstanceList= new ArrayList();

        TaskAssigneeCandidateInstance taskAssigneeCandidateInstance = new TaskAssigneeCandidateInstance();
        taskAssigneeCandidateInstance.setAssigneeId("1");
        taskAssigneeCandidateInstance.setAssigneeType(AssigneeTypeConstant.USER);
        taskAssigneeCandidateInstanceList.add(taskAssigneeCandidateInstance);

        TaskAssigneeCandidateInstance taskAssigneeCandidateInstance1 = new TaskAssigneeCandidateInstance();
        taskAssigneeCandidateInstance1.setAssigneeId("3");
        taskAssigneeCandidateInstance1.setAssigneeType(AssigneeTypeConstant.USER);
        taskAssigneeCandidateInstanceList.add(taskAssigneeCandidateInstance1);


        TaskAssigneeCandidateInstance taskAssigneeCandidateInstance2 = new TaskAssigneeCandidateInstance();
        taskAssigneeCandidateInstance2.setAssigneeId("5");
        taskAssigneeCandidateInstance2.setAssigneeType(AssigneeTypeConstant.USER);
        taskAssigneeCandidateInstanceList.add(taskAssigneeCandidateInstance2);

        return taskAssigneeCandidateInstanceList;
    }

}
