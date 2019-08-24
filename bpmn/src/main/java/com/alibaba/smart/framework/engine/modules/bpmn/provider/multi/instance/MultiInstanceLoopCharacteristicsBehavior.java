package com.alibaba.smart.framework.engine.modules.bpmn.provider.multi.instance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.LoopCollection;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.ExecutePolicyBehavior;
import com.alibaba.smart.framework.engine.provider.Performer;
import com.alibaba.smart.framework.engine.provider.ProviderFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

/**
 * @author ettear
 * Created by ettear on 14/10/2017.
 */
public class MultiInstanceLoopCharacteristicsBehavior implements ExecutePolicyBehavior {
    private ExtensionPointRegistry extensionPointRegistry;
    private ProcessCommandService processCommandService;
    private ProcessEngineConfiguration processEngineConfiguration ;
    private ExecutionInstanceFactory executionInstanceFactory;
    private TaskInstanceStorage taskInstanceStorage;


    private CompletionCheckerProvider completionCheckerProvider;
    private Performer completionCheckPrepareProvider;

    private LoopCollectionProvider loopCollectionProvider;
    private String collectionItemName;


    private MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics;
    private ExecutionInstanceStorage executionInstanceStorage;
    public MultiInstanceLoopCharacteristicsBehavior(
        ExtensionPointRegistry extensionPointRegistry,
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics) {
        this.extensionPointRegistry = extensionPointRegistry;
        this.processEngineConfiguration =  extensionPointRegistry.getExtensionPoint(
            SmartEngine.class).getProcessEngineConfiguration();

        this.processCommandService = this.extensionPointRegistry.getExtensionPoint(ProcessCommandService.class);

        ProviderFactoryExtensionPoint providerFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(
            ProviderFactoryExtensionPoint.class);

        this.collectionItemName = multiInstanceLoopCharacteristics.getInputDataItemName();

        LoopCollection loopCollection = multiInstanceLoopCharacteristics.getLoopCollection();
        if (null != loopCollection) {
            LoopCollectionProviderFactory loopCollectionProviderFactory
                = (LoopCollectionProviderFactory)providerFactoryExtensionPoint
                .getProviderFactory(loopCollection.getClass());
            this.loopCollectionProvider = loopCollectionProviderFactory.createProvider(loopCollection);
        }

        //FIXME

        //Performable completionPrepareCheckPerformable = multiInstanceLoopCharacteristics.getCompletionCheckPrepare();
        //if (null != completionPrepareCheckPerformable) {
        //
        //    PerformerProviderFactory completionPrepareCheckPerformerProviderFactory
        //        = (PerformerProviderFactory)providerFactoryExtensionPoint
        //        .getProviderFactory(completionPrepareCheckPerformable.getClass());
        //    this.completionCheckPrepareProvider = completionPrepareCheckPerformerProviderFactory.createPerformer(null,
        //        completionPrepareCheckPerformable);
        //}
        //
        //if (null != multiInstanceLoopCharacteristics.getCompletionChecker()) {
        //    this.completionCheckerProvider = new CompletionCheckerProvider(extensionPointRegistry,
        //        multiInstanceLoopCharacteristics.getCompletionChecker());
        //}


        this.multiInstanceLoopCharacteristics = multiInstanceLoopCharacteristics;
        this.executionInstanceFactory = extensionPointRegistry.getExtensionPoint(ExecutionInstanceFactory.class);
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(
            PersisterFactoryExtensionPoint.class);
        this.executionInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(
            ExecutionInstanceStorage.class);
        this.taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);

    }

    @Override
    public void enter(PvmActivity pvmActivity, ExecutionContext context) {
        ActivityInstance activityInstance = context.getActivityInstance();
        boolean needPause = false;
        Collection<Object> collection = null;
        if (null != this.loopCollectionProvider) {

            collection = this.loopCollectionProvider.getCollection(
                context, pvmActivity);
        }

        if (null == collection) {
            collection = new ArrayList<Object>(1);
            //FIXME WHY?

            collection.add(0);
        }

        List<ExecutionInstance> executionInstanceList = new ArrayList<ExecutionInstance>(
            collection.size());
        activityInstance.setExecutionInstanceList(executionInstanceList);

        for (Object item : collection) {

            ExecutionInstance executionInstance = this.executionInstanceFactory.create(activityInstance,
                context);
            executionInstanceList.add(executionInstance);
            context.setExecutionInstance(executionInstance);

            if (null != this.collectionItemName) {
                if (null == context.getRequest()) {
                    context.setRequest(new HashMap<String, Object>());
                }
                context.getRequest().put(collectionItemName, item);
            }
            pvmActivity.invoke(PvmEventConstant.ACTIVITY_START.name(), context);
            if (context.isNeedPause()) {
                needPause = true;
                executionInstance.setStatus(InstanceStatus.suspended);
            }
        }

        if (needPause) {
            context.setNeedPause(true);
        }
    }

    @Override
    public void execute(PvmActivity pvmActivity, ExecutionContext context) {
        pvmActivity.invoke(PvmEventConstant.ACTIVITY_EXECUTE.name(), context);
        if (!context.isNeedPause()) {
            ExecutionInstance executionInstance = context.getExecutionInstance();
            //只负责完成当前executionInstance的状态更新,此时产生了 DB 写.
            MarkDoneUtil.markDoneExecutionInstance(executionInstance, this.executionInstanceStorage,
                this.processEngineConfiguration);

            ActivityInstance activityInstance = context.getActivityInstance();

            List<ExecutionInstance> executionInstances = executionInstanceStorage.findByActivityInstanceId(
                activityInstance.getProcessInstanceId(), activityInstance.getInstanceId(),this.processEngineConfiguration );
            activityInstance.setExecutionInstanceList(executionInstances);

            if (null != this.completionCheckPrepareProvider) {
                this.completionCheckPrepareProvider.perform(context);
            }

            //check
            boolean needAbort = false, needComplete = false;

            //使用检查器进行判断

                Performer abortCheckPerformer = this.completionCheckerProvider.getAbortCheckPerformer();
                needAbort = this.check(abortCheckPerformer, context);

                //不需要中断，判断是否需要完成

            //不需要中断
            if (!needAbort) {
                //检查是否所有的ExecutionInstance都已完成
                boolean executionCompleted = true;
                for (ExecutionInstance instance : executionInstances) {
                    if (instance.isActive()) {
                        executionCompleted = false;
                        break;
                    }
                }

                if (executionCompleted) {
                    //所有的ExecutionInstance都已完成
                    if (null != this.completionCheckerProvider && null != this.completionCheckerProvider
                        .getCompletionCheckPerformer()) {
                        //完成条件存在
                        Performer completionCheckPerformer = this.completionCheckerProvider
                            .getCompletionCheckPerformer();
                        needComplete = this.check(completionCheckPerformer, context);
                        if (!needComplete) {
                            //如果没有达到完成条件，执行中断
                            needAbort = true;
                        }
                    } else {
                        //完成条件不存在
                        needComplete = true;
                    }
                } else {
                    //ExecutionInstance没有结束
                    if (null != this.completionCheckerProvider) {
                        Performer completionCheckPerformer = this.completionCheckerProvider
                            .getCompletionCheckPerformer();
                        needComplete = this.check(completionCheckPerformer, context);
                    }
                }
            }

            if (needAbort) {
                context.getProcessInstance().setStatus(InstanceStatus.aborted);
                processCommandService.abort(executionInstance.getProcessInstanceId(), InstanceStatus.aborted.name());
                context.setNeedPause(true);

            } else if (needComplete) {
                // Complete all execution
                for (ExecutionInstance instance : executionInstances) {
                    if (instance.isActive()) {
                        MarkDoneUtil.markDoneExecutionInstance(instance, this.executionInstanceStorage,
                            this.processEngineConfiguration);
                    }
                }

                // Find all task
                //TODO ADD INDEX
                TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
                List<String> processInstanceIdList = new ArrayList<String>(2);
                processInstanceIdList.add(executionInstance.getProcessInstanceId());
                taskInstanceQueryParam.setProcessInstanceIdList(processInstanceIdList);
                taskInstanceQueryParam.setActivityInstanceId(executionInstance.getActivityInstanceId());
                List<TaskInstance> allTaskInstanceList = this.taskInstanceStorage.findTaskList(taskInstanceQueryParam,this.processEngineConfiguration );

                // Cancel uncompleted task
                for (TaskInstance taskInstance : allTaskInstanceList) {

                    if (taskInstance.getExecutionInstanceId().equals(executionInstance.getInstanceId())) {
                        continue;
                    }

                    if (TaskInstanceConstant.COMPLETED.equals(taskInstance.getStatus())) {
                        continue;
                    }

                    // 这里产生了db 读写访问,
                    taskInstance.setStatus(TaskInstanceConstant.CANCELED);
                    Date currentDate = DateUtil.getCurrentDate();
                    taskInstance.setCompleteTime(currentDate);
                    taskInstanceStorage.update(taskInstance, this.processEngineConfiguration);
                }
            } else {
                context.setNeedPause(true);
            }
        }else{
            ExecutionInstance executionInstance = context.getExecutionInstance();
            executionInstance.setStatus(InstanceStatus.suspended);
        }
    }

    private boolean check(Performer performer, ExecutionContext context) {
        if (null != performer) {
            Boolean result = (Boolean)performer.perform(context);
            if (null != result) {
                return result;
            }
        }
        return false;

    }

}
