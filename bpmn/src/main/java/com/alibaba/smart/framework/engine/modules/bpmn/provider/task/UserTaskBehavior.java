package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.common.service.TaskAssigneeService;
import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.constant.AssigneeTypeConstant;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskAssigneeInstance;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.assembly.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.UserTask;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

public class UserTaskBehavior extends AbstractActivityBehavior<UserTask> {

    public UserTaskBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    @Override
    protected void beforeEnter(ExecutionContext context) {
        ProcessInstance processInstance = context.getProcessInstance();

        UserTask userTask = this.getModel();

        ActivityInstance activityInstance = this.activityInstanceFactory.create(userTask, context);
        processInstance.addNewActivityInstance(activityInstance);
        context.setActivityInstance(activityInstance);



        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = userTask
            .getMultiInstanceLoopCharacteristics();
        if(null!= multiInstanceLoopCharacteristics)  {
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


        }else{

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

                    taskAssigneeInstance.setProcessDefinitionIdAndVersion(taskInstance.getProcessDefinitionIdAndVersion());
                    taskAssigneeInstance.setProcessInstanceId(taskInstance.getProcessInstanceId());
                    taskAssigneeInstance.setTaskInstanceId(taskInstance.getInstanceId());

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

        taskAssigneeInstance.setProcessDefinitionIdAndVersion(taskInstance.getProcessDefinitionIdAndVersion());
        taskAssigneeInstance.setProcessInstanceId(taskInstance.getProcessInstanceId());
        taskAssigneeInstance.setTaskInstanceId(taskInstance.getInstanceId());

        taskAssigneeInstanceList.add(taskAssigneeInstance);
        return taskAssigneeInstanceList;
    }

    private TaskInstance buildTaskInstance(ExecutionContext context, ActivityInstance activityInstance,
                                           List<ExecutionInstance> executionInstanceList) {
        ExecutionInstance executionInstance = this.executionInstanceFactory.create(activityInstance,  context);

        TaskInstance taskInstance = super.taskInstanceFactory.create(this.getModel(), executionInstance, context);

        Map<String, Object> request = context.getRequest();

        if(null != request){

            //TODO 约定了key,其他。。。
            String assigneeId = (String) request.get("assigneeUserId");
            if(null != assigneeId){
                taskInstance.setClaimUserId(assigneeId);
            }

        }

        executionInstance.setTaskInstance(taskInstance);
        executionInstanceList.add(executionInstance);
        activityInstance.setExecutionInstanceList(executionInstanceList);

        //FIXME 在这里LOOP执行setExecutionInstance，没什么意义。 并且和并行网关集成起来，可能会有问题。
        context.setExecutionInstance(executionInstance);
        return taskInstance;
    }

    private List<TaskAssigneeCandidateInstance> getTaskAssigneeCandidateInstances(ExecutionContext context,
                                                                                  UserTask userTask) {
        TaskAssigneeService taskAssigneeService = context.getProcessEngineConfiguration().getTaskAssigneeService();

        if(null == taskAssigneeService){
            throw  new EngineException("The taskAssigneeService can't be null for MultiInstanceLoopCharacteristics feature");
        }

        return taskAssigneeService.getTaskAssigneeCandidateInstance(userTask,context.getRequest());
    }

    @Override
    public boolean enter(ExecutionContext context) {
        beforeEnter(context);

        return true;
    }

    @Override
    public boolean execute(ExecutionContext context) {
        UserTask userTask = this.getModel();

        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        ExecutionInstanceStorage executionInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);
        TaskInstanceStorage taskInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);


        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = userTask
            .getMultiInstanceLoopCharacteristics();

        if(null!= multiInstanceLoopCharacteristics)  {
            ExecutionInstance executionInstance =    context.getExecutionInstance();

            //FIXME ADD INDEX
            TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
            taskInstanceQueryParam.setProcessInstanceId(executionInstance.getProcessInstanceId());
            taskInstanceQueryParam.setActivityInstanceId(executionInstance.getActivityInstanceId());
            List<TaskInstance> taskInstanceList = taskInstanceStorage.findTaskList(taskInstanceQueryParam);


            if(userTask.getProperties().get("approvalType").equals("anyone")){

                //mark done other
                for (TaskInstance taskInstance : taskInstanceList) {
                    //if(taskInstance.getExecutionInstanceId().equals(executionInstance.getInstanceId())){
                    //
                    //    //FIXME  任务处理逻辑有些分散,和task不在同一处处理.
                    //    updateExecution(executionInstanceStorage, taskInstance);
                    //
                    //    continue;
                    //}
                    // todo 把complete的逻辑挪到这里?
                    // todo 这里产生了db 读写访问,能否优化?

                    taskInstance.setStatus(TaskInstanceConstant.COMPLETED);
                    Date currentDate = DateUtil.getCurrentDate();
                    taskInstance.setCompleteTime(currentDate);

                    taskInstanceStorage.update(taskInstance);

                    updateExecution(executionInstanceStorage, taskInstance);

                }



                return false;

            }else if(userTask.getProperties().get("approvalType").equals("all")){

                // Am I the last one? yes return false,no return true.

                boolean allWeDone = true;
                for (TaskInstance taskInstance : taskInstanceList) {

                    if(!TaskInstanceConstant.COMPLETED.equals(taskInstance.getStatus())){
                        allWeDone = false;
                        break;
                    }
                }

                if(allWeDone){
                    return false;
                }else {
                    return true;
                }

            }else{
                throw new EngineException("should have ONE soultion");
            }
        } else{
            return false;
        }

    }

    private void updateExecution(ExecutionInstanceStorage executionInstanceStorage, TaskInstance taskInstance) {
        ExecutionInstance executionInstance=   executionInstanceStorage.find(taskInstance.getExecutionInstanceId());
        executionInstance.setActive(false);
        //FIXME CompleteDate 和 EndDate 取一个.
        executionInstance.setCompleteTime(DateUtil.getCurrentDate());
        executionInstanceStorage.update(executionInstance);
    }
}
