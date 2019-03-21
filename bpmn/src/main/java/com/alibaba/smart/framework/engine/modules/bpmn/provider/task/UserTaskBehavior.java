package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.TaskAssigneeDispatcher;
import com.alibaba.smart.framework.engine.configuration.TaskItemCompleteProcessor;
import com.alibaba.smart.framework.engine.constant.ProcessInstanceModeConstant;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskItemInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.UserTask;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

import org.springframework.util.StringUtils;

public class UserTaskBehavior extends AbstractActivityBehavior<UserTask> {

    public UserTaskBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    /*
    protected void beforeEnter(ExecutionContext context) {
        ProcessInstance processInstance = context.getProcessInstance();

        UserTask userTask = this.getModel();

        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = (MultiInstanceLoopCharacteristics)userTask
            .getExecutePolicy();
        if(null!= multiInstanceLoopCharacteristics)  {//TODO ettear move to MultiInstanceLoopCharacteristicsBehavior
            ActivityInstance activityInstance = this.activityInstanceFactory.create(userTask, context);
            processInstance.addActivityInstance(activityInstance);
            context.setActivityInstance(activityInstance);

            List<TaskAssigneeCandidateInstance> taskAssigneeCandidateInstanceList = getTaskAssigneeCandidateInstances(
                context, userTask);

            if(null != taskAssigneeCandidateInstanceList){
                List<ExecutionInstance> executionInstanceList = new ArrayList<ExecutionInstance>(taskAssigneeCandidateInstanceList.size());


                for (TaskAssigneeCandidateInstance taskAssigneeCandidateInstance : taskAssigneeCandidateInstanceList) {

                    TaskInstance taskInstance = buildTaskInstance(context, activityInstance, executionInstanceList);

                    List<TaskAssigneeInstance> taskAssigneeInstanceList = buildTaskAssigneeInstanceListForMultiInstanceLoopCharacteristics( taskInstance,
                        taskAssigneeCandidateInstance);

                    taskInstance.setTaskAssigneeInstanceList(taskAssigneeInstanceList);


                }


            }


        } else {
            ActivityInstance activityInstance=context.getActivityInstance();

            List<TaskAssigneeCandidateInstance> taskAssigneeCandidateInstanceList = getTaskAssigneeCandidateInstances(
                context, userTask);

            List<ExecutionInstance> executionInstanceList = new ArrayList<ExecutionInstance>(2);

            TaskInstance taskInstance = buildTaskInstance(context, activityInstance, executionInstanceList);

            List<TaskAssigneeInstance> taskAssigneeInstanceList = new ArrayList<TaskAssigneeInstance>(2);

            if(null != taskAssigneeCandidateInstanceList){
                for (TaskAssigneeCandidateInstance taskAssigneeCandidateInstance : taskAssigneeCandidateInstanceList) {

                    TaskAssigneeInstance taskAssigneeInstance = new DefaultTaskAssigneeInstance();
                    taskAssigneeInstance.setAssigneeId(taskAssigneeCandidateInstance.getAssigneeId());
                    taskAssigneeInstance.setAssigneeType(taskAssigneeCandidateInstance.getAssigneeType());

                    //taskAssigneeInstance.setProcessDefinitionIdAndVersion(taskInstance.getProcessDefinitionIdAndVersion());
                    //taskAssigneeInstance.setProcessInstanceId(taskInstance.getProcessInstanceId());
                    //taskAssigneeInstance.setTaskInstanceId(taskInstance.getInstanceId());

                    taskAssigneeInstanceList.add(taskAssigneeInstance);

                }

                taskInstance.setTaskAssigneeInstanceList(taskAssigneeInstanceList);


            }


        }



    }

    private List<TaskAssigneeInstance> buildTaskAssigneeInstanceListForMultiInstanceLoopCharacteristics(TaskInstance taskInstance,
                                                                                                        TaskAssigneeCandidateInstance taskAssigneeCandidateInstance) {
        List<TaskAssigneeInstance> taskAssigneeInstanceList = new ArrayList<TaskAssigneeInstance>(2);

        TaskAssigneeInstance taskAssigneeInstance = new DefaultTaskAssigneeInstance();
        taskAssigneeInstance.setAssigneeId(taskAssigneeCandidateInstance.getAssigneeId());

        if(!AssigneeTypeConstant.USER.equals(taskAssigneeCandidateInstance.getAssigneeType())){

            throw  new EngineException("The assigneeType should only be USER for MultiInstanceLoopCharacteristics feature");

        }

        taskAssigneeInstance.setAssigneeType(taskAssigneeCandidateInstance.getAssigneeType());

        //taskAssigneeInstance.setProcessDefinitionIdAndVersion(taskInstance.getProcessDefinitionIdAndVersion());
        //taskAssigneeInstance.setProcessInstanceId(taskInstance.getProcessInstanceId());
        //taskAssigneeInstance.setTaskInstanceId(taskInstance.getInstanceId());

        taskAssigneeInstanceList.add(taskAssigneeInstance);
        return taskAssigneeInstanceList;
    }

    private TaskInstance buildTaskInstance(ExecutionContext context, ActivityInstance activityInstance,
                                           List<ExecutionInstance> executionInstanceList) {
        ExecutionInstance executionInstance = this.executionInstanceFactory.create(activityInstance,  context);

        TaskInstance taskInstance = super.taskInstanceFactory.create(this.getModel(), executionInstance, context);

        executionInstance.setTaskInstance(taskInstance);
        executionInstanceList.add(executionInstance);
        activityInstance.setExecutionInstanceList(executionInstanceList);

        //因为这段代码是在 UserTask 的 beforeEnter 阶段执行的,执行完这段代码会基本完成本次 signal 的执行. 所以在这里LOOP执行setExecutionInstance，没什么意义。
        //context.setExecutionInstance(executionInstance);
        return taskInstance;
    }
*/
    private List<TaskAssigneeCandidateInstance> getTaskAssigneeCandidateInstances(ExecutionContext context,
                                                                                  UserTask userTask) {
        TaskAssigneeDispatcher taskAssigneeDispatcher = context.getProcessEngineConfiguration().getTaskAssigneeDispatcher();

        if(null == taskAssigneeDispatcher){
            throw  new EngineException("The taskAssigneeService can't be null for MultiInstanceLoopCharacteristics feature");
        }

        return taskAssigneeDispatcher.getTaskAssigneeCandidateInstance(userTask,context.getRequest());
    }

