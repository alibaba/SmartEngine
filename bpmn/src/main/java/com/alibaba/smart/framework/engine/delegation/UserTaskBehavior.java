package com.alibaba.smart.framework.engine.delegation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.expression.ExpressionEvaluator;
import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.configuration.MultiInstanceCounter;
import com.alibaba.smart.framework.engine.configuration.TaskAssigneeDispatcher;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskAssigneeInstance;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.assembly.ConditionExpression;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.UserTask;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

@ExtensionBinding(type = ExtensionConstant.ACTIVITY_BEHAVIOR, bindingTo = UserTask.class)

public class UserTaskBehavior extends AbstractActivityBehavior<UserTask> {

    public UserTaskBehavior() {
        super();
    }

    private List<TaskAssigneeCandidateInstance> getTaskAssigneeCandidateInstances(ExecutionContext context,
                                                                                  UserTask userTask) {
        TaskAssigneeDispatcher taskAssigneeDispatcher = context.getProcessEngineConfiguration()
            .getTaskAssigneeDispatcher();

        if (null == taskAssigneeDispatcher) {
            throw new EngineException("The taskAssigneeService can't be null for UserTask feature");
        }

        return taskAssigneeDispatcher.getTaskAssigneeCandidateInstance(userTask, context.getRequest());
    }

    @Override
    public boolean enter(ExecutionContext context) {
        UserTask userTask = this.getModel();

        List<TaskAssigneeCandidateInstance> taskAssigneeCandidateInstanceList = getTaskAssigneeCandidateInstances(
            context, userTask);

        if (null != userTask.getMultiInstanceLoopCharacteristics()) {

            ActivityInstance activityInstance = super.createSingleActivityInstance(context);

            List<ExecutionInstance> executionInstanceList = new ArrayList<ExecutionInstance>(
                taskAssigneeCandidateInstanceList.size());
            activityInstance.setExecutionInstanceList(executionInstanceList);

            for (TaskAssigneeCandidateInstance taskAssigneeCandidateInstance : taskAssigneeCandidateInstanceList) {

                ExecutionInstance executionInstance = this.executionInstanceFactory.create(activityInstance,
                    context);
                executionInstanceList.add(executionInstance);
                context.setExecutionInstance(executionInstance);

                TaskInstance taskInstance = super.taskInstanceFactory.create(this.getModel(), executionInstance,
                    context);

                List<TaskAssigneeInstance> taskAssigneeInstanceList = new ArrayList<TaskAssigneeInstance>(2);

                IdGenerator idGenerator = context.getProcessEngineConfiguration().getIdGenerator();

                buildTaskAssigneeInstance(taskAssigneeCandidateInstance, taskAssigneeInstanceList, idGenerator);

                taskInstance.setTaskAssigneeInstanceList(taskAssigneeInstanceList);
                executionInstance.setTaskInstance(taskInstance);

            }

        } else {

            super.enter(context);

            if (null != taskAssigneeCandidateInstanceList) {
                ExecutionInstance executionInstance = context.getExecutionInstance();

                TaskInstance taskInstance = super.taskInstanceFactory.create(this.getModel(), executionInstance,
                    context);

                List<TaskAssigneeInstance> taskAssigneeInstanceList = new ArrayList<TaskAssigneeInstance>(2);

                IdGenerator idGenerator = context.getProcessEngineConfiguration().getIdGenerator();

                for (TaskAssigneeCandidateInstance taskAssigneeCandidateInstance : taskAssigneeCandidateInstanceList) {
                    buildTaskAssigneeInstance(taskAssigneeCandidateInstance, taskAssigneeInstanceList, idGenerator);
                }
                taskInstance.setTaskAssigneeInstanceList(taskAssigneeInstanceList);
                executionInstance.setTaskInstance(taskInstance);
            }
        }

        return true;
    }

    private void buildTaskAssigneeInstance(TaskAssigneeCandidateInstance taskAssigneeCandidateInstance,
                                           List<TaskAssigneeInstance> taskAssigneeInstanceList,
                                           IdGenerator idGenerator) {
        TaskAssigneeInstance taskAssigneeInstance = new DefaultTaskAssigneeInstance();
        taskAssigneeInstance.setAssigneeId(taskAssigneeCandidateInstance.getAssigneeId());
        taskAssigneeInstance.setAssigneeType(taskAssigneeCandidateInstance.getAssigneeType());
        taskAssigneeInstance.setInstanceId(idGenerator.getId());
        taskAssigneeInstanceList.add(taskAssigneeInstance);
    }

