package com.alibaba.smart.framework.engine.test.storage.dual.helper;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.configuration.TaskAssigneeDispatcher;
import com.alibaba.smart.framework.engine.constant.AssigneeTypeConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;

public class DefaultTaskAssigneeDispatcher implements TaskAssigneeDispatcher {

    @Override
    public List<TaskAssigneeCandidateInstance> getTaskAssigneeCandidateInstance(Activity activity, ExecutionContext context) {
        List<TaskAssigneeCandidateInstance> list = new ArrayList<>();

        TaskAssigneeCandidateInstance c1 = new TaskAssigneeCandidateInstance();
        c1.setAssigneeId("1");
        c1.setAssigneeType(AssigneeTypeConstant.USER);
        list.add(c1);

        TaskAssigneeCandidateInstance c2 = new TaskAssigneeCandidateInstance();
        c2.setAssigneeId("3");
        c2.setAssigneeType(AssigneeTypeConstant.USER);
        list.add(c2);

        TaskAssigneeCandidateInstance c3 = new TaskAssigneeCandidateInstance();
        c3.setAssigneeId("5");
        c3.setAssigneeType(AssigneeTypeConstant.USER);
        list.add(c3);

        return list;
    }
}