    @Override
    public boolean enter(ExecutionContext context) {
        UserTask userTask = this.getModel();

        List<TaskAssigneeCandidateInstance> taskAssigneeCandidateInstanceList= null;
        if(null!=context.getRequest()) {
            taskAssigneeCandidateInstanceList=(List<TaskAssigneeCandidateInstance>)context.getRequest().get("assignee");
        }
        if (null == taskAssigneeCandidateInstanceList) {
            taskAssigneeCandidateInstanceList = getTaskAssigneeCandidateInstances(context, userTask);
        }
        if (null != taskAssigneeCandidateInstanceList) {
            ExecutionInstance executionInstance = context.getExecutionInstance();

            TaskInstance taskInstance = super.taskInstanceFactory.create(this.getModel(), executionInstance, context);

            List<TaskAssigneeInstance> taskAssigneeInstanceList = new ArrayList<TaskAssigneeInstance>(2);
            IdGenerator idGenerator = context.getProcessEngineConfiguration().getIdGenerator();
            for (TaskAssigneeCandidateInstance taskAssigneeCandidateInstance : taskAssigneeCandidateInstanceList) {
                TaskAssigneeInstance taskAssigneeInstance = new DefaultTaskAssigneeInstance();
                taskAssigneeInstance.setAssigneeId(taskAssigneeCandidateInstance.getAssigneeId());
                taskAssigneeInstance.setAssigneeType(taskAssigneeCandidateInstance.getAssigneeType());
                taskAssigneeInstance.setInstanceId(idGenerator.getId());
                taskAssigneeInstanceList.add(taskAssigneeInstance);
            }
            taskInstance.setTaskAssigneeInstanceList(taskAssigneeInstanceList);

            //创建taskItemInstance
            if(ProcessInstanceModeConstant.ITEM.equals(context.getRequest().get(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_MODE))){
                List<TaskItemInstance> taskItemInstanceList = new ArrayList<TaskItemInstance>(2);
                Object subBizIds = context.getRequest().get(RequestMapSpecialKeyConstant.PROCESS_SUB_BIZ_UNIQUE_ID);
                if(subBizIds == null){
                    String activityInstanceId = (String)context.getRequest().get("activityInstanceId");
                    ExtensionPointRegistry extensionPointRegistry = context.getExtensionPointRegistry();
                    SmartEngine smartEngine = extensionPointRegistry.getExtensionPoint(SmartEngine.class);
                    ProcessEngineConfiguration processEngineConfiguration = context.getProcessEngineConfiguration();
                    TaskItemCompleteProcessor taskItemCompleteProcessor = processEngineConfiguration.getTaskItemCompleteProcessor();
                    subBizIds = taskItemCompleteProcessor.getPassedSubBizIdByActivityInstanceId(activityInstanceId, smartEngine);
                }
                if(subBizIds instanceof List){
                    List subBizIdList = (List)subBizIds;
                    for(Object subBizIdStr : subBizIdList){
                        TaskItemInstance taskItemInstance = super.taskItemInstanceFactory.create(this.getModel(), executionInstance, context);
                        taskItemInstance.setTaskInstanceId(Long.valueOf(taskInstance.getInstanceId()));
                        taskItemInstance.setSubBizId(String.valueOf(subBizIdStr));
                        taskItemInstanceList.add(taskItemInstance);
                    }
                }
                taskInstance.setTaskItemInstanceList(taskItemInstanceList);
            }

            executionInstance.setTaskInstance(taskInstance);
        }
        return true;
    }