    @Override
    public void execute(ExecutionContext context) {
        UserTask userTask = this.getModel();

        super.makeExtensionWorkAndExecuteBehavior(context);

        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = userTask
            .getMultiInstanceLoopCharacteristics();
        if (null != multiInstanceLoopCharacteristics) {

            ActivityInstance activityInstance = context.getActivityInstance();

            //1. 当前的所有的 totalExecutionInstanceList，包含所有状态的。
            List<ExecutionInstance> totalExecutionInstanceList = executionInstanceStorage.findByActivityInstanceId(
                activityInstance.getProcessInstanceId(), activityInstance.getInstanceId(),
                this.processEngineConfiguration);

            //1.1 重新写回内存。
            activityInstance.setExecutionInstanceList(totalExecutionInstanceList);

            //2. 完成当前ExecutionInstance的状态更新
            ExecutionInstance executionInstance = context.getExecutionInstance();
            //只负责完成当前executionInstance的状态更新,此时产生了 DB 写.
            MarkDoneUtil.markDoneExecutionInstance(executionInstance, this.executionInstanceStorage,
                this.processEngineConfiguration);

            SmartEngine smartEngine = this.extensionPointRegistry.getExtensionPoint(SmartEngine.class);

            MultiInstanceCounter multiInstanceCounter = context.getProcessEngineConfiguration()
                .getMultiInstanceCounter();

            Integer passedTaskInstanceNumber = multiInstanceCounter.countPassedTaskInstanceNumber(
                activityInstance.getProcessInstanceId(), activityInstance.getInstanceId(),
                smartEngine);

            Integer rejectedTaskInstanceNumber = multiInstanceCounter.countRejectedTaskInstanceNumber(
                activityInstance.getProcessInstanceId(), activityInstance.getInstanceId(),
                smartEngine);

            Integer totalInstanceCount = totalExecutionInstanceList.size();


            Map<String,Object> requestContext=context.getRequest();
            if(null == requestContext){
                requestContext = new HashMap<String, Object>();
            }

            // 不变式  nrOfCompletedInstances+ nrOfRejectedInstance <= nrOfInstances
            requestContext.put("nrOfCompletedInstances", passedTaskInstanceNumber);
            requestContext.put("nrOfRejectedInstance", rejectedTaskInstanceNumber);
            requestContext.put("nrOfInstances", totalInstanceCount);

            // 注意：任务处理的并发性，需要业务程序来控制。
            boolean abortMatched = false;

            ConditionExpression abortCondition = multiInstanceLoopCharacteristics.getAbortCondition();

            if (null != abortCondition) {
                abortMatched = ExpressionEvaluator.eval(context, abortCondition);
            }

            //此时，尚未触发订单abort逻辑
            if (!abortMatched) {
                ConditionExpression completionCondition = multiInstanceLoopCharacteristics.getCompletionCondition();

                if(null != completionCondition){
                    boolean passedMatched  = ExpressionEvaluator.eval(context, completionCondition) ;

                    long finishedTaskCount = passedTaskInstanceNumber + rejectedTaskInstanceNumber;
                    if(finishedTaskCount < totalInstanceCount){

                        if(passedMatched){
                            // Complete all execution
                            for (ExecutionInstance instance : totalExecutionInstanceList) {
                                if (instance.isActive()) {
                                    MarkDoneUtil.markDoneExecutionInstance(instance, this.executionInstanceStorage,
                                        this.processEngineConfiguration);
                                }
                            }

                            // Find all task
                            TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
                            List<String> processInstanceIdList = new ArrayList<String>(2);
                            processInstanceIdList.add(executionInstance.getProcessInstanceId());
                            taskInstanceQueryParam.setProcessInstanceIdList(processInstanceIdList);
                            taskInstanceQueryParam.setActivityInstanceId(executionInstance.getActivityInstanceId());

                            PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry
                                .getExtensionPoint(
                                    PersisterFactoryExtensionPoint.class);

                            TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(
                                TaskInstanceStorage.class);
                            List<TaskInstance> allTaskInstanceList = taskInstanceStorage.findTaskList(taskInstanceQueryParam,
                                this.processEngineConfiguration);

                            // Cancel uncompleted task
                            for (TaskInstance taskInstance : allTaskInstanceList) {

                                //当前的taskInstance 已经在complete方法中更新过了
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

                            context.setNeedPause(false);

                        }else{
                            context.setNeedPause(true);

                        }


                    }else if(finishedTaskCount == totalInstanceCount){


                        if(passedMatched){
                            context.setNeedPause(false);
                        }else {
                            abort(context, executionInstance, smartEngine);
                        }



                    }else{
                        String message =
                            "Error args: passedTaskInstanceNumber, rejectedTaskInstanceNumber, totalInstanceCount each is :"
                                + passedMatched+"," + rejectedTaskInstanceNumber+"," + totalInstanceCount+".";
                        throw new EngineException(
                            message);
                    }

                }
                else{

                    //completionCondition 为空时，则表示是all 模式， 则需要所有任务都完成后，才做判断。
                    if(passedTaskInstanceNumber < totalInstanceCount  ){
                        context.setNeedPause(true);
                    }
                    else  if(passedTaskInstanceNumber.equals(totalInstanceCount)  ){
                        context.setNeedPause(false);
                    }

                    if(rejectedTaskInstanceNumber>=1){
                        abort(context, executionInstance, smartEngine);

                    }


                }






            } else {
                abort(context, executionInstance, smartEngine);

            }

        } else {
            super.commonUpdateExecutionInstance(context);
        }

    }

    protected void abort(ExecutionContext context, ExecutionInstance executionInstance, SmartEngine smartEngine) {
        context.getProcessInstance().setStatus(InstanceStatus.aborted);
        smartEngine.getProcessCommandService().abort(executionInstance.getProcessInstanceId(),
            InstanceStatus.aborted.name());
        context.setNeedPause(true);
    }

}
