package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.param.query.PaginateQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2016 November  22:10.
 */
public class DefaultTaskQueryService implements TaskQueryService, LifeCycleListener {

    private ExtensionPointRegistry extensionPointRegistry;
//    private TaskInstanceStorage taskInstanceStorage;


    public DefaultTaskQueryService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {

//        this.taskInstanceStorage = this.extensionPointRegistry.getExtensionPoint(TaskInstanceStorage.class);

    }

    @Override
    public void stop() {

    }

    @Override
    public List<TaskInstance> findPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);


        //TaskInstanceQueryParam taskInstanceQueryParam = buildPendingTaskQueryParam(pendingTaskQueryParam);
        return taskInstanceStorage.findPendingTaskList(pendingTaskQueryParam);
    }

    @Override
    public Integer countPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);
        //TaskInstanceQueryParam taskInstanceQueryParam = buildPendingTaskQueryParam(pendingTaskQueryParam, null);

        return taskInstanceStorage.countPendingTaskList(pendingTaskQueryParam);
    }

    //private TaskInstanceQueryParam buildPendingTaskQueryParam(PendingTaskQueryParam pendingTaskQueryParam, PaginateQueryParam paginateQueryParam) {
    //    TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
    //    taskInstanceQueryParam.setPendingTaskQueryParam(pendingTaskQueryParam);
    //    taskInstanceQueryParam.setStatus(TaskInstanceConstant.PENDING);
    //
    //
    //    if(null!= paginateQueryParam){
    //        taskInstanceQueryParam.setPageOffset(paginateQueryParam.getPageOffset());
    //        taskInstanceQueryParam.setPageSize(paginateQueryParam.getPageSize());
    //        pendingTaskQueryParam.setPageOffset(paginateQueryParam.getPageOffset());
    //        pendingTaskQueryParam.setPageSize(paginateQueryParam.getPageSize());
    //    }
    //
    //    return taskInstanceQueryParam;
    //}



    @Override
    public List<TaskInstance> findAllPendingTaskList(Long processInstanceId) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);

        TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
        taskInstanceQueryParam.setProcessInstanceId(processInstanceId);
        taskInstanceQueryParam.setStatus(TaskInstanceConstant.PENDING);

        return taskInstanceStorage.findTaskByProcessInstanceIdAndStatus(taskInstanceQueryParam);
    }

    //@Override
    //public List<TaskInstance> findAllPendingTaskList(Long processInstanceId, PendingTaskQueryParam pendingTaskQueryParam) {
    //    PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
    //    TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);
    //
    //
    //    TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
    //    taskInstanceQueryParam.setPendingTaskQueryParam(pendingTaskQueryParam);
    //    taskInstanceQueryParam.setProcessInstanceId(processInstanceId);
    //    taskInstanceQueryParam.setStatus(TaskInstanceConstant.PENDING);
    //
    //    return taskInstanceStorage.findTaskList(taskInstanceQueryParam);
    //}


    @Override
    public TaskInstance findOne(Long taskInstanceId) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);
        TaskInstance taskInstance = taskInstanceStorage.find(taskInstanceId);
        return taskInstance;
    }

    @Override
    public List<TaskInstance> findList(TaskInstanceQueryParam taskInstanceQueryParam){
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);

        return taskInstanceStorage.findTaskList(taskInstanceQueryParam);
    }

    @Override
    public Integer count(TaskInstanceQueryParam taskInstanceQueryParam) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);

        return taskInstanceStorage.count(taskInstanceQueryParam);
    }
}
