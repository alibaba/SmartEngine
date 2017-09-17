package com.alibaba.smart.framework.engine.test.process;

import com.alibaba.smart.framework.engine.common.service.TaskAssigneeService;
import com.alibaba.smart.framework.engine.constant.AssigneeTypeConstant;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskAssigneeDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskAssigneeEntity;
import com.alibaba.smart.framework.engine.persister.util.SpringContextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 高海军 帝奇 74394 on 2017 January  18:03.
 */
public class DefaultTaskAssigneeService implements TaskAssigneeService {

    @Override
    public void persistTaskAssignee(TaskInstance taskInstance,TaskAssigneeInstance taskAssigneeInstance, Map<String, Object> variables) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");

        //TODO 这里可以根据自己的业务, 循环 build 多个TaskAssigneeEntity。这个是简单示例。

        TaskAssigneeEntity taskAssigneeEntity = buildTaskInstanceEntity(taskInstance,  taskAssigneeInstance, variables);
        taskAssigneeEntity.setId(null);
        taskAssigneeDAO.insert(taskAssigneeEntity);

    }


    private TaskAssigneeEntity buildTaskInstanceEntity(TaskInstance taskInstance,TaskAssigneeInstance taskAssigneeInstance, Map<String, Object> variables) {
        TaskAssigneeEntity taskAssigneeEntity = new TaskAssigneeEntity();

        taskAssigneeEntity.setId(taskInstance.getInstanceId());
        taskAssigneeEntity.setProcessDefinitionId(taskInstance.getActivityId());
        taskAssigneeEntity.setProcessInstanceId(taskInstance.getProcessInstanceId());
        taskAssigneeEntity.setTaskInstanceId(taskInstance.getInstanceId());
        taskAssigneeEntity.setAssigneeId(taskAssigneeInstance.getAssigneeId());
        taskAssigneeEntity.setAssigneeType(taskAssigneeInstance.getAssigneeType());


        //TODO 默认标题key约定
        //if(null != variables){
        //    //taskAssigneeEntity.setTitle((String)variables.get("title"));
        //}


        //taskAssigneeEntity.setClaimTime(taskInstance.getClaimTime());
        //taskAssigneeEntity.setEndTime(taskInstance.getEndTime());
        //taskAssigneeEntity.setPriority(taskInstance.getPriority());
        //taskAssigneeEntity.setGmtModified(taskInstance.getEndTime());
        return taskAssigneeEntity;
    }


    @Override
    public void complete(Long taskInstanceId) {

        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");
        List<TaskAssigneeEntity>  taskAssigneeEntityList = taskAssigneeDAO.findSameTask(taskInstanceId);
        //可以批处理删除
        for (TaskAssigneeEntity taskAssigneeEntity : taskAssigneeEntityList) {
            taskAssigneeDAO.delete(taskAssigneeEntity.getId());
        }

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
