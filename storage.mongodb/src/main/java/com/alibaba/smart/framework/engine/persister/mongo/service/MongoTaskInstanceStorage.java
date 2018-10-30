package com.alibaba.smart.framework.engine.persister.mongo.service;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.TableSchemaStrategy;
import com.alibaba.smart.framework.engine.instance.impl.DefaultProcessInstance;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.common.util.SpringContextUtil;
import com.alibaba.smart.framework.engine.persister.mongo.entity.ProcessInstanceEntity;
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

import static com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant.*;


/**
 * Created by 高海军 帝奇 74394 on 2018 October  21:52.
 */
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


        MongoTemplate mongoTemplate =  SpringContextUtil.getBean(MONGO_TEMPLATE, MongoTemplate.class);
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
            Pageable pageableRequest = new PageRequest(taskInstanceQueryParam.getPageOffset(),taskInstanceQueryParam.getPageSize());
            query.with(pageableRequest);
        }

        query.with( new Sort(Sort.Direction.ASC, GMT_CREATE));
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

        MongoTemplate mongoTemplate =  SpringContextUtil.getBean(MONGO_TEMPLATE, MongoTemplate.class);
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
        return null;
    }

    @Override
    public Integer countPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam,
                                        ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public List<TaskInstance> findTaskListByAssignee(TaskInstanceQueryByAssigneeParam param,
                                                     ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public Integer countTaskListByAssignee(TaskInstanceQueryByAssigneeParam param,
                                           ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }



    @Override
    public TaskInstance insert(TaskInstance taskInstance, ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public TaskInstance update(TaskInstance taskInstance, ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public int updateFromStatus(TaskInstance taskInstance, String fromStatus,
                                ProcessEngineConfiguration processEngineConfiguration) {
        return 0;
    }

    @Override
    public TaskInstance find(String taskInstanceId, ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public void remove(String taskInstanceId, ProcessEngineConfiguration processEngineConfiguration) {

    }


    @Override
    public List<TaskInstance> findTaskByProcessInstanceIdAndStatus(TaskInstanceQueryParam taskInstanceQueryParam,
                                                                   ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }
}