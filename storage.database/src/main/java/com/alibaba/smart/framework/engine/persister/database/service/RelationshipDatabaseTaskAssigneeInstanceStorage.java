package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskAssigneeInstance;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskInstance;
import com.alibaba.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.ProcessInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskAssigneeDAO;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskAssigneeEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.persister.util.SpringContextUtil;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

public class RelationshipDatabaseTaskAssigneeInstanceStorage implements TaskAssigneeStorage {

    @Override
    public List<TaskAssigneeInstance> findPendingTask(Long processInstanceId) {
        return null;
    }

    @Override
    public TaskAssigneeInstance insert(TaskAssigneeInstance taskAssigneeInstance) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");

        TaskAssigneeEntity taskAssigneeEntity = buildTaskInstanceEntity(  taskAssigneeInstance);
        taskAssigneeEntity.setId(null);
        taskAssigneeDAO.insert(taskAssigneeEntity);

        TaskAssigneeInstance resultTaskAssigneeInstance =    this.find(taskAssigneeEntity.getId());
        return resultTaskAssigneeInstance;
    }

    private TaskAssigneeEntity buildTaskInstanceEntity(TaskAssigneeInstance taskAssigneeInstance) {
        TaskAssigneeEntity taskAssigneeEntity = new TaskAssigneeEntity();

        taskAssigneeEntity.setProcessDefinitionIdAndVersion(taskAssigneeInstance.getProcessDefinitionIdAndVersion());
        taskAssigneeEntity.setProcessInstanceId(taskAssigneeInstance.getProcessInstanceId());
        taskAssigneeEntity.setTaskInstanceId(taskAssigneeInstance.getTaskInstanceId());
        taskAssigneeEntity.setAssigneeId(taskAssigneeInstance.getAssigneeId());
        taskAssigneeEntity.setAssigneeType(taskAssigneeInstance.getAssigneeType());

        return taskAssigneeEntity;
    }


    @Override
    public TaskAssigneeInstance update(Long taskAssigneeInstanceId,String assigneeId) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");
        taskAssigneeDAO.update(  taskAssigneeInstanceId,  assigneeId);
        TaskAssigneeInstance resultTaskAssigneeInstance =    this.find(taskAssigneeInstanceId);
        return resultTaskAssigneeInstance;
    }

    @Override
    public TaskAssigneeInstance find(Long taskAssigneeInstanceId) {

        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");
        TaskAssigneeEntity taskAssigneeEntity =  taskAssigneeDAO.findOne(taskAssigneeInstanceId);

        TaskAssigneeInstance taskAssigneeInstance = buildTaskAssigneeInstance(taskAssigneeEntity);

        return taskAssigneeInstance;
    }

    private TaskAssigneeInstance buildTaskAssigneeInstance(TaskAssigneeEntity taskAssigneeEntity) {
        TaskAssigneeInstance taskAssigneeInstance = new DefaultTaskAssigneeInstance();

        taskAssigneeInstance.setInstanceId(taskAssigneeEntity.getId());
        taskAssigneeInstance.setProcessInstanceId(taskAssigneeEntity.getProcessInstanceId());
        taskAssigneeInstance.setTaskInstanceId(taskAssigneeEntity.getTaskInstanceId());

        taskAssigneeInstance.setProcessDefinitionIdAndVersion(taskAssigneeEntity.getProcessDefinitionIdAndVersion());

        taskAssigneeInstance.setAssigneeId(taskAssigneeEntity.getAssigneeId());
        taskAssigneeInstance.setAssigneeType(taskAssigneeEntity.getAssigneeType());

        taskAssigneeInstance.setStartTime(taskAssigneeEntity.getGmtCreate());
        taskAssigneeInstance.setCompleteTime(taskAssigneeEntity.getGmtModified());
        return taskAssigneeInstance;
    }

    @Override
    public void remove(Long taskAssigneeInstanceId) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");
        taskAssigneeDAO.delete(taskAssigneeInstanceId);

    }
}
