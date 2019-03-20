package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.TaskItemInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskItemInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.param.query.TaskItemInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.query.TaskItemQueryService;


public class DefaultTaskItemQueryService implements TaskItemQueryService {

    private ExtensionPointRegistry extensionPointRegistry;
    private ProcessEngineConfiguration processEngineConfiguration ;

    public DefaultTaskItemQueryService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
        this.processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();
    }
    @Override
    public Long count(TaskItemInstanceQueryParam taskItemInstanceQueryParam) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskItemInstanceStorage taskItemInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskItemInstanceStorage.class);
        return taskItemInstanceStorage.count(taskItemInstanceQueryParam, processEngineConfiguration);
    }

    @Override
    public List<TaskItemInstance> findTaskItemList(TaskItemInstanceQueryParam taskItemInstanceQueryParam) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskItemInstanceStorage taskItemInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskItemInstanceStorage.class);
        return taskItemInstanceStorage.findTaskItemList(taskItemInstanceQueryParam, processEngineConfiguration);
    }

    @Override
    public boolean taskItemIntersectionResult(String activityInstanceId, String passTag) {
        List<String> subBizIdOftIntersectionResult = getSubBizIdOfIntersectionResult(activityInstanceId, passTag);
        return subBizIdOftIntersectionResult != null && subBizIdOftIntersectionResult.size() > 0;
    }

    @Override
    public List<String> getSubBizIdOfIntersectionResult(String activityInstanceId, String passTag) {
        List<String> subBizIdList = new ArrayList<String>();
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskItemInstanceStorage taskItemInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskItemInstanceStorage.class);
        TaskItemInstanceQueryParam taskItemInstanceQueryParam = new TaskItemInstanceQueryParam();
        taskItemInstanceQueryParam.setActivityInstanceId(activityInstanceId);
        List<TaskItemInstance> taskItemList = taskItemInstanceStorage.findTaskItemList(taskItemInstanceQueryParam, processEngineConfiguration);
        Map<String, List<TaskItemInstance>> subBizIdMap = new HashMap<String, List<TaskItemInstance>>(2);
        for(TaskItemInstance taskItemInstance : taskItemList){
            String subBizId = taskItemInstance.getSubBizId();
            if(subBizIdMap.keySet().contains(subBizId)){
                List<TaskItemInstance> taskItemInstanceList = subBizIdMap.get(subBizId);
                taskItemInstanceList.add(taskItemInstance);
            }else{
                List<TaskItemInstance> taskItemInstanceList = new ArrayList<TaskItemInstance>();
                taskItemInstanceList.add(taskItemInstance);
                subBizIdMap.put(subBizId,taskItemInstanceList);
            }
        }
        for(Map.Entry<String, List<TaskItemInstance>> entry : subBizIdMap.entrySet()){
            List<TaskItemInstance> taskItemInstanceList = entry.getValue();
            int count = 0;
            for(TaskItemInstance taskItemInstance : taskItemInstanceList){
                if(taskItemInstance.getTag().equals(passTag)){
                    count ++;
                }
            }
            if(taskItemInstanceList.size() == count){
                subBizIdList.add(entry.getKey());
            }
        }
        return subBizIdList;
    }

    @Override
    public List<String> getSubBizIdOfIntersectionResult(Long processInstanceId, String passTag) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskItemInstanceStorage taskItemInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskItemInstanceStorage.class);
        TaskItemInstanceQueryParam taskItemInstanceQueryParam = new TaskItemInstanceQueryParam();
        taskItemInstanceQueryParam.setProcessInstanceIdList(Collections.singletonList(String.valueOf(processInstanceId)));
        List<TaskItemInstance> taskItemList = taskItemInstanceStorage.findTaskItemList(taskItemInstanceQueryParam, processEngineConfiguration);
        Long maxActivityId = 0L;
        if(taskItemList != null && taskItemList.size() >0){
            for(TaskItemInstance taskItemInstance : taskItemList){
                Long activityId = Long.valueOf(taskItemInstance.getActivityInstanceId());
                if(activityId > maxActivityId){
                    maxActivityId = activityId;
                }
            }
        }
        return getSubBizIdOfIntersectionResult(String.valueOf(maxActivityId), passTag);
    }
}
