package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskAssigneeInstance;
import com.alibaba.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskAssigneeDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskAssigneeEntity;
import com.alibaba.smart.framework.engine.persister.database.util.SpringContextUtil;

import com.google.common.collect.Lists;

public class RelationshipDatabaseTaskAssigneeInstanceStorage implements TaskAssigneeStorage {

    @Override
    public List<TaskAssigneeInstance> findList(Long taskInstanceId) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");
        List<TaskAssigneeEntity> taskAssigneeEntityList =  taskAssigneeDAO.findList(taskInstanceId);

        List<TaskAssigneeInstance> taskAssigneeInstanceList= null;
        if(null != taskAssigneeEntityList){
            taskAssigneeInstanceList = new ArrayList<TaskAssigneeInstance>(taskAssigneeEntityList.size());
            for (TaskAssigneeEntity taskAssigneeEntity : taskAssigneeEntityList) {
                TaskAssigneeInstance taskAssigneeInstance = buildTaskAssigneeInstance(taskAssigneeEntity);
                taskAssigneeInstanceList.add(taskAssigneeInstance);
            }
        }

        return taskAssigneeInstanceList;
    }

    @Override
    public Map<Long, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(List<Long> taskInstanceIdList) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");
        List<TaskAssigneeEntity> taskAssigneeEntityList =  taskAssigneeDAO.findListForInstanceList(taskInstanceIdList);

        Map<Long, List<TaskAssigneeInstance>> assigneeMap = new HashMap<Long, List<TaskAssigneeInstance>>();
        if (taskAssigneeEntityList != null) {
            for (TaskAssigneeEntity entity: taskAssigneeEntityList) {
                TaskAssigneeInstance taskAssigneeInstance = buildTaskAssigneeInstance(entity);
                List<TaskAssigneeInstance> assigneeListForTaskInstance = assigneeMap.get(entity.getTaskInstanceId());
                if (assigneeListForTaskInstance == null) {
                    assigneeListForTaskInstance = new ArrayList<TaskAssigneeInstance>();
                    assigneeMap.put(entity.getTaskInstanceId(), assigneeListForTaskInstance);
                }
                assigneeListForTaskInstance.add(taskAssigneeInstance);
            }
         }
         return assigneeMap;
    }

    @Override
    public TaskAssigneeInstance insert(TaskAssigneeInstance taskAssigneeInstance) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");

        TaskAssigneeEntity taskAssigneeEntity = buildTaskInstanceEntity(  taskAssigneeInstance);
        //taskAssigneeEntity.setId(null);
        taskAssigneeDAO.insert(taskAssigneeEntity);

        TaskAssigneeInstance resultTaskAssigneeInstance =    this.findOne(taskAssigneeEntity.getId());
        return resultTaskAssigneeInstance;
    }

    @Override
    public void batchInsert(List<TaskAssigneeInstance> taskAssigneeInstanceList) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");
        List<TaskAssigneeEntity> taskAssigneeEntityList = Lists.newArrayList();
        for(TaskAssigneeInstance taskAssigneeInstance :taskAssigneeInstanceList){
            TaskAssigneeEntity taskAssigneeEntity = buildTaskInstanceEntity(  taskAssigneeInstance);
            taskAssigneeEntityList.add(taskAssigneeEntity);
        }
        taskAssigneeDAO.batchInsert(taskAssigneeEntityList);
    }

    private TaskAssigneeEntity buildTaskInstanceEntity(TaskAssigneeInstance taskAssigneeInstance) {
        TaskAssigneeEntity taskAssigneeEntity = new TaskAssigneeEntity();

        taskAssigneeEntity.setProcessInstanceId(taskAssigneeInstance.getProcessInstanceId());
        taskAssigneeEntity.setTaskInstanceId(taskAssigneeInstance.getTaskInstanceId());
        taskAssigneeEntity.setAssigneeId(taskAssigneeInstance.getAssigneeId());
        taskAssigneeEntity.setAssigneeType(taskAssigneeInstance.getAssigneeType());
        taskAssigneeEntity.setId(taskAssigneeInstance.getInstanceId());
        return taskAssigneeEntity;
    }


    @Override
    public TaskAssigneeInstance update(Long taskAssigneeInstanceId,String assigneeId) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");
        taskAssigneeDAO.update(  taskAssigneeInstanceId,  assigneeId);
        TaskAssigneeInstance resultTaskAssigneeInstance =    this.findOne(taskAssigneeInstanceId);
        return resultTaskAssigneeInstance;
    }

    @Override
    public TaskAssigneeInstance findOne(Long taskAssigneeInstanceId) {

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
