package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.*;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskAssigneeInstance;
import com.alibaba.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.persister.database.builder.TaskAssigneeInstanceBuilder;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskAssigneeDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskAssigneeEntity;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;

import static com.alibaba.smart.framework.engine.persister.common.constant.StorageConstant.NOT_IMPLEMENT_INTENTIONALLY;

@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = TaskAssigneeStorage.class)

public class RelationshipDatabaseTaskAssigneeInstanceStorage implements TaskAssigneeStorage {

    @Override
    public List<TaskAssigneeInstance> findList(String taskInstanceId,String tenantId,
                                               ProcessEngineConfiguration processEngineConfiguration) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) processEngineConfiguration.getInstanceAccessor().access("taskAssigneeDAO");
        List<TaskAssigneeEntity> taskAssigneeEntityList =  taskAssigneeDAO.findList(Long.valueOf(taskInstanceId),tenantId);

        List<TaskAssigneeInstance> taskAssigneeInstanceList= null;
        if(null != taskAssigneeEntityList){
            taskAssigneeInstanceList = new ArrayList<TaskAssigneeInstance>(taskAssigneeEntityList.size());
            for (TaskAssigneeEntity taskAssigneeEntity : taskAssigneeEntityList) {
                TaskAssigneeInstance taskAssigneeInstance = TaskAssigneeInstanceBuilder.buildTaskAssigneeInstance(taskAssigneeEntity);
                taskAssigneeInstanceList.add(taskAssigneeInstance);
            }
        }

        return taskAssigneeInstanceList;
    }

    @Override
    public Map<String, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(List<String> taskInstanceIdList,String tenantId,
                                                                              ProcessEngineConfiguration processEngineConfiguration) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) processEngineConfiguration.getInstanceAccessor().access("taskAssigneeDAO");

        Map<String, List<TaskAssigneeInstance>> assigneeMap = null;
        if (taskInstanceIdList != null && taskInstanceIdList.size() > 0) {

            assigneeMap = new HashMap<String, List<TaskAssigneeInstance>>();

            List<Long> longList = new ArrayList<Long>(taskInstanceIdList.size());
            for (String s : taskInstanceIdList) {
                longList.add(Long.valueOf(s));
            }

            List<TaskAssigneeEntity> taskAssigneeEntityList =  taskAssigneeDAO.findListForInstanceList(longList,tenantId);


            for (TaskAssigneeEntity entity: taskAssigneeEntityList) {
                TaskAssigneeInstance taskAssigneeInstance = TaskAssigneeInstanceBuilder.buildTaskAssigneeInstance(entity);
                List<TaskAssigneeInstance> assigneeListForTaskInstance = assigneeMap.get(entity.getTaskInstanceId().toString());
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
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) processEngineConfiguration.getInstanceAccessor().access("taskAssigneeDAO");

        TaskAssigneeEntity taskAssigneeEntity = TaskAssigneeInstanceBuilder.buildTaskInstanceEntity(  taskAssigneeInstance);
        taskAssigneeDAO.insert(taskAssigneeEntity);

        Long entityId = taskAssigneeEntity.getId();

        // 当数据库表id 是非自增时，需要以传入的 id 值为准
        if(0L == entityId){
            entityId = Long.valueOf(taskAssigneeInstance.getInstanceId());
        }

        TaskAssigneeInstance resultTaskAssigneeInstance = this.findOne(
                entityId.toString(), taskAssigneeInstance.getTenantId(), processEngineConfiguration);

        return resultTaskAssigneeInstance;
    }

    @Override
    public TaskAssigneeInstance update(String taskAssigneeInstanceId, String assigneeId,String tenantId,
                                       ProcessEngineConfiguration processEngineConfiguration) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) processEngineConfiguration.getInstanceAccessor().access("taskAssigneeDAO");
        taskAssigneeDAO.update(Long.valueOf(taskAssigneeInstanceId), assigneeId,tenantId);
        TaskAssigneeInstance resultTaskAssigneeInstance =    this.findOne(taskAssigneeInstanceId,tenantId, processEngineConfiguration);
        return resultTaskAssigneeInstance;
    }

    @Override
    public TaskAssigneeInstance findOne(String taskAssigneeInstanceId,String tenantId,
                                        ProcessEngineConfiguration processEngineConfiguration) {

        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) processEngineConfiguration.getInstanceAccessor().access("taskAssigneeDAO");
        TaskAssigneeEntity taskAssigneeEntity =  taskAssigneeDAO.findOne(Long.valueOf(taskAssigneeInstanceId),tenantId);
        if (taskAssigneeEntity == null){
            return null;
        }
        return TaskAssigneeInstanceBuilder.buildTaskAssigneeInstance(taskAssigneeEntity);
    }

    @Override
    public void remove(String taskAssigneeInstanceId,String tenantId,
                       ProcessEngineConfiguration processEngineConfiguration) {
        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) processEngineConfiguration.getInstanceAccessor().access("taskAssigneeDAO");
        taskAssigneeDAO.delete(Long.valueOf(taskAssigneeInstanceId),tenantId);

    }

    @Override
    public void removeAll(String taskInstanceId, String tenantId,ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }
}
