package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
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
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.UserTask;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.Performer;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

@ExtensionBinding(type = ExtensionConstant.ACTIVITY_BEHAVIOR, bindingTo = UserTask.class)

public class UserTaskBehavior extends AbstractActivityBehavior<UserTask> {


    public UserTaskBehavior() {
        super();
    }


    private List<TaskAssigneeCandidateInstance> getTaskAssigneeCandidateInstances(ExecutionContext context,
                                                                                  UserTask userTask) {
        TaskAssigneeDispatcher taskAssigneeDispatcher = context.getProcessEngineConfiguration().getTaskAssigneeDispatcher();

        if(null == taskAssigneeDispatcher){
            throw  new EngineException("The taskAssigneeService can't be null for UserTask feature");
        }

        return taskAssigneeDispatcher.getTaskAssigneeCandidateInstance(userTask,context.getRequest());
    }

    @Override
    public boolean enter(ExecutionContext context) {
        UserTask userTask = this.getModel();

        List<TaskAssigneeCandidateInstance> taskAssigneeCandidateInstanceList = getTaskAssigneeCandidateInstances(context, userTask);


        if(null != userTask.getMultiInstanceLoopCharacteristics()){


            ActivityInstance activityInstance = super.createSingleActivityInstance(context);


            List<ExecutionInstance> executionInstanceList = new ArrayList<ExecutionInstance>(
                taskAssigneeCandidateInstanceList.size());
            activityInstance.setExecutionInstanceList(executionInstanceList);

            for (TaskAssigneeCandidateInstance taskAssigneeCandidateInstance : taskAssigneeCandidateInstanceList) {

                ExecutionInstance executionInstance = this.executionInstanceFactory.create(activityInstance,
                    context);
                executionInstanceList.add(executionInstance);
                context.setExecutionInstance(executionInstance);


                TaskInstance taskInstance = super.taskInstanceFactory.create(this.getModel(), executionInstance, context);

                List<TaskAssigneeInstance> taskAssigneeInstanceList = new ArrayList<TaskAssigneeInstance>(2);

                IdGenerator idGenerator = context.getProcessEngineConfiguration().getIdGenerator();

                buildTaskAssigneeInstance(taskAssigneeCandidateInstance, taskAssigneeInstanceList, idGenerator);

                taskInstance.setTaskAssigneeInstanceList(taskAssigneeInstanceList);
                executionInstance.setTaskInstance(taskInstance);

            }



        }else{

            super.enter(context);


            if (null != taskAssigneeCandidateInstanceList) {
                ExecutionInstance executionInstance = context.getExecutionInstance();

                TaskInstance taskInstance = super.taskInstanceFactory.create(this.getModel(), executionInstance, context);

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
                                           List<TaskAssigneeInstance> taskAssigneeInstanceList, IdGenerator idGenerator) {
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
        if(null != multiInstanceLoopCharacteristics){




                ExecutionInstance executionInstance = context.getExecutionInstance();
                //只负责完成当前executionInstance的状态更新,此时产生了 DB 写.
                MarkDoneUtil.markDoneExecutionInstance(executionInstance, this.executionInstanceStorage,
                    this.processEngineConfiguration);

                ActivityInstance activityInstance = context.getActivityInstance();

                List<ExecutionInstance> executionInstances = executionInstanceStorage.findByActivityInstanceId(
                    activityInstance.getProcessInstanceId(), activityInstance.getInstanceId(),this.processEngineConfiguration );

                //重新写回内存。
                activityInstance.setExecutionInstanceList(executionInstances);

            SmartEngine smartEngine = this.extensionPointRegistry.getExtensionPoint(SmartEngine.class);

            MultiInstanceCounter multiInstanceCounter = context.getProcessEngineConfiguration().getMultiInstanceCounter();
            Long completedInstanceCount = multiInstanceCounter.countPassedTaskInstanceNumber(
                activityInstance.getProcessInstanceId(), activityInstance.getInstanceId(),

                smartEngine);

            Long rejectedInstanceCount = multiInstanceCounter.countRejectedTaskInstanceNumber(
                activityInstance.getProcessInstanceId(), activityInstance.getInstanceId(),

                smartEngine);

            Long instanceCount = null;
            if (null != activityInstance.getExecutionInstanceList()) {
                int size = activityInstance.getExecutionInstanceList().size();
                instanceCount = Long.valueOf(size);
            } else {
                instanceCount = 0L;
            }

            Map<String, Object> privateContext = context.getPrivateContext();
            // TUNE
            privateContext.put("nrOfCompletedInstances", completedInstanceCount);
            privateContext.put("nrOfRejectedInstance", rejectedInstanceCount);
            privateContext.put("nrOfInstances", instanceCount);


                //TUNE 任务处理的并发性，需要业务程序来控制。

                //check
                boolean needAbort = false, needComplete = false;

                //使用检查器进行判断

                needAbort =null!=rejectedInstanceCount && rejectedInstanceCount>0;


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
                        if (null != multiInstanceLoopCharacteristics.getCompletionCondition()) {
                            //完成条件存在

                            if(null!=instanceCount && null!=completedInstanceCount){
                                needComplete  =     instanceCount.equals(completedInstanceCount);
                            }


                            if (!needComplete) {
                                //如果没有达到完成条件，执行中断
                                needAbort = true;
                            }
                        } else {
                            //完成条件不存在
                            needComplete = true;
                        }
                    }
                }

                if (needAbort) {
                    context.getProcessInstance().setStatus(InstanceStatus.aborted);
                    smartEngine.getProcessCommandService().abort(executionInstance.getProcessInstanceId(), InstanceStatus.aborted.name());
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

                    PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(
                        PersisterFactoryExtensionPoint.class);

                    TaskInstanceStorage   taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);
                    List<TaskInstance> allTaskInstanceList = taskInstanceStorage.findTaskList(taskInstanceQueryParam,this.processEngineConfiguration );

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
           super.commonUpdateExecutionInstance(context);
        }



    }

}
