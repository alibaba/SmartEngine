package com.alibaba.smart.framework.example;

import com.alibaba.smart.framework.engine.common.service.TaskAssigneeService;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskAssigneeDAO;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskAssigneeEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.persister.util.SpringContextUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by 高海军 帝奇 74394 on 2017 January  18:03.
 */
public class DefaultTaskAssigneeService implements TaskAssigneeService {
    @Override
    public void persistTaskAssignee(TaskInstance taskInstance, Map<String, Object> variables) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");

        //TODO 这里可以根据自己的业务, 循环 build 多个TaskAssigneeEntity。这个是简单示例。
        String assigneeId = taskInstance.getAssigneeId();

        TaskAssigneeEntity taskAssigneeEntity = buildTaskInstanceEntity(taskInstance,variables,assigneeId);
        taskAssigneeEntity.setId(null);
        taskAssigneeDAO.insert(taskAssigneeEntity);

    }


    private TaskAssigneeEntity buildTaskInstanceEntity(TaskInstance taskInstance,Map<String, Object> variables, String assigneeId) {
        TaskAssigneeEntity taskAssigneeEntity = new TaskAssigneeEntity();

        taskAssigneeEntity.setId(taskInstance.getInstanceId());
        taskAssigneeEntity.setProcessDefinitionId(taskInstance.getActivityId());
        taskAssigneeEntity.setProcessInstanceId(taskInstance.getProcessInstanceId());
        taskAssigneeEntity.setActivityInstanceId(taskInstance.getActivityInstanceId());
        taskAssigneeEntity.setExecutionInstanceId(taskInstance.getExecutionInstanceId());
        taskAssigneeEntity.setTaskInstanceId(taskInstance.getInstanceId());

        //TODO 默认标题key约定
        if(null != variables){
            taskAssigneeEntity.setTitle((String)variables.get("title"));
        }

        taskAssigneeEntity.setAssigneeId(assigneeId);
        taskAssigneeEntity.setClaimTime(taskInstance.getClaimTime());
        taskAssigneeEntity.setEndTime(taskInstance.getEndTime());
        taskAssigneeEntity.setPriority(taskInstance.getPriority());
        taskAssigneeEntity.setGmtModified(taskInstance.getEndTime());
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
}
