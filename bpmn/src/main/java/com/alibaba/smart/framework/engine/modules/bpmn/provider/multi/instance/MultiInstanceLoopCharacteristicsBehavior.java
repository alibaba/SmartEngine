package com.alibaba.smart.framework.engine.modules.bpmn.provider.multi.instance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.expression.evaluator.MvelExpressionEvaluator;
import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.MultiInstanceCounter;
import com.alibaba.smart.framework.engine.configuration.TaskAssigneeDispatcher;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.CompletionCondition;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.ExecutePolicyBehavior;
import com.alibaba.smart.framework.engine.provider.Invoker;
import com.alibaba.smart.framework.engine.provider.Performer;
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
    private ExecutionInstanceFactory executionInstanceFactory;
    private TaskInstanceStorage taskInstanceStorage;
    private List<Invoker> extensionInvokers;
    private Performer collectionPerformer;
    private Performer completionConditionPerformer;


    private MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics;
    private ExecutionInstanceStorage executionInstanceStorage;
    public MultiInstanceLoopCharacteristicsBehavior(
        ExtensionPointRegistry extensionPointRegistry,
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics) {
        this.extensionPointRegistry = extensionPointRegistry;
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
        Collection<Object> collection = this.getCollection(
            context, pvmActivity.getModel());
        if (null != collection) {
            List<ExecutionInstance> executionInstanceList = new ArrayList<ExecutionInstance>(
                collection.size());
            activityInstance.setExecutionInstanceList(executionInstanceList);

            for (Object item : collection) {

                ExecutionInstance executionInstance = this.executionInstanceFactory.create(activityInstance, context);
                executionInstanceList.add(executionInstance);
                context.setExecutionInstance(executionInstance);

                if(null==context.getRequest()){
                    context.setRequest(new HashMap<String, Object>());
                }
                context.getRequest().put("assignee", item);
                pvmActivity.invoke(PvmEventConstant.ACTIVITY_START.name(), context);
                if (context.isNeedPause()) {
                    needPause = true;
                }
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
            MarkDoneUtil.markDone(executionInstance, this.executionInstanceStorage);

            ActivityInstance activityInstance = context.getActivityInstance();

            List<ExecutionInstance> executionInstances = executionInstanceStorage.findByActivityInstanceId(
                activityInstance.getProcessInstanceId(), activityInstance.getInstanceId());
            activityInstance.setExecutionInstanceList(executionInstances);

            boolean needPause = this.needSuspend(context);
            context.setNeedPause(needPause);
            if (!needPause) {
                // Complete all execution
                for (ExecutionInstance instance : executionInstances) {
                    if (instance.isActive()) {
                        MarkDoneUtil.markDone(instance, this.executionInstanceStorage);
                    }
                }

                // Find all task
                //TODO ADD INDEX
                TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
                taskInstanceQueryParam.setProcessInstanceId(executionInstance.getProcessInstanceId());
                taskInstanceQueryParam.setActivityInstanceId(executionInstance.getActivityInstanceId());
                List<TaskInstance> allTaskInstanceList = this.taskInstanceStorage.findTaskList(taskInstanceQueryParam);

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
                    taskInstanceStorage.update(taskInstance);

                    //ExecutionInstance executionInstance1=   executionInstanceStorage.find(taskInstance
                    // .getExecutionInstanceId());

                    //MarkDoneUtil.markDone(executionInstance1,executionInstanceStorage);
                }
            }
        }
    }

    //TODO ettear move to compatible.activiti

    private Collection<Object> getCollection(ExecutionContext context,
                                             Activity activity) {
        TaskAssigneeDispatcher taskAssigneeDispatcher = context.getProcessEngineConfiguration()
            .getTaskAssigneeDispatcher();

        if (null == taskAssigneeDispatcher) {
            throw new EngineException(
                "The taskAssigneeService can't be null for MultiInstanceLoopCharacteristics feature");
        }

        List<TaskAssigneeCandidateInstance> taskAssigneeCandidateInstanceList = taskAssigneeDispatcher
            .getTaskAssigneeCandidateInstance(activity, context.getRequest());

        List<Object> collection = new ArrayList<Object>(taskAssigneeCandidateInstanceList.size());
        for (TaskAssigneeCandidateInstance taskAssigneeCandidateInstance : taskAssigneeCandidateInstanceList) {
            List<TaskAssigneeCandidateInstance> taskAssigneeCandidateInstanceExecuteList = new ArrayList
                <TaskAssigneeCandidateInstance>(1);
            taskAssigneeCandidateInstanceExecuteList.add(taskAssigneeCandidateInstance);
            collection.add(taskAssigneeCandidateInstanceExecuteList);
        }
        return collection;
    }

    //TODO ettear move to compatible.activiti
    private boolean needSuspend(ExecutionContext context) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(
            PersisterFactoryExtensionPoint.class);
        MultiInstanceCounter multiInstanceCounter = context.getProcessEngineConfiguration().getMultiInstanceCounter();

        SmartEngine smartEngine = this.extensionPointRegistry.getExtensionPoint(SmartEngine.class);
        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();

        //ExecutionInstanceStorage executionInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint
        // (ExecutionInstanceStorage.class);
        //TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(
        //    TaskInstanceStorage.class);

        ExecutionInstance executionInstance = context.getExecutionInstance();

        //TODO ADD INDEX
        TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
        taskInstanceQueryParam.setProcessInstanceId(executionInstance.getProcessInstanceId());
        taskInstanceQueryParam.setActivityInstanceId(executionInstance.getActivityInstanceId());
        List<TaskInstance> allTaskInstanceList = this.taskInstanceStorage.findTaskList(taskInstanceQueryParam);

        CompletionCondition completionCondition = this.multiInstanceLoopCharacteristics.getCompletionCondition();

        Integer passedTaskInstanceNumber = multiInstanceCounter.countPassedTaskInstanceNumber(
            executionInstance.getProcessInstanceId(), executionInstance.getActivityInstanceId(),

            smartEngine);

        Integer rejectedTaskInstanceNumber = multiInstanceCounter.countRejectedTaskInstanceNumber(
            executionInstance.getProcessInstanceId(), executionInstance.getActivityInstanceId(),

            smartEngine);

        //boolean isCounterSignSucceeded = false;
        if (null != completionCondition) {
            // 不是 all 模式
            String passedConditionExpressionContent = completionCondition.getExpressionContent();

            //Number passedThresholdValue=  MvelExpressionEvaluator.getRightValueForBinaryOperationExpression
            // (passedConditionExpressionContent);

            //Double rejectedThresholdValue= 1- passedThresholdValue.doubleValue();

            //String rejectConditionExpression = " nrOfRejectedInstance /nrOfCompletedInstances > "
            // +rejectedThresholdValue;

            Map<String, Object> vars = new HashMap<String, Object>(4);
            // 此变量nrOfCompletedInstances命名并不合适,但是为了兼容 Activiti 不得不这样做.
            vars.put("nrOfCompletedInstances", passedTaskInstanceNumber);
            vars.put("nrOfRejectedInstance", rejectedTaskInstanceNumber);
            vars.put("nrOfInstances", allTaskInstanceList.size());

            //boolean isCounterSignFailed  = MvelExpressionEvaluator.staticEval(rejectConditionExpression,vars);

            //if(isCounterSignFailed){
            //    processCommandService.abort(executionInstance.getProcessInstanceId(), InstanceStatus.aborted.name());
            //会签失败,需要暂停
            //    return  true;

            //}

            return !MvelExpressionEvaluator.staticEval(passedConditionExpressionContent, vars);

            /*
            if (isCounterSignSucceeded) {

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
                    taskInstanceStorage.update(taskInstance);

                    //ExecutionInstance executionInstance1=   executionInstanceStorage.find(taskInstance
                    // .getExecutionInstanceId());

                    //MarkDoneUtil.markDone(executionInstance1,executionInstanceStorage);
                }

                // 会签完成,不需要暂停.
                return false;
            } else {
                //会签未完成,需要暂停
                return true;
            }
            */
        } else {
            //  all 模式 ; 只要一个失败,那么理解 reject. fail fast
            // 只要一个失败,那么理解 reject. fail fast
            if (rejectedTaskInstanceNumber >= 1) {
                // 整个会签任务失败,订单终止.abort 所有关联的 ei 和 ti.
                // TUNE 改成内存模式,这里会产生 db 写.是不合适的. 因为最好还是会触发 统一的 db 写逻辑. 存在多次的更新操作,产生性能额外开销.
                 context.getProcessInstance().setStatus(InstanceStatus.aborted);
                 processCommandService.abort(executionInstance.getProcessInstanceId(), InstanceStatus.aborted.name());
                //会签失败,需要暂停
                return true;
            }

            //TODO 需要结合锁机制.
            if (passedTaskInstanceNumber.equals(allTaskInstanceList.size())) {
                // 会签完成,不需要暂停.
                return false;
            } else {
                //会签未完成,需要暂停
                return true;
            }
        }
    }

}
