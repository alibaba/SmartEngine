package com.alibaba.smart.framework.engine.behavior.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.multi.instance.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.bpmn.assembly.task.UserTask;
import com.alibaba.smart.framework.engine.common.expression.ExpressionUtil;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.configuration.MultiInstanceCounter;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.exception.ValidationException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.ConditionExpression;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.event.EventConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = UserTask.class)

public class UserTaskBehavior extends AbstractActivityBehavior<UserTask> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserTaskBehavior.class);


    public UserTaskBehavior() {
        super();
    }

    @Override
    public boolean enter(ExecutionContext context, PvmActivity pvmActivity) {
        UserTask userTask = (UserTask) pvmActivity.getModel();

        // Introduce explaining variable
        List<TaskAssigneeCandidateInstance> candidateInstances = UserTaskBehaviorHelper.getTaskAssigneeCandidateInstances(context, userTask);

        ProcessInstance processInstance = context.getProcessInstance();

        LOGGER.info("Task Assignee Candidates: " + candidateInstances +
                " for Process Instance: " + processInstance.getInstanceId() +
                " and Activity ID: " + pvmActivity.getModel().getId());

        // Check if multi-instance characteristics are present
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = userTask.getMultiInstanceLoopCharacteristics();

        if (multiInstanceLoopCharacteristics != null) {
            handleMultiInstanceTask(context, pvmActivity, userTask, candidateInstances, multiInstanceLoopCharacteristics);
        } else {
            handleStandardTask(context, pvmActivity, userTask, candidateInstances);
        }

        return true;
    }

    // Extracted method for handling multi-instance tasks
    private void handleMultiInstanceTask(ExecutionContext context, PvmActivity pvmActivity,
                                         UserTask userTask,
                                         List<TaskAssigneeCandidateInstance> candidateInstances,
                                         MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics) {
        fireEvent(context, pvmActivity, EventConstant.ACTIVITY_START);

        ActivityInstance activityInstance = super.createSingleActivityInstanceAndAttachToProcessInstance(context, userTask);
        List<ExecutionInstance> executionInstanceList = new ArrayList<>(candidateInstances.size());
        activityInstance.setExecutionInstanceList(executionInstanceList);

        List<TaskAssigneeCandidateInstance> prioritizedInstances = multiInstanceLoopCharacteristics.isSequential()
                ? UserTaskBehaviorHelper.findBatchOfHighestPriorityTaskAssigneeList(candidateInstances)
                : candidateInstances;

        for (TaskAssigneeCandidateInstance candidateInstance : prioritizedInstances) {
            createExecutionAndTaskInstances(context, activityInstance, userTask, candidateInstance, executionInstanceList);
        }
    }

    // Extracted method for handling standard tasks
    private void handleStandardTask(ExecutionContext context, PvmActivity pvmActivity,
                                    UserTask userTask,
                                    List<TaskAssigneeCandidateInstance> candidateInstances) {
        super.enter(context, pvmActivity);

        if (candidateInstances != null) {
            ExecutionInstance executionInstance = context.getExecutionInstance();

            TaskInstance taskInstance = super.taskInstanceFactory.create(userTask, executionInstance, context);

            List<TaskAssigneeInstance> assigneeInstances = createTaskAssigneeInstances(context, candidateInstances);

            taskInstance.setTaskAssigneeInstanceList(assigneeInstances);
            executionInstance.setTaskInstance(taskInstance);
        }
    }

    // Extracted method to create Execution and Task Instances
    private void createExecutionAndTaskInstances(ExecutionContext context, ActivityInstance activityInstance,
                                                 UserTask userTask,
                                                 TaskAssigneeCandidateInstance candidateInstance,
                                                 List<ExecutionInstance> executionInstanceList) {
        ExecutionInstance executionInstance = this.executionInstanceFactory.create(activityInstance, context);
        executionInstanceList.add(executionInstance);

        TaskInstance taskInstance = super.taskInstanceFactory.create(userTask, executionInstance, context);
        taskInstance.setPriority(candidateInstance.getPriority());

        List<TaskAssigneeInstance> assigneeInstances = createTaskAssigneeInstances(context, List.of(candidateInstance));
        taskInstance.setTaskAssigneeInstanceList(assigneeInstances);

        executionInstance.setTaskInstance(taskInstance);
    }

    // Extracted method to create Task Assignee Instances
    private List<TaskAssigneeInstance> createTaskAssigneeInstances(ExecutionContext context,
                                                                   List<TaskAssigneeCandidateInstance> candidateInstances) {
        List<TaskAssigneeInstance> assigneeInstances = new ArrayList<>();
        IdGenerator idGenerator = context.getProcessEngineConfiguration().getIdGenerator();

        for (TaskAssigneeCandidateInstance candidateInstance : candidateInstances) {
            UserTaskBehaviorHelper.buildTaskAssigneeInstance(candidateInstance, assigneeInstances, idGenerator);
        }

        return assigneeInstances;
    }






    @Override
    public void execute(ExecutionContext context, PvmActivity pvmActivity) {

        fireEvent(context,pvmActivity, EventConstant.ACTIVITY_EXECUTE);


        //1. 完成当前ExecutionInstance的状态更新
        ExecutionInstance executionInstance = context.getExecutionInstance();
        //只负责完成当前executionInstance的状态更新,此时产生了 DB 写.
        MarkDoneUtil.markDoneExecutionInstance(executionInstance, this.executionInstanceStorage,
            this.processEngineConfiguration);


        UserTask userTask = (UserTask) pvmActivity.getModel();

        super.executeDelegation(context,userTask);


        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = userTask
            .getMultiInstanceLoopCharacteristics();

        if (null != multiInstanceLoopCharacteristics) {

            handleMultiInstance(context, executionInstance, userTask, multiInstanceLoopCharacteristics);

        } else {

            handleSingleInstance(context);
        }
    
    }

    protected void handleMultiInstance(ExecutionContext context, ExecutionInstance executionInstance, UserTask userTask,
                                     MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics) {
        ActivityInstance activityInstance = context.getActivityInstance();

        SmartEngine smartEngine = processEngineConfiguration.getSmartEngine();

        //重要前提: 在会签场景中,ei:ti:tai= 1:1:1 ,并且会签场景中, assigneeType 应该只能为 user.
        //1. 当前的数据库中所有的 totalExecutionInstanceList，包含所有状态的。 但是此时，由于顺序会签的问题，totalExecutionInstanceList 不再是所有的ExecutionList了。
        List<ExecutionInstance> totalExecutionInstanceList = executionInstanceStorage.findByActivityInstanceId(
            activityInstance.getProcessInstanceId(), activityInstance.getInstanceId(),
            this.processEngineConfiguration);

        // 针对顺序会签,这里totalInstanceCount 为目前已经创建出来的count,未来还会补偿新增; 但是针对非顺序会签,则是最终的全量,不会再变.
        Integer totalInstanceCount  = totalExecutionInstanceList.size();
        Integer passedTaskInstanceCount = 0;
        Integer rejectedTaskInstanceCount = 0;

        MultiInstanceCounter multiInstanceCounter = context.getProcessEngineConfiguration().getMultiInstanceCounter();

        if(multiInstanceCounter != null) {
            passedTaskInstanceCount = multiInstanceCounter.countPassedTaskInstanceNumber(
                activityInstance.getProcessInstanceId(), activityInstance.getInstanceId(), smartEngine);
            rejectedTaskInstanceCount = multiInstanceCounter.countRejectedTaskInstanceNumber(
                activityInstance.getProcessInstanceId(), activityInstance.getInstanceId(), smartEngine);
        } else {
            throw new ValidationException("MultiInstanceCounter can NOT be null for multiInstanceLoopCharacteristics");
        }

        // 不变式  nrOfCompletedInstances + nrOfRejectedInstance <= nrOfInstances
        Map<String,Object> requestContext =  new HashMap<String, Object>(4);
        requestContext.put("nrOfCompletedInstances", passedTaskInstanceCount);
        requestContext.put("nrOfRejectedInstance", rejectedTaskInstanceCount);
        requestContext.put("nrOfInstances", totalInstanceCount);

        // 注意：任务处理的并发性，需要业务程序来控制。
        boolean abortMatched = false;

        ConditionExpression abortCondition = multiInstanceLoopCharacteristics.getAbortCondition();

        if (null != abortCondition) {
            abortMatched = ExpressionUtil.eval(requestContext, abortCondition,context.getProcessEngineConfiguration());
        }

        //此时，尚未触发订单abort逻辑
        if (!abortMatched) {
            ConditionExpression completionCondition = multiInstanceLoopCharacteristics.getCompletionCondition();

            if(null != completionCondition){
                boolean passedMatched  = ExpressionUtil.eval(requestContext, completionCondition,context.getProcessEngineConfiguration()) ;

                Integer completedTaskInstanceCount = passedTaskInstanceCount + rejectedTaskInstanceCount;
                if(completedTaskInstanceCount < totalInstanceCount){

                    if(passedMatched){
                        UserTaskBehaviorHelper.markDoneEIAndCancelTI(context, executionInstance, totalExecutionInstanceList,executionInstanceStorage,processEngineConfiguration);

                        context.setNeedPause(false);

                    } else {
                        context.setNeedPause(true);
                        //生成顺序型会签，需要补偿创建任务。
                        if(multiInstanceLoopCharacteristics.isSequential()){
                            Map<String, TaskAssigneeCandidateInstance> taskAssigneeMap = queryTaskAssigneeCandidateInstance(context, userTask);

                            UserTaskBehaviorHelper.compensateExecutionAndTask(context, userTask, activityInstance, executionInstance, taskAssigneeMap,executionInstanceStorage,executionInstanceFactory,taskInstanceFactory,processEngineConfiguration);
                        }else{
                            // do nothing
                        }
                    }

                }else if(completedTaskInstanceCount.equals(totalInstanceCount)){

                    if(passedMatched){
                        context.setNeedPause(false);
                    }else {
                        UserTaskBehaviorHelper.abortAndSetNeedPause(context, executionInstance, smartEngine);
                    }

                }else{
                    handleException(totalInstanceCount, passedTaskInstanceCount, rejectedTaskInstanceCount);
                }

            }
            else{
                //completionCondition 为空时，则表示是all模式 （兼容历史逻辑）， 则需要所有任务都完成后，才做判断。

                if(rejectedTaskInstanceCount >= 1){
                    //fail fast ,abort
                    UserTaskBehaviorHelper.abortAndSetNeedPause(context, executionInstance, smartEngine);
                    UserTaskBehaviorHelper.markDoneEIAndCancelTI(context, executionInstance, totalExecutionInstanceList,executionInstanceStorage,processEngineConfiguration);

                } else if(passedTaskInstanceCount < totalInstanceCount  ){
                    context.setNeedPause(true);

                    if(multiInstanceLoopCharacteristics.isSequential()){
                        Map<String, TaskAssigneeCandidateInstance> taskAssigneeMap = queryTaskAssigneeCandidateInstance(context, userTask);

                        UserTaskBehaviorHelper.compensateExecutionAndTask(context, userTask, activityInstance, executionInstance, taskAssigneeMap,executionInstanceStorage,executionInstanceFactory,taskInstanceFactory,processEngineConfiguration);
                    } else {
                        //do nothing
                    }
                }
                else  if(passedTaskInstanceCount.equals(totalInstanceCount)  ){
                    context.setNeedPause(false);
                }else{
                    handleException(totalInstanceCount, passedTaskInstanceCount, rejectedTaskInstanceCount);

                }

            }

        } else {

            UserTaskBehaviorHelper.abortAndSetNeedPause(context, executionInstance, smartEngine);
            UserTaskBehaviorHelper.markDoneEIAndCancelTI(context, executionInstance, totalExecutionInstanceList,executionInstanceStorage,processEngineConfiguration);

        }
    }

    private Map<String, TaskAssigneeCandidateInstance> queryTaskAssigneeCandidateInstance(ExecutionContext context, UserTask userTask) {
        //针对 顺序会签,需要业务传入所需要的任务处理者,以便未来补偿创建.
        Map<String, TaskAssigneeCandidateInstance> taskAssigneeMap = new HashMap<String, TaskAssigneeCandidateInstance>();

        List<TaskAssigneeCandidateInstance> taskAssigneeCandidateInstanceList = UserTaskBehaviorHelper
         .getTaskAssigneeCandidateInstances(context, userTask);

        if(taskAssigneeCandidateInstanceList != null) {
            for(TaskAssigneeCandidateInstance assigneeCandidateInstance : taskAssigneeCandidateInstanceList) {
                taskAssigneeMap.put(assigneeCandidateInstance.getAssigneeId(), assigneeCandidateInstance);
            }
        }
        return taskAssigneeMap;
    }

    protected void handleSingleInstance(ExecutionContext context) {
        super.commonUpdateExecutionInstance(context);
    }

    private void handleException(Integer totalInstanceCount, Integer passedTaskInstanceCount,
                                 Integer rejectedTaskInstanceCount) {
        String message =
            "Error args: passedTaskInstanceCount, rejectedTaskInstanceCount, totalInstanceCount each is :"
                + passedTaskInstanceCount+"," + rejectedTaskInstanceCount+"," + totalInstanceCount+".";
        throw new EngineException(message);
    }

}
