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

public class RelationshipDatabaseTaskAssigneeInstanceStorage implements TaskAssigneeStorage {

    @Override
    public List<TaskAssigneeInstance> findList(String taskInstanceId) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");
        List<TaskAssigneeEntity> taskAssigneeEntityList =  taskAssigneeDAO.findList(Long.valueOf(taskInstanceId));

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
    public Map<String, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(List<String> taskInstanceIdList) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");

        Map<String, List<TaskAssigneeInstance>> assigneeMap = null;
        if (taskInstanceIdList != null) {

            assigneeMap = new HashMap<String, List<TaskAssigneeInstance>>();

            List<Long> longList = new ArrayList<Long>(taskInstanceIdList.size());
            for (String s : taskInstanceIdList) {
                longList.add(Long.valueOf(s));
            }

            List<TaskAssigneeEntity> taskAssigneeEntityList =  taskAssigneeDAO.findListForInstanceList(longList);


            for (TaskAssigneeEntity entity: taskAssigneeEntityList) {
                TaskAssigneeInstance taskAssigneeInstance = buildTaskAssigneeInstance(entity);
                List<TaskAssigneeInstance> assigneeListForTaskInstance = assigneeMap.get(entity.getTaskInstanceId());
                if (assigneeListForTaskInstance == null) {
                    assigneeListForTaskInstance = new ArrayList<TaskAssigneeInstance>();
                    assigneeMap.put(entity.getTaskInstanceId().toString(), assigneeListForTaskInstance);
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

        TaskAssigneeInstance resultTaskAssigneeInstance =    this.findOne(taskAssigneeEntity.getId().toString());
        return resultTaskAssigneeInstance;
    }

    private TaskAssigneeEntity buildTaskInstanceEntity(TaskAssigneeInstance taskAssigneeInstance) {
        TaskAssigneeEntity taskAssigneeEntity = new TaskAssigneeEntity();

        taskAssigneeEntity.setProcessInstanceId(Long.valueOf(taskAssigneeInstance.getProcessInstanceId()));
        taskAssigneeEntity.setTaskInstanceId(Long.valueOf(taskAssigneeInstance.getTaskInstanceId()));
        taskAssigneeEntity.setAssigneeId(taskAssigneeInstance.getAssigneeId());
        taskAssigneeEntity.setAssigneeType(taskAssigneeInstance.getAssigneeType());
        String taskAssigneeInstanceId = taskAssigneeInstance.getInstanceId();
        if(null != taskAssigneeInstanceId){
            taskAssigneeEntity.setId(Long.valueOf(taskAssigneeInstanceId));
        }
        return taskAssigneeEntity;
    }


    @Override
    public TaskAssigneeInstance update(String taskAssigneeInstanceId, String assigneeId) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");
        taskAssigneeDAO.update(  Long.valueOf(taskAssigneeInstanceId), Long.valueOf( assigneeId));
        TaskAssigneeInstance resultTaskAssigneeInstance =    this.findOne(taskAssigneeInstanceId);
        return resultTaskAssigneeInstance;
    }

    @Override
    public TaskAssigneeInstance findOne(String taskAssigneeInstanceId) {

        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");
        TaskAssigneeEntity taskAssigneeEntity =  taskAssigneeDAO.findOne(Long.valueOf(taskAssigneeInstanceId));

        TaskAssigneeInstance taskAssigneeInstance = buildTaskAssigneeInstance(taskAssigneeEntity);

        return taskAssigneeInstance;
    }

    private TaskAssigneeInstance buildTaskAssigneeInstance(TaskAssigneeEntity taskAssigneeEntity) {
        TaskAssigneeInstance taskAssigneeInstance = new DefaultTaskAssigneeInstance();

        taskAssigneeInstance.setInstanceId(taskAssigneeEntity.getId().toString());
        taskAssigneeInstance.setProcessInstanceId(taskAssigneeEntity.getProcessInstanceId().toString());
        taskAssigneeInstance.setTaskInstanceId(taskAssigneeEntity.getTaskInstanceId().toString());


        taskAssigneeInstance.setAssigneeId(taskAssigneeEntity.getAssigneeId());
        taskAssigneeInstance.setAssigneeType(taskAssigneeEntity.getAssigneeType());

        taskAssigneeInstance.setStartTime(taskAssigneeEntity.getGmtCreate());
        taskAssigneeInstance.setCompleteTime(taskAssigneeEntity.getGmtModified());
        return taskAssigneeInstance;
    }

    @Override
    public void remove(String taskAssigneeInstanceId) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");
        taskAssigneeDAO.delete(Long.valueOf(taskAssigneeInstanceId));

    }
}