    /*

    public boolean execute2(ExecutionContext context) {

        UserTask userTask = this.getModel();

        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskAssigneeDispatcher taskAssigneeDispatcher = context.getProcessEngineConfiguration().getTaskAssigneeDispatcher();
        MultiInstanceCounter multiInstanceCounter = context.getProcessEngineConfiguration().getMultiInstanceCounter();


        SmartEngine smartEngine = extensionPointRegistry.getExtensionPoint(SmartEngine.class);
        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();

        ExecutionInstanceStorage executionInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);
        TaskInstanceStorage taskInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);

        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = (MultiInstanceLoopCharacteristics)userTask
            .getExecutePolicy();

        if(null!= multiInstanceLoopCharacteristics)  {
            ExecutionInstance executionInstance =    context.getExecutionInstance();

            //TODO ADD INDEX
            TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
            taskInstanceQueryParam.setProcessInstanceId(executionInstance.getProcessInstanceId());
            taskInstanceQueryParam.setActivityInstanceId(executionInstance.getActivityInstanceId());
            List<TaskInstance> allTaskInstanceList = taskInstanceStorage.findTaskList(taskInstanceQueryParam);

            CompletionCondition completionCondition = multiInstanceLoopCharacteristics.getCompletionCondition();

            Integer passedTaskInstanceNumber = multiInstanceCounter.countPassedTaskInstanceNumber(executionInstance.getProcessInstanceId(),executionInstance.getActivityInstanceId(),

                smartEngine);

            Integer rejectedTaskInstanceNumber = multiInstanceCounter.countRejectedTaskInstanceNumber(executionInstance.getProcessInstanceId(),executionInstance.getActivityInstanceId(),

                smartEngine);


            boolean isCounterSignSucceeded = false;
            if(null != completionCondition){
                // 不是 all 模式
                String passedConditionExpressionContent = completionCondition.getExpressionContent();



                Number passedThresholdValue=  MvelExpressionEvaluator.getRightValueForBinaryOperationExpression(passedConditionExpressionContent);

                Double rejectedThresholdValue= 1- passedThresholdValue.doubleValue();

                String rejectConditionExpression = " nrOfRejectedInstance /nrOfCompletedInstances > " +rejectedThresholdValue;

                Map<String, Object> vars = new HashMap<String, Object>(4);
                // 此变量nrOfCompletedInstances命名并不合适,但是为了兼容 Activiti 不得不这样做.
                vars.put("nrOfCompletedInstances",passedTaskInstanceNumber);
                vars.put("nrOfRejectedInstance",rejectedTaskInstanceNumber);
                vars.put("nrOfInstances",allTaskInstanceList.size());

                boolean isCounterSignFailed  = MvelExpressionEvaluator.staticEval(rejectConditionExpression,vars);

                if(isCounterSignFailed){
                    processCommandService.abort(executionInstance.getProcessInstanceId(), InstanceStatus.aborted.name());
                    //会签失败,需要暂停
                    return  true;

                }


                isCounterSignSucceeded = MvelExpressionEvaluator.staticEval(passedConditionExpressionContent,vars);

                 if(isCounterSignSucceeded){

                     for (TaskInstance taskInstance : allTaskInstanceList) {

                         if(taskInstance.getExecutionInstanceId().equals(executionInstance.getInstanceId())){
                             continue;
                         }

                         if(TaskInstanceConstant.COMPLETED .equals(taskInstance.getStatus())){
                             continue;
                         }

                         // 这里产生了db 读写访问,
                         taskInstance.setStatus(TaskInstanceConstant.CANCELED);
                         Date currentDate = DateUtil.getCurrentDate();
                         taskInstance.setCompleteTime(currentDate);
                         taskInstanceStorage.update(taskInstance);

                         ExecutionInstance executionInstance1=   executionInstanceStorage.find(taskInstance.getExecutionInstanceId());

                         MarkDoneUtil.markDone(executionInstance1,executionInstanceStorage);
                     }

                     // 会签完成,不需要暂停.
                     return  false;
                 } else{
                     //会签未完成,需要暂停
                     return true;
                 }
            } else {
                //  all 模式 ; 只要一个失败,那么理解 reject. fail fast
                // 只要一个失败,那么理解 reject. fail fast
                if(rejectedTaskInstanceNumber >=1){
                    // 整个会签任务失败,订单终止.abort 所有关联的 ei 和 ti.
                    processCommandService.abort(executionInstance.getProcessInstanceId(), InstanceStatus.aborted.name());
                    //会签失败,需要暂停
                    return  true;
                }

                //TODO 需要结合锁机制.
                if(passedTaskInstanceNumber.equals(allTaskInstanceList.size())){
                    // 会签完成,不需要暂停.
                    return  false;
                }else{
                    //会签未完成,需要暂停
                    return true;
                }
            }





            //// TODO 不够通用,需要扩展
            //if(userTask.getProperties().get("approvalType").equals("anyone") ){
            //
            //    //任何一个任务通过,那么将其他任务取消掉.
            //    if(taskAssigneeDispatcher.canPassThrough(userTask,context.getRequest())){
            //        //mark done other
            //            for (TaskInstance taskInstance : allTaskInstanceList) {
            //
            //            if(taskInstance.getExecutionInstanceId().equals(executionInstance.getInstanceId())){
            //                continue;
            //            }
            //
            //            // 这里产生了db 读写访问
            //            taskInstance.setStatus(TaskInstanceConstant.COMPLETED);
            //            Date currentDate = DateUtil.getCurrentDate();
            //            taskInstance.setCompleteTime(currentDate);
            //            taskInstanceStorage.update(taskInstance);
            //
            //            ExecutionInstance executionInstance1=   executionInstanceStorage.find(taskInstance.getExecutionInstanceId());
            //
            //            MarkDoneUtil.markDone(executionInstance1,executionInstanceStorage);
            //
            //        }
            //
            //        needPause = false;
            //    } else {
            //        //所有任务都未通过,那么将流程实例关掉,其他活跃的实例,任务也都关闭掉. 并行网关下可能由其他活跃的实例,所以需要全局关闭.
            //        boolean allTaskInstanceCompleted = isAllTaskInstanceCompleted(allTaskInstanceList);
            //        if(allTaskInstanceCompleted){
            //            processCommandService.abort(executionInstance.getProcessInstanceId());
            //            needPause = true;
            //
            //        }else{
            //            needPause = true;
            //
            //        }
            //    }
            //
            //
            //
            //
            //    return needPause;
            //
            //} else if(userTask.getProperties().get("approvalType").equals("all")){
            //
            //    // Am I the last one? yes return false,no return true.
            //
            //    boolean allWeDone = isAllTaskInstanceCompleted(allTaskInstanceList);
            //
            //    if(allWeDone){
            //        return false;
            //    }else {
            //        return true;
            //    }
            //
            //} else {
            //    throw new EngineException("should have ONE solution at least.");
            //}








        } else{
            return false;
        }

    }

    private boolean isAllTaskInstanceCompleted(List<TaskInstance> taskInstanceList) {
        boolean allWeDone = true;
        for (TaskInstance taskInstance : taskInstanceList) {

            if(!TaskInstanceConstant.COMPLETED.equals(taskInstance.getStatus())){
                allWeDone = false;
                break;
            }
        }
        return allWeDone;
    }
    */

}
