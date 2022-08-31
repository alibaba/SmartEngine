package com.alibaba.smart.framework.engine.persister.mongo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.TableSchemaStrategy;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskInstance;
import com.alibaba.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.mongo.entity.TaskAssigneeEntity;
import com.alibaba.smart.framework.engine.persister.mongo.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant.ACTIVITY_INSTANCE_ID;
import static com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant.CLAIM_USER_ID;
import static com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant.GMT_CREATE;
import static com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant.MONGO_TEMPLATE;
import static com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant.PROCESS_DEFINITION_ACTIVITY_ID;
import static com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant.PROCESS_DEFINITION_TYPE;
import static com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant.PROCESS_INSTANCE_ID;
import static com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant.STATUS;
import static com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant.TAG;


/**
 * Created by 高海军 帝奇 74394 on 2018 October  21:52.
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = TaskInstanceStorage.class)

public class MongoTaskInstanceStorage implements TaskInstanceStorage {

    private static final String INSTANCE = "se_task_instance";

    /**
     *
     * 查询单个 collection： se_task_instance ，param 中的参数 claimUserId 是指完成指定任务的人。
     * @param taskInstanceQueryParam
     * @param processEngineConfiguration
     * @return
     */
    @Override
    public List<TaskInstance> findTaskList(TaskInstanceQueryParam taskInstanceQueryParam,
                                           ProcessEngineConfiguration processEngineConfiguration) {


        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        Query query = buildQuery(taskInstanceQueryParam);

        List<TaskInstanceEntity> entityList = mongoTemplate.find(query,TaskInstanceEntity.class,collectionName);


        if(null != entityList){
            List<TaskInstance> instanceList = new ArrayList<TaskInstance>(entityList.size());

            for (TaskInstanceEntity entity : entityList) {
                TaskInstance instance = buildInstance(entity);
                instanceList.add(instance);
            }

            return instanceList;
        }

        return Collections.emptyList();

    }

    private TaskInstance buildInstance(TaskInstanceEntity entity) {
        TaskInstance taskInstance= new DefaultTaskInstance();
        taskInstance.setInstanceId(entity.getId());
        taskInstance.setProcessDefinitionIdAndVersion(entity.getProcessDefinitionIdAndVersion());
        taskInstance.setProcessInstanceId(entity.getProcessInstanceId());
        taskInstance.setProcessDefinitionActivityId(entity.getProcessDefinitionActivityId());
        taskInstance.setProcessDefinitionType(entity.getProcessDefinitionType());
        taskInstance.setExecutionInstanceId(entity.getExecutionInstanceId());
        taskInstance.setActivityInstanceId(entity.getActivityInstanceId());
        taskInstance.setClaimUserId(entity.getClaimUserId());
        taskInstance.setStatus(entity.getStatus());
        taskInstance.setTag(entity.getTag());
        taskInstance.setPriority(entity.getPriority());
        taskInstance.setClaimTime(entity.getClaimTime());
        taskInstance.setComment(entity.getComment());
        taskInstance.setExtension(entity.getExtension());
        taskInstance.setTitle(entity.getTitle());
        return taskInstance;

    }

    private Query buildQuery(TaskInstanceQueryParam taskInstanceQueryParam) {
        Query query = new Query();

        if(null !=  taskInstanceQueryParam.getProcessDefinitionType()){
            query.addCriteria(Criteria.where(PROCESS_DEFINITION_TYPE).is(taskInstanceQueryParam.getProcessDefinitionType()));
        }

        if(null !=  taskInstanceQueryParam.getProcessInstanceIdList()){
            query.addCriteria(Criteria.where(PROCESS_INSTANCE_ID).in(taskInstanceQueryParam.getProcessInstanceIdList()));
        }

        if(null !=  taskInstanceQueryParam.getProcessDefinitionActivityId()){
            query.addCriteria(Criteria.where(PROCESS_DEFINITION_ACTIVITY_ID).is(taskInstanceQueryParam.getProcessDefinitionActivityId()));
        }

        if(null !=  taskInstanceQueryParam.getActivityInstanceId()){
            query.addCriteria(Criteria.where(ACTIVITY_INSTANCE_ID).is(taskInstanceQueryParam.getActivityInstanceId()));
        }

        if(null !=  taskInstanceQueryParam.getStatus()){
            query.addCriteria(Criteria.where(STATUS).is(taskInstanceQueryParam.getStatus()));
        }

        if(null !=  taskInstanceQueryParam.getClaimUserId()){
            query.addCriteria(Criteria.where(CLAIM_USER_ID).is(taskInstanceQueryParam.getClaimUserId()));
        }

        if(null !=  taskInstanceQueryParam.getTag()){
            query.addCriteria(Criteria.where(TAG).is(taskInstanceQueryParam.getTag()));
        }


        if(null != taskInstanceQueryParam.getPageOffset()){
            Pageable pageableRequest =   PageRequest.of(taskInstanceQueryParam.getPageOffset(),taskInstanceQueryParam.getPageSize());
            query.with(pageableRequest);
        }

        query.with(   Sort.by(Sort.Direction.ASC, GMT_CREATE));
        return query;
    }

    /**
     *
     * 同上，计数用。
     * @param taskInstanceQueryParam
     * @param processEngineConfiguration
     * @return
     */
    @Override
    public Long count(TaskInstanceQueryParam taskInstanceQueryParam,
                      ProcessEngineConfiguration processEngineConfiguration) {

        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        Query query = buildQuery(taskInstanceQueryParam);

        return mongoTemplate.count(query,collectionName);
    }

    /**
     * PendingTaskQueryParam # assigneeUserId 不能为空 ，# assigneeGroupIdList 则必须为空（当前不支持根据群组查询。 ）。
     *
     * @param pendingTaskQueryParam
     * @param processEngineConfiguration
     * @return
     */
    @Override
    public List<TaskInstance> findPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam,
                                                  ProcessEngineConfiguration processEngineConfiguration) {
        // 该算法逻辑依赖：  先 insert 任务实例时，需要插入任务的处理者。 同时，需要保存另一个 collectionA，当前候选者对应的任务实例 list
        // 2. 完成任务时， 更新任务实例状态，子 collection 不需要处理。 同时，将collectionA 的任务实例置为 completed。
        // 3. 删除或者 abort 流程实例时，对应的冗余数据也需要处理掉。

        //下面的待办逻辑是：先从 TaskAssignee 中查询

        TaskAssigneeStorage  taskAssigneeStorage =  processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.COMMON,TaskAssigneeStorage.class);
        List<TaskAssigneeInstance>  taskAssigneeInstanceList =  taskAssigneeStorage.findPendingTaskAssigneeList(pendingTaskQueryParam,processEngineConfiguration);

        if(CollectionUtil.isNotEmpty(taskAssigneeInstanceList)){

            List<String> taskInstanceIdList = new ArrayList<String>(taskAssigneeInstanceList.size());

            for (TaskAssigneeInstance taskAssigneeInstance : taskAssigneeInstanceList) {
                taskInstanceIdList.add(taskAssigneeInstance.getTaskInstanceId());
            }

            List<TaskInstance>  taskInstanceList = findList(taskInstanceIdList,  processEngineConfiguration) ;
            return taskInstanceList;
        }



        return Collections.emptyList();
    }

    @Override
    public Long countPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam,
                                     ProcessEngineConfiguration processEngineConfiguration) {

        TaskAssigneeStorage  taskAssigneeStorage =  processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.COMMON,TaskAssigneeStorage.class);

        Long  counter =  taskAssigneeStorage.countPendingTaskAssigneeList(pendingTaskQueryParam,processEngineConfiguration);
        return counter;

    }


    @Override
    public void remove(String taskInstanceId, ProcessEngineConfiguration processEngineConfiguration) {
        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);


        TaskInstanceEntity entity =  mongoTemplate.findById(taskInstanceId,TaskInstanceEntity.class,
            collectionName);

        mongoTemplate.remove(entity,collectionName);
    }





    @Override
    public TaskInstance insert(TaskInstance instance, ProcessEngineConfiguration processEngineConfiguration) {
        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        TaskInstanceEntity entity = buildEntity(instance);

        mongoTemplate.insert(entity, collectionName);

        instance.setInstanceId(entity.getId());

        return  instance;

    }

    private TaskInstanceEntity buildEntity(TaskInstance instance) {
        TaskInstanceEntity taskInstanceEntity= new TaskInstanceEntity();
        taskInstanceEntity.setProcessInstanceId(instance.getProcessInstanceId());
        taskInstanceEntity.setProcessDefinitionIdAndVersion(instance.getProcessDefinitionIdAndVersion());
        taskInstanceEntity.setExecutionInstanceId(instance.getExecutionInstanceId());
        taskInstanceEntity.setActivityInstanceId(instance.getActivityInstanceId());
        taskInstanceEntity.setProcessDefinitionType(instance.getProcessDefinitionType());
        taskInstanceEntity.setProcessDefinitionActivityId(instance.getProcessDefinitionActivityId());
        taskInstanceEntity.setClaimUserId(instance.getClaimUserId());
        taskInstanceEntity.setPriority(instance.getPriority());
        taskInstanceEntity.setStatus(instance.getStatus());
        taskInstanceEntity.setTag(instance.getTag());
        taskInstanceEntity.setClaimTime(instance.getClaimTime());
        taskInstanceEntity.setComment(instance.getComment());
        taskInstanceEntity.setExtension(instance.getExtension());
        taskInstanceEntity.setTitle(instance.getTitle());

        Date now = DateUtil.getCurrentDate();
        taskInstanceEntity.setGmtCreate(now);
        taskInstanceEntity.setGmtModified(now);


        List<TaskAssigneeInstance> taskAssigneeInstanceList = instance.getTaskAssigneeInstanceList();
        if(CollectionUtil.isNotEmpty(taskAssigneeInstanceList)){
            List<TaskAssigneeEntity> assigneeList  = new ArrayList<TaskAssigneeEntity>(taskAssigneeInstanceList.size());

            for (TaskAssigneeInstance taskAssigneeInstance : taskAssigneeInstanceList) {
                TaskAssigneeEntity taskAssigneeEntity = new TaskAssigneeEntity();
                taskAssigneeEntity.setProcessInstanceId(taskAssigneeInstance.getProcessInstanceId());
                taskAssigneeEntity.setTaskInstanceId(taskAssigneeInstance.getTaskInstanceId());
                taskAssigneeEntity.setAssigneeId(taskAssigneeInstance.getAssigneeId());
                taskAssigneeEntity.setAssigneeType(taskAssigneeInstance.getAssigneeType());
                taskAssigneeEntity.setGmtCreate(now);
                taskAssigneeEntity.setGmtModified(now);
                assigneeList.add(taskAssigneeEntity);

            }

            taskInstanceEntity.setAssigneeList(assigneeList);

        }

        return taskInstanceEntity;

    }

    @Override
    public TaskInstance update(TaskInstance instance, ProcessEngineConfiguration processEngineConfiguration) {

        save(instance, processEngineConfiguration);

        return instance;

    }



    @Override
    public TaskInstance find(String instanceId, ProcessEngineConfiguration processEngineConfiguration) {
        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        TaskInstanceEntity entity =  mongoTemplate.findById(instanceId,TaskInstanceEntity.class,collectionName);

        TaskInstance instance = buildInstance(entity);

        return instance;

    }

    public List<TaskInstance> findList(List<String> taskInstanceIdList, ProcessEngineConfiguration processEngineConfiguration) {
        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        Query query = new Query();

        if(CollectionUtil.isNotEmpty(taskInstanceIdList)){
            query.addCriteria(Criteria.where("id").in(taskInstanceIdList));
        }


        List<TaskInstanceEntity> entityList = mongoTemplate.find(query,TaskInstanceEntity.class,collectionName);


        if(null != entityList){
            List<TaskInstance> instanceList = new ArrayList<TaskInstance>(entityList.size());

            for (TaskInstanceEntity entity : entityList) {
                TaskInstance instance = buildInstance(entity);
                instanceList.add(instance);
            }

            return instanceList;
        }

        return Collections.emptyList();

    }


    @Override
    public List<TaskInstance> findTaskListByAssignee(TaskInstanceQueryByAssigneeParam param,
                                                     ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public Long countTaskListByAssignee(TaskInstanceQueryByAssigneeParam param,
                                           ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public int updateFromStatus(TaskInstance taskInstance, String fromStatus,
                                ProcessEngineConfiguration processEngineConfiguration) {

        save(taskInstance, processEngineConfiguration);

        TaskAssigneeStorage  taskAssigneeStorage =  processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.COMMON,TaskAssigneeStorage.class);

        taskAssigneeStorage.removeAll(taskInstance.getInstanceId(),processEngineConfiguration);

        //TUNE
        return 1;

    }

    private void save(TaskInstance taskInstance, ProcessEngineConfiguration processEngineConfiguration) {
        MongoTemplate mongoTemplate = (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        TaskInstanceEntity entity = buildEntity(taskInstance);

        mongoTemplate.save(entity, collectionName);
    }

    @Override
    public List<TaskInstance> findTaskByProcessInstanceIdAndStatus(TaskInstanceQueryParam taskInstanceQueryParam,
                                                                   ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }
}