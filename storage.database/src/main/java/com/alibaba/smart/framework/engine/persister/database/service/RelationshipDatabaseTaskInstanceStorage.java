package com.alibaba.smart.framework.engine.persister.database.service;

import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskInstance;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.ProcessInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.persister.util.SpringContextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class RelationshipDatabaseTaskInstanceStorage implements TaskInstanceStorage {


    @Override
    public List<TaskInstance> findPendingTask(Long processInstanceId) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        List<TaskInstanceEntity>  taskInstanceEntityList= taskInstanceDAO.findPendingTask(processInstanceId);

        List<TaskInstance> taskInstanceList = new ArrayList<>(taskInstanceEntityList.size());
        for (TaskInstanceEntity taskInstanceEntity : taskInstanceEntityList) {

            if(null == taskInstanceEntity.getEndTime()){
                TaskInstance taskInstance= buildTaskInstance(taskInstanceEntity);

                taskInstanceList.add(taskInstance);

            }
        }

        return taskInstanceList;
    }

    @Override
    public TaskInstance save(TaskInstance taskInstance) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");

        TaskInstanceEntity taskInstanceEntity = new TaskInstanceEntity();

        taskInstanceEntity.setProcessDefinitionId(taskInstance.getActivityId());
        taskInstanceEntity.setProcessInstanceId(taskInstance.getProcessInstanceId());
        taskInstanceEntity.setActivityInstanceId(taskInstance.getActivityInstanceId());
        taskInstanceEntity.setExecutionInstanceId(taskInstance.getExecutionInstanceId());
        taskInstanceDAO.insert(taskInstanceEntity);

        //reAssign
        taskInstance= buildTaskInstance(taskInstanceEntity);


        return taskInstance;
    }

    @Override
    public TaskInstance find(Long instanceId) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        TaskInstanceEntity taskInstanceEntity =  taskInstanceDAO.findOne(instanceId);

        TaskInstance taskInstance= buildTaskInstance(taskInstanceEntity);
        return  taskInstance;
    }

    private TaskInstance buildTaskInstance(TaskInstanceEntity taskInstanceEntity) {
        TaskInstance taskInstance = new DefaultTaskInstance();
        taskInstance.setInstanceId(taskInstanceEntity.getId());
        taskInstance.setStartDate(taskInstanceEntity.getGmtCreate());
        taskInstance.setProcessDefinitionIdAndVersion(taskInstanceEntity.getProcessDefinitionId());
        taskInstance.setProcessInstanceId(taskInstanceEntity.getProcessInstanceId());
        taskInstance.setActivityInstanceId(taskInstanceEntity.getActivityInstanceId());
        taskInstance.setExecutionInstanceId(taskInstanceEntity.getExecutionInstanceId());
        // taskInstance.setActivityId(taskInstanceEntity.get);

        return taskInstance;
    }


    @Override
    public void remove(Long instanceId) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        taskInstanceDAO.delete(instanceId);

    }
}
