package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskInstance;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.common.util.SpringContextUtil;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import org.springframework.beans.BeanUtils;


public class RelationshipDatabaseTaskInstanceStorage implements TaskInstanceStorage {

    @Override
    public List<TaskInstance> findPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam,
                                                  ProcessEngineConfiguration processEngineConfiguration) {
        return findTaskListByAssignee(convertToTaskInstanceQueryByAssigneeParam(pendingTaskQueryParam), processEngineConfiguration);
    }

    private TaskInstanceQueryByAssigneeParam convertToTaskInstanceQueryByAssigneeParam(PendingTaskQueryParam pendingTaskQueryParam) {
        TaskInstanceQueryByAssigneeParam taskInstanceQueryByAssigneeParam = new TaskInstanceQueryByAssigneeParam();
        taskInstanceQueryByAssigneeParam.setAssigneeGroupIdList(pendingTaskQueryParam.getAssigneeGroupIdList());
        taskInstanceQueryByAssigneeParam.setAssigneeUserId(pendingTaskQueryParam.getAssigneeUserId());
        taskInstanceQueryByAssigneeParam.setProcessDefinitionType(pendingTaskQueryParam.getProcessDefinitionType());

        List<String> processInstanceIdList = pendingTaskQueryParam.getProcessInstanceIdList();
        if(null != processInstanceIdList){
            List<Long> processInstanceIdList1  = new ArrayList<Long>(processInstanceIdList.size());
            for (String s : processInstanceIdList) {
                processInstanceIdList1.add(Long.valueOf(s));
            }
            taskInstanceQueryByAssigneeParam.setProcessInstanceIdList(processInstanceIdList1);
        }

        taskInstanceQueryByAssigneeParam.setPageOffset(pendingTaskQueryParam.getPageOffset());
        taskInstanceQueryByAssigneeParam.setPageSize(pendingTaskQueryParam.getPageSize());

        taskInstanceQueryByAssigneeParam.setStatus(TaskInstanceConstant.PENDING);
        //自定义查询条件
        taskInstanceQueryByAssigneeParam.setCustomFieldsQueryParam(pendingTaskQueryParam.getCustomFieldsQueryParam());
        return taskInstanceQueryByAssigneeParam;
    }

    @Override
    public Long countPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam,
                                     ProcessEngineConfiguration processEngineConfiguration) {
        return countTaskListByAssignee(convertToTaskInstanceQueryByAssigneeParam(pendingTaskQueryParam),processEngineConfiguration );
    }

    @Override
    public List<TaskInstance> findTaskListByAssignee(TaskInstanceQueryByAssigneeParam param,
                                                     ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        List<TaskInstanceEntity>  taskInstanceEntityList= taskInstanceDAO.findTaskByAssignee(param);

        //获取扩展字段
        //获取扩展字段
        Map<String,Map<String,Object>> taskInstanceFieldsMaps=new HashMap<String, Map<String, Object>>();
        if(param.getCustomFieldsQueryParam()!=null&&param.getCustomFieldsQueryParam().getAllCustomFieldsList().size()>0){
            List<Map<String,Object>> taskInstanceCustomFieldsMapList=taskInstanceDAO.findTaskCustomFieldsByAssignee(param);
            convert(taskInstanceFieldsMaps, taskInstanceCustomFieldsMapList);
        }

        List<TaskInstance> taskInstanceList = new ArrayList<TaskInstance>(taskInstanceEntityList.size());
        for (TaskInstanceEntity taskInstanceEntity : taskInstanceEntityList) {
            TaskInstance taskInstance= buildTaskInstanceFromEntity(taskInstanceEntity);
            Map<String, Object> customFieldsMap = taskInstanceFieldsMaps.get(taskInstance.getInstanceId());
            if(customFieldsMap !=null){
                taskInstance.setCustomFiledValues(customFieldsMap);
            }
            taskInstanceList.add(taskInstance);

        }
        return taskInstanceList;
    }

    @Override
    public Long countTaskListByAssignee(TaskInstanceQueryByAssigneeParam param,
                                           ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        Integer count = taskInstanceDAO.countTaskByAssignee(param);
        return  count  == null? 0L:count;
    }

    @Override
    public List<TaskInstance> findTaskByProcessInstanceIdAndStatus(TaskInstanceQueryParam taskInstanceQueryParam,
                                                                   ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        String processInstanceId = taskInstanceQueryParam.getProcessInstanceIdList().get(0);
        List<TaskInstanceEntity>  taskInstanceEntityList= taskInstanceDAO.findTaskByProcessInstanceIdAndStatus(
            Long.valueOf(processInstanceId),taskInstanceQueryParam.getStatus());

        List<TaskInstance> taskInstanceList = new ArrayList<TaskInstance>(taskInstanceEntityList.size());
        for (TaskInstanceEntity taskInstanceEntity : taskInstanceEntityList) {

            TaskInstance taskInstance= buildTaskInstanceFromEntity(taskInstanceEntity);

            taskInstanceList.add(taskInstance);

        }

        return taskInstanceList;
    }



    @Override
    public List<TaskInstance> findTaskList(TaskInstanceQueryParam taskInstanceQueryParam,
                                           ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        List<TaskInstanceEntity>  taskInstanceEntityList= taskInstanceDAO.findTaskList(taskInstanceQueryParam);

        //获取扩展字段
        Map<String,Map<String,Object>> taskInstanceFieldsMaps=new HashMap<String, Map<String, Object>>();
        if(taskInstanceQueryParam.getCustomFieldsQueryParam()!=null&&taskInstanceQueryParam.getCustomFieldsQueryParam().getAllCustomFieldsList().size()>0){
            List<Map<String,Object>> taskInstanceCustomFieldsMapList=taskInstanceDAO.findTaskCustomFields(taskInstanceQueryParam);
            convert(taskInstanceFieldsMaps, taskInstanceCustomFieldsMapList);
        }
        List<TaskInstance> taskInstanceList = new ArrayList<TaskInstance>(taskInstanceEntityList.size());
        for (TaskInstanceEntity taskInstanceEntity : taskInstanceEntityList) {
          TaskInstance taskInstance= buildTaskInstanceFromEntity(taskInstanceEntity);
            Map<String, Object> customFieldsMap = taskInstanceFieldsMaps.get(taskInstance.getInstanceId());
            if(customFieldsMap ==null){
                continue;
            }
            taskInstance.setCustomFiledValues(customFieldsMap);
          taskInstanceList.add(taskInstance);

        }

        return taskInstanceList;
    }

    private void convert(Map<String, Map<String, Object>> taskInstanceFieldsMaps, List<Map<String, Object>> taskInstanceCustomFieldsMapList) {
        if(taskInstanceCustomFieldsMapList!=null&&taskInstanceCustomFieldsMapList.size()>0){
            for(Map<String,Object> map:taskInstanceCustomFieldsMapList){
                if(map.isEmpty()||map.get("id")==null){
                    continue;
                }
                //任务Id
                String taskId=map.get("id").toString();
                //临时map，把id删除
                Map<String,Object> tempMap=new HashMap<String,Object>(map);
                tempMap.remove("id");
                //判断除id之外的字段是否都为空，如果都为空，则过滤掉
                if(tempMap.isEmpty()){
                    continue;
                }
                taskInstanceFieldsMaps.put(taskId,tempMap);
            }
        }
    }

    @Override
    public Long count(TaskInstanceQueryParam taskInstanceQueryParam,
                      ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        Integer count = taskInstanceDAO.count(taskInstanceQueryParam);
        return  count  == null? 0L:count;
    }

    //@Override
    //public List<TaskInstance> findList(Long processInstanceId, Long activityInstanceId) {
    //    TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
    //    List<TaskInstanceEntity>  taskInstanceEntityList= taskInstanceDAO.findList(processInstanceId ,activityInstanceId);
    //
    //    List<TaskInstance> taskInstanceList = new ArrayList<TaskInstance>(taskInstanceEntityList.size());
    //    for (TaskInstanceEntity taskInstanceEntity : taskInstanceEntityList) {
    //            TaskInstance taskInstance= buildTaskInstance(taskInstanceEntity);
    //            taskInstanceList.add(taskInstance);
    //
    //    }
    //
    //    return taskInstanceList;
    //}

    @Override
    public TaskInstance insert(TaskInstance taskInstance,
                               ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");

        TaskInstanceEntity taskInstanceEntity = buildTaskInstanceEntity(taskInstance);
        taskInstanceDAO.insert(taskInstanceEntity);

        Long entityId = taskInstanceEntity.getId();

        // 当数据库表id 是非自增时，需要以传入的 id 值为准
        if(0L == entityId){
            entityId = Long.valueOf( taskInstance.getInstanceId());
        }

        taskInstanceEntity = taskInstanceDAO.findOne(entityId);

        //reAssign
        TaskInstance   resultTaskInstance= buildTaskInstanceFromEntity(taskInstanceEntity);
        resultTaskInstance.setTaskAssigneeInstanceList(taskInstance.getTaskAssigneeInstanceList());
        resultTaskInstance.setTaskItemInstanceList(taskInstance.getTaskItemInstanceList());
        return resultTaskInstance;
    }

    @Override
    public TaskInstance insert(TaskInstance taskInstance, Map<String,Object> customFieldsMap, ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");

        TaskInstanceEntity taskInstanceEntity = buildTaskInstanceEntity(taskInstance);

        //如果没有扩展字段，走原来的逻辑
        if(null== customFieldsMap || customFieldsMap.size()==0){
            taskInstanceDAO.insert(taskInstanceEntity);
        }else{
            taskInstanceDAO.insertWithCustomFields(taskInstanceEntity,customFieldsMap);
        }


        Long entityId = taskInstanceEntity.getId();

        // 当数据库表id 是非自增时，需要以传入的 id 值为准
        if(0L == entityId){
            entityId = Long.valueOf( taskInstance.getInstanceId());
        }

        taskInstanceEntity = taskInstanceDAO.findOne(entityId);

        //reAssign
        TaskInstance   resultTaskInstance= buildTaskInstanceFromEntity(taskInstanceEntity);
        resultTaskInstance.setTaskAssigneeInstanceList(taskInstance.getTaskAssigneeInstanceList());
        resultTaskInstance.setTaskItemInstanceList(taskInstance.getTaskItemInstanceList());
        return resultTaskInstance;
    }


    @Override
    public TaskInstance update(TaskInstance taskInstance,
                               ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        TaskInstanceEntity taskInstanceEntity = buildTaskInstanceEntity(taskInstance);
        taskInstanceDAO.update(taskInstanceEntity);


        return taskInstance;
    }

    @Override
    public int updateFromStatus(TaskInstance taskInstance, String fromStatus,
                                ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        TaskInstanceEntity taskInstanceEntity = buildTaskInstanceEntity(taskInstance);
        return taskInstanceDAO.updateFromStatus(taskInstanceEntity,fromStatus);
    }

    @Override
    public TaskInstance find(String instanceId,
                             ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        TaskInstanceEntity taskInstanceEntity =  taskInstanceDAO.findOne(Long.valueOf(instanceId));

        TaskInstance taskInstance= buildTaskInstanceFromEntity(taskInstanceEntity);
        return  taskInstance;
    }

    private TaskInstance buildTaskInstanceFromEntity(TaskInstanceEntity taskInstanceEntity) {
        TaskInstance taskInstance = new DefaultTaskInstance();
        taskInstance.setInstanceId(taskInstanceEntity.getId().toString());
        taskInstance.setStartTime(taskInstanceEntity.getGmtCreate());
        taskInstance.setProcessDefinitionIdAndVersion(taskInstanceEntity.getProcessDefinitionIdAndVersion());
        taskInstance.setProcessInstanceId(taskInstanceEntity.getProcessInstanceId().toString());
        taskInstance.setActivityInstanceId(taskInstanceEntity.getActivityInstanceId().toString());
        taskInstance.setProcessDefinitionType(taskInstanceEntity.getProcessDefinitionType());
        taskInstance.setTag(taskInstanceEntity.getTag());
        taskInstance.setStatus(taskInstanceEntity.getStatus());

        taskInstance.setProcessDefinitionActivityId(taskInstanceEntity.getProcessDefinitionActivityId());
        taskInstance.setExecutionInstanceId(taskInstanceEntity.getExecutionInstanceId().toString());

        taskInstance.setClaimUserId(taskInstanceEntity.getClaimUserId());
        taskInstance.setCompleteTime(taskInstanceEntity.getCompleteTime());
        taskInstance.setClaimTime(taskInstanceEntity.getClaimTime());
        taskInstance.setComment(taskInstanceEntity.getComment());
        taskInstance.setExtension(taskInstanceEntity.getExtension());
        taskInstance.setTitle(taskInstanceEntity.getTitle());
        return taskInstance;
    }

    private TaskInstanceEntity buildTaskInstanceEntity(TaskInstance taskInstance) {
        TaskInstanceEntity taskInstanceEntity = new TaskInstanceEntity();

        taskInstanceEntity.setId(Long.valueOf(taskInstance.getInstanceId()));
        taskInstanceEntity.setProcessDefinitionIdAndVersion(taskInstance.getProcessDefinitionIdAndVersion());
        taskInstanceEntity.setProcessInstanceId(Long.valueOf(taskInstance.getProcessInstanceId()));
        taskInstanceEntity.setActivityInstanceId(Long.valueOf(taskInstance.getActivityInstanceId()));
        taskInstanceEntity.setProcessDefinitionActivityId(taskInstance.getProcessDefinitionActivityId());
        taskInstanceEntity.setExecutionInstanceId(Long.valueOf(taskInstance.getExecutionInstanceId()));
        taskInstanceEntity.setClaimUserId(taskInstance.getClaimUserId());
        taskInstanceEntity.setClaimTime(taskInstance.getClaimTime());
        taskInstanceEntity.setStatus(taskInstance.getStatus());
        taskInstanceEntity.setCompleteTime(taskInstance.getCompleteTime());
        taskInstanceEntity.setPriority(taskInstance.getPriority());
        taskInstanceEntity.setTag(taskInstance.getTag());
        taskInstanceEntity.setProcessDefinitionType(taskInstance.getProcessDefinitionType());
        taskInstanceEntity.setClaimTime(taskInstance.getClaimTime());
        taskInstanceEntity.setComment(taskInstance.getComment());
        taskInstanceEntity.setTitle(taskInstance.getTitle());
        taskInstanceEntity.setExtension(taskInstance.getExtension());
        //taskInstanceEntity.setGmtModified(taskInstance.getCompleteTime());
        return taskInstanceEntity;
    }


    @Override
    public void remove(String instanceId,
                       ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        taskInstanceDAO.delete(Long.valueOf(instanceId));

    }
}
