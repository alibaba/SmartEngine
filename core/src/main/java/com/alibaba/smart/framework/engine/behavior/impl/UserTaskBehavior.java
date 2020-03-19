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
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

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
        UserTask userTask = (UserTask)pvmActivity.getModel();

        List<TaskAssigneeCandidateInstance> allTaskAssigneeCandidateInstanceList = UserTaskBehaviorHelper.getTaskAssigneeCandidateInstances(
            context, userTask);

        LOGGER.info("The taskAssigneeCandidateInstance are "+allTaskAssigneeCandidateInstanceList +" for PI:"+context.getProcessInstance().getInstanceId() +" and AI id: "+pvmActivity.getModel().getId() );

        
        //1. 开启了会签特性
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = userTask
            .getMultiInstanceLoopCharacteristics();

        if (null != multiInstanceLoopCharacteristics) {

            ActivityInstance activityInstance = super.createSingleActivityInstance(context,userTask);

            List<ExecutionInstance> executionInstanceList = new ArrayList<ExecutionInstance>(allTaskAssigneeCandidateInstanceList.size());
            activityInstance.setExecutionInstanceList(executionInstanceList);

            //1.1 会签节点有两种业务类型，分别是 顺序会签 和 并发会签。 前者需要按照特定顺序的节点来审批，后者则需要是并发会签。
            // 针对顺序会签，则需要指定顺序，并且需要在 UserTask 节点完成的时候，去补充创建下一批任务。
            // 针对并发会签，则会批量创建出所有的会签任务。

            List<TaskAssigneeCandidateInstance> newTaskAssigneeCandidateInstanceList = null;

            if(multiInstanceLoopCharacteristics.isSequential()){
                 newTaskAssigneeCandidateInstanceList = UserTaskBehaviorHelper.findBatchOfHighestPriorityTaskAssigneeList(allTaskAssigneeCandidateInstanceList);
            }else{
                newTaskAssigneeCandidateInstanceList = allTaskAssigneeCandidateInstanceList;
            }

            for (TaskAssigneeCandidateInstance taskAssigneeCandidateInstance : newTaskAssigneeCandidateInstanceList) {

                ExecutionInstance executionInstance = this.executionInstanceFactory.create(activityInstance, context);
                executionInstanceList.add(executionInstance);


                TaskInstance taskInstance = super.taskInstanceFactory.create(userTask, executionInstance, context);
                taskInstance.setPriority(taskAssigneeCandidateInstance.getPriority());

                List<TaskAssigneeInstance> taskAssigneeInstanceList = new ArrayList<TaskAssigneeInstance>(2);

                IdGenerator idGenerator = context.getProcessEngineConfiguration().getIdGenerator();

                UserTaskBehaviorHelper.buildTaskAssigneeInstance(taskAssigneeCandidateInstance, taskAssigneeInstanceList, idGenerator);

                taskInstance.setTaskAssigneeInstanceList(taskAssigneeInstanceList);

                executionInstance.setTaskInstance(taskInstance);

            }

        } else {

            //2.  普通任务节点
            super.enter(context, pvmActivity);

            if (null != allTaskAssigneeCandidateInstanceList) {
                ExecutionInstance executionInstance = context.getExecutionInstance();

                TaskInstance taskInstance = super.taskInstanceFactory.create(userTask, executionInstance,
                    context);

                List<TaskAssigneeInstance> taskAssigneeInstanceList = new ArrayList<TaskAssigneeInstance>(2);

                IdGenerator idGenerator = context.getProcessEngineConfiguration().getIdGenerator();

                for (TaskAssigneeCandidateInstance taskAssigneeCandidateInstance : allTaskAssigneeCandidateInstanceList) {
                    UserTaskBehaviorHelper.buildTaskAssigneeInstance(taskAssigneeCandidateInstance, taskAssigneeInstanceList, idGenerator);
                }

                //2.1 普通UserTask，只会创建出一个TI和可能多个TACI(TaskAssigneeCandidateInstance)
                taskInstance.setTaskAssigneeInstanceList(taskAssigneeInstanceList);
                executionInstance.setTaskInstance(taskInstance);
            }
        }

        return true;
    }





    @Override
    public void execute(ExecutionContext context, PvmActivity pvmActivity) {

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

        //1. 当前的数据库中所有的 totalExecutionInstanceList，包含所有状态的。 但是此时，由于顺序会签的问题，totalExecutionInstanceList 不再是所有的ExecutionList了。
        List<ExecutionInstance> totalExecutionInstanceList = executionInstanceStorage.findByActivityInstanceId(
            activityInstance.getProcessInstanceId(), activityInstance.getInstanceId(),
            this.processEngineConfiguration);

        //取所有审批人
        List<TaskAssigneeCandidateInstance> taskAssigneeCandidateInstanceList = UserTaskBehaviorHelper
            .getTaskAssigneeCandidateInstances( context,userTask);

        Integer totalInstanceCount ;

        if(multiInstanceLoopCharacteristics.isSequential()){
            totalInstanceCount = taskAssigneeCandidateInstanceList.size();
        }else {
            totalInstanceCount = totalExecutionInstanceList.size();
        }

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

        Map<String, TaskAssigneeCandidateInstance> taskAssigneeMap = new HashMap<String, TaskAssigneeCandidateInstance>();

        if(taskAssigneeCandidateInstanceList != null) {
            for(TaskAssigneeCandidateInstance item : taskAssigneeCandidateInstanceList) {
                taskAssigneeMap.put(item.getAssigneeId(), item);
            }
        }

        // 不变式  nrOfCompletedInstances+ nrOfRejectedInstance <= nrOfInstances
        Map<String,Object> requestContext =  new HashMap<String, Object>();
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
                            UserTaskBehaviorHelper.compensateExecutionAndTask(context, userTask, activityInstance, executionInstance, taskAssigneeMap,executionInstanceStorage,executionInstanceFactory,taskInstanceFactory,processEngineConfiguration);
                        }else{
                            // do nothing
                        }
                    }

                }else if(completedTaskInstanceCount == totalInstanceCount){

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

                    UserTaskBehaviorHelper.abortAndSetNeedPause(context, executionInstance, smartEngine);
                    UserTaskBehaviorHelper.markDoneEIAndCancelTI(context, executionInstance, totalExecutionInstanceList,executionInstanceStorage,processEngineConfiguration);

                } else if(passedTaskInstanceCount < totalInstanceCount  ){
                    context.setNeedPause(true);

                    if(multiInstanceLoopCharacteristics.isSequential()){
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
