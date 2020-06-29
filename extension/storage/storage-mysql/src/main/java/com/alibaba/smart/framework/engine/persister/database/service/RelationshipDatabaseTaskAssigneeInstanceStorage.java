package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskAssigneeInstance;
import com.alibaba.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.persister.common.util.SpringContextUtil;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskAssigneeDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskAssigneeEntity;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;

import static com.alibaba.smart.framework.engine.persister.common.constant.StorageConstant.NOT_IMPLEMENT_INTENTIONALLY;

@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = TaskAssigneeStorage.class)

public class RelationshipDatabaseTaskAssigneeInstanceStorage implements TaskAssigneeStorage {

    @Override
    public List<TaskAssigneeInstance> findList(String taskInstanceId,
                                               ProcessEngineConfiguration processEngineConfiguration) {
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
    public Map<String, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(List<String> taskInstanceIdList,
                                                                              ProcessEngineConfiguration processEngineConfiguration) {
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
    public List<TaskAssigneeInstance> findPendingTaskAssigneeList(PendingTaskQueryParam pendingTaskQueryParam,
                                                                  ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }

    @Override
    public Long countPendingTaskAssigneeList(PendingTaskQueryParam pendingTaskQueryParam,
                                             ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }

    @Override
    public TaskAssigneeInstance insert(TaskAssigneeInstance taskAssigneeInstance,
                                       ProcessEngineConfiguration processEngineConfiguration) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");

        TaskAssigneeEntity taskAssigneeEntity = buildTaskInstanceEntity(  taskAssigneeInstance);
        taskAssigneeDAO.insert(taskAssigneeEntity);

        Long entityId = taskAssigneeEntity.getId();

        // 当数据库表id 是非自增时，需要以传入的 id 值为准
        if(0L == entityId){
            entityId = Long.valueOf(taskAssigneeInstance.getInstanceId());
        }

        TaskAssigneeInstance resultTaskAssigneeInstance =    this.findOne(entityId.toString(),processEngineConfiguration );
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
    public TaskAssigneeInstance update(String taskAssigneeInstanceId, String assigneeId,
                                       ProcessEngineConfiguration processEngineConfiguration) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");
        taskAssigneeDAO.update(  Long.valueOf(taskAssigneeInstanceId), Long.valueOf( assigneeId));
        TaskAssigneeInstance resultTaskAssigneeInstance =    this.findOne(taskAssigneeInstanceId, processEngineConfiguration);
        return resultTaskAssigneeInstance;
    }

    @Override
    public TaskAssigneeInstance findOne(String taskAssigneeInstanceId,
                                        ProcessEngineConfiguration processEngineConfiguration) {

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
    public void remove(String taskAssigneeInstanceId,
                       ProcessEngineConfiguration processEngineConfiguration) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");
        taskAssigneeDAO.delete(Long.valueOf(taskAssigneeInstanceId));

    }

    @Override
    public void removeAll(String taskInstanceId, ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }
}
