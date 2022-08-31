package com.alibaba.smart.framework.engine.persister.mongo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.TableSchemaStrategy;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskAssigneeInstance;
import com.alibaba.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant;
import com.alibaba.smart.framework.engine.persister.mongo.entity.TaskAssigneeEntity;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant.GMT_CREATE;
import static com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant.MONGO_TEMPLATE;
import static com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant.PROCESS_INSTANCE_ID;

/**
 * Created by 高海军 帝奇 74394 on 2018 October  21:52.
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = TaskAssigneeStorage.class)

public class MongoTaskAssigneeInstanceStorage implements TaskAssigneeStorage {

    private static final String INSTANCE = "se_task_assignee_instance";




    @Override
    public List<TaskAssigneeInstance> findPendingTaskAssigneeList(PendingTaskQueryParam pendingTaskQueryParam,
                                                                  ProcessEngineConfiguration processEngineConfiguration) {
        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        Query query = buildQuery(pendingTaskQueryParam);

        List<TaskAssigneeEntity> entityList = mongoTemplate.find(query,TaskAssigneeEntity.class,collectionName);


        if(null != entityList){
            List<TaskAssigneeInstance> instanceList = new ArrayList<TaskAssigneeInstance>(entityList.size());

            for (TaskAssigneeEntity entity : entityList) {
                TaskAssigneeInstance instance = buildInstance(entity);
                instanceList.add(instance);
            }

            return instanceList;
        }

        return Collections.emptyList();
    }

    @Override
    public Long countPendingTaskAssigneeList(PendingTaskQueryParam pendingTaskQueryParam,
                                             ProcessEngineConfiguration processEngineConfiguration) {
        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);
        Query query = buildCommonQuery(pendingTaskQueryParam);

        Long counter = mongoTemplate.count(query,TaskAssigneeEntity.class,collectionName);

        return counter;
    }

    private TaskAssigneeInstance buildInstance(TaskAssigneeEntity entity) {
        TaskAssigneeInstance taskAssigneeInstance= new DefaultTaskAssigneeInstance();
        taskAssigneeInstance.setProcessInstanceId(entity.getProcessInstanceId());
        taskAssigneeInstance.setTaskInstanceId(entity.getTaskInstanceId());
        taskAssigneeInstance.setAssigneeId(entity.getAssigneeId());
        taskAssigneeInstance.setAssigneeType(entity.getAssigneeType());
        return taskAssigneeInstance;

    }

    private Query buildQuery(PendingTaskQueryParam pendingTaskQueryParam) {
        Query query = buildCommonQuery(pendingTaskQueryParam);

        if(null != pendingTaskQueryParam.getPageOffset()){
            Pageable pageableRequest =   PageRequest.of(pendingTaskQueryParam.getPageOffset(),pendingTaskQueryParam.getPageSize());
            query.with(pageableRequest);
        }

        query.with(   Sort.by(Sort.Direction.ASC, GMT_CREATE));
        return query;
    }

    private Query buildCommonQuery(PendingTaskQueryParam pendingTaskQueryParam) {
        Query query = new Query();

        if(null !=  pendingTaskQueryParam.getProcessDefinitionType()){
            query.addCriteria(Criteria.where(MongoConstant.PROCESS_DEFINITION_TYPE).is(pendingTaskQueryParam.getProcessDefinitionType()));
        }

        if(null != pendingTaskQueryParam.getProcessInstanceIdList()){
            query.addCriteria(Criteria.where(PROCESS_INSTANCE_ID).in(pendingTaskQueryParam.getProcessInstanceIdList()));
        }

        if(null != pendingTaskQueryParam.getAssigneeUserId()){
            Criteria userPendingCriteria = Criteria.where("assigneeId").is(
                pendingTaskQueryParam.getAssigneeUserId()).and("assigneeType").is("user");

            Criteria  groupPendingCriteria = null;
            if(CollectionUtil.isNotEmpty(pendingTaskQueryParam.getAssigneeGroupIdList())){
                groupPendingCriteria   = Criteria.where("assigneeId").in(pendingTaskQueryParam.getAssigneeGroupIdList()).and("assigneeType").is("groupPendingCriteria");
            }

            if(groupPendingCriteria == null ){

                // only user
                query.addCriteria(userPendingCriteria);

            }else{

                // user and group, need or condition
                Criteria cr = new Criteria();
                cr.orOperator(userPendingCriteria,groupPendingCriteria);

                query.addCriteria(cr);

            }


        }
        return query;
    }

    @Override
    public TaskAssigneeInstance insert(TaskAssigneeInstance instance,
                                       ProcessEngineConfiguration processEngineConfiguration) {

        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        TaskAssigneeEntity entity = buildEntity(instance);

        mongoTemplate.insert(entity, collectionName);

        instance.setInstanceId(entity.getId());

        return  instance;
    }

    private TaskAssigneeEntity buildEntity(TaskAssigneeInstance instance) {
        TaskAssigneeEntity taskAssigneeEntity= new TaskAssigneeEntity();
        taskAssigneeEntity.setProcessInstanceId(instance.getProcessInstanceId());
        taskAssigneeEntity.setTaskInstanceId(instance.getTaskInstanceId());
        taskAssigneeEntity.setAssigneeId(instance.getAssigneeId());
        taskAssigneeEntity.setAssigneeType(instance.getAssigneeType());

        Date now = DateUtil.getCurrentDate();
        taskAssigneeEntity.setGmtCreate(now);
        taskAssigneeEntity.setGmtModified(now);
        return taskAssigneeEntity;
    }


    @Override
    public void remove(String taskAssigneeId, ProcessEngineConfiguration processEngineConfiguration) {
        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);


        TaskAssigneeEntity entity =  mongoTemplate.findById(taskAssigneeId,TaskAssigneeEntity.class,
            collectionName);

        mongoTemplate.remove(entity,collectionName);
    }

    @Override
    public void removeAll(String taskInstanceId, ProcessEngineConfiguration processEngineConfiguration) {
        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        Query query = buildQuery(taskInstanceId);

        mongoTemplate.remove(query,TaskAssigneeEntity.class,collectionName);
    }

    @Override
    public List<TaskAssigneeInstance> findList(String taskInstanceId,
                                               ProcessEngineConfiguration processEngineConfiguration) {
        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        Query query = buildQuery(taskInstanceId);

        List<TaskAssigneeEntity> entityList = mongoTemplate.find(query,TaskAssigneeEntity.class,collectionName);


        if(null != entityList){
            List<TaskAssigneeInstance> instanceList = new ArrayList<TaskAssigneeInstance>(entityList.size());

            for (TaskAssigneeEntity entity : entityList) {
                TaskAssigneeInstance instance = buildInstance(entity);
                instanceList.add(instance);
            }

            return instanceList;
        }

        return Collections.emptyList();

    }

    private Query buildQuery(String taskInstanceId) {
        Query query = new Query();

        query.addCriteria(Criteria.where("taskInstanceId").is(taskInstanceId));
        return query;
    }

    @Override
    public TaskAssigneeInstance update(String taskAssigneeId, String assigneeId,
                                       ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public TaskAssigneeInstance findOne(String taskAssigneeId, ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }



    @Override
    public Map<String, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(List<String> taskInstanceIdList,
                                                                              ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }
}