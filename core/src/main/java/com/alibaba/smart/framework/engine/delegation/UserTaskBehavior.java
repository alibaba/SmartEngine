package com.alibaba.smart.framework.engine.delegation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.multi.instance.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.bpmn.assembly.task.UserTask;
import com.alibaba.smart.framework.engine.common.expression.ExpressionUtil;
import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
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
import com.alibaba.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.assembly.ConditionExpression;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = UserTask.class)

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
    public boolean enter(ExecutionContext context, PvmActivity pvmActivity) {
        UserTask userTask = (UserTask)pvmActivity.getModel();

        List<TaskAssigneeCandidateInstance> taskAssigneeCandidateInstanceList = getTaskAssigneeCandidateInstances(
            context, userTask);
        

        if (null != userTask.getMultiInstanceLoopCharacteristics()) {

            ActivityInstance activityInstance = super.createSingleActivityInstance(context,userTask);

            List<ExecutionInstance> executionInstanceList = new ArrayList<ExecutionInstance>(taskAssigneeCandidateInstanceList.size());
            activityInstance.setExecutionInstanceList(executionInstanceList);
            
            List<TaskAssigneeCandidateInstance> newTaskAssigneeCandidateInstanceList = findLowPriorityTaskAssigneeList(taskAssigneeCandidateInstanceList);

            for (TaskAssigneeCandidateInstance taskAssigneeCandidateInstance : newTaskAssigneeCandidateInstanceList) {

                ExecutionInstance executionInstance = this.executionInstanceFactory.create(activityInstance, context);
                executionInstanceList.add(executionInstance);
                context.setExecutionInstance(executionInstance);

                TaskInstance taskInstance = super.taskInstanceFactory.create(userTask, executionInstance, context);
                taskInstance.setPriority(taskAssigneeCandidateInstance.getPriority());

                List<TaskAssigneeInstance> taskAssigneeInstanceList = new ArrayList<TaskAssigneeInstance>(2);

                IdGenerator idGenerator = context.getProcessEngineConfiguration().getIdGenerator();

                buildTaskAssigneeInstance(taskAssigneeCandidateInstance, taskAssigneeInstanceList, idGenerator);

                taskInstance.setTaskAssigneeInstanceList(taskAssigneeInstanceList);
                executionInstance.setTaskInstance(taskInstance);

            }

        } else {

            super.enter(context, pvmActivity);

            if (null != taskAssigneeCandidateInstanceList) {
                ExecutionInstance executionInstance = context.getExecutionInstance();

                TaskInstance taskInstance = super.taskInstanceFactory.create(userTask, executionInstance,
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

    /**
     * 查找低优先级的审批人
     * @param taskAssigneeCandidateInstanceList
     * @return
     */
	private List<TaskAssigneeCandidateInstance> findLowPriorityTaskAssigneeList(List<TaskAssigneeCandidateInstance> taskAssigneeCandidateInstanceList) {
		if(CollectionUtil.isEmpty(taskAssigneeCandidateInstanceList)) {
			return new ArrayList<TaskAssigneeCandidateInstance>(0);
		}
		int taskAssigneeCandidateInstanceSize = taskAssigneeCandidateInstanceList.size();
		//排序
		if(CollectionUtil.isNotEmpty(taskAssigneeCandidateInstanceList)) {
			Collections.sort(taskAssigneeCandidateInstanceList, new Comparator<TaskAssigneeCandidateInstance>() {
				@Override
				public int compare(TaskAssigneeCandidateInstance one, TaskAssigneeCandidateInstance two) {
					return one.getPriority() - two.getPriority();
				}
			});
		}
		//优先级低的和所有相同的
		List<TaskAssigneeCandidateInstance> newTaskAssigneeCandidateInstanceList = new ArrayList<TaskAssigneeCandidateInstance>(taskAssigneeCandidateInstanceSize);
		int minPriority = 0;
		for(int i = 0; i < taskAssigneeCandidateInstanceSize; i++) {
			TaskAssigneeCandidateInstance instance = taskAssigneeCandidateInstanceList.get(i);
			if(i == 0) {
				minPriority = instance.getPriority();
				newTaskAssigneeCandidateInstanceList.add(instance);
			}else {
				if(instance.getPriority() == minPriority) {
					newTaskAssigneeCandidateInstanceList.add(instance);
				}else {
					break;
				}
			}
		}
		return newTaskAssigneeCandidateInstanceList;
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
    public void execute(ExecutionContext context, PvmActivity pvmActivity) {

        UserTask userTask = (UserTask) pvmActivity.getModel();

        super.makeExtensionWorkAndExecuteBehavior(context,userTask);
        
        MultiInstanceCounter multiInstanceCounter = context.getProcessEngineConfiguration().getMultiInstanceCounter();
        
        ActivityInstance activityInstance = context.getActivityInstance();
        
        SmartEngine smartEngine = processEngineConfiguration.getSmartEngine();
        
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
        
        Integer passedTaskInstanceNumber = 0;
        Integer rejectedTaskInstanceNumber = 0;
        Integer totalInstanceCount = totalExecutionInstanceList.size();
        if(multiInstanceCounter != null) {
        	passedTaskInstanceNumber = multiInstanceCounter.countPassedTaskInstanceNumber(
                    activityInstance.getProcessInstanceId(), activityInstance.getInstanceId(), smartEngine);
            rejectedTaskInstanceNumber = multiInstanceCounter.countRejectedTaskInstanceNumber(
                    activityInstance.getProcessInstanceId(), activityInstance.getInstanceId(), smartEngine);
        }

        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = userTask
            .getMultiInstanceLoopCharacteristics();
        if (null != multiInstanceLoopCharacteristics) {

            Map<String,Object> requestContext=context.getRequest();
            if(null == requestContext){
                requestContext = new HashMap<String, Object>();
            }
            //取所有审批人
            List<TaskAssigneeCandidateInstance> taskAssigneeCandidateInstanceList = processEngineConfiguration.getTaskAssigneeDispatcher()
            		.getTaskAssigneeCandidateInstance(userTask, context.getRequest());
            Map<String, TaskAssigneeCandidateInstance> taskAssigneeMap = new HashMap<String, TaskAssigneeCandidateInstance>();
            if(taskAssigneeCandidateInstanceList != null) {
            	totalInstanceCount = taskAssigneeCandidateInstanceList.size();
            	for(TaskAssigneeCandidateInstance item : taskAssigneeCandidateInstanceList) {
            		taskAssigneeMap.put(item.getAssigneeId(), item);
            	}
            }

            // 不变式  nrOfCompletedInstances+ nrOfRejectedInstance <= nrOfInstances
            requestContext.put("nrOfCompletedInstances", passedTaskInstanceNumber);
            requestContext.put("nrOfRejectedInstance", rejectedTaskInstanceNumber);
            requestContext.put("nrOfInstances", totalInstanceCount);
            
            

            // 注意：任务处理的并发性，需要业务程序来控制。
            boolean abortMatched = false;

            ConditionExpression abortCondition = multiInstanceLoopCharacteristics.getAbortCondition();

            if (null != abortCondition) {
                abortMatched = ExpressionUtil.eval(context, abortCondition);
            }

            //此时，尚未触发订单abort逻辑
            if (!abortMatched) {
                ConditionExpression completionCondition = multiInstanceLoopCharacteristics.getCompletionCondition();

                if(null != completionCondition){
                    boolean passedMatched  = ExpressionUtil.eval(context, completionCondition) ;

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


                            TaskInstanceStorage taskInstanceStorage = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(
                                ExtensionConstant.COMMON,TaskInstanceStorage.class);
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
                            //生成新节点，硬编码需优化
                            generateExecutionAndTaskForMultiInstance(context, userTask, activityInstance, executionInstance, taskAssigneeMap);
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
                        //生成新节点
                        generateExecutionAndTaskForMultiInstance(context, userTask, activityInstance, executionInstance, taskAssigneeMap);
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
        	//普通节点多任务时只支持all-pass
    		if(rejectedTaskInstanceNumber > 0) {
            	abort(context, executionInstance, smartEngine);
            }
        	if(passedTaskInstanceNumber < totalInstanceCount  ){
                context.setNeedPause(true);
            } else{
                context.setNeedPause(false);
            }
        }
    
    }

    /**
     * 会签生成新任务
     * @param context
     * @param userTask
     * @param activityInstance
     * @param executionInstance
     * @param taskAssigneeMap
     */
	private void generateExecutionAndTaskForMultiInstance(ExecutionContext context, UserTask userTask,
			ActivityInstance activityInstance, ExecutionInstance executionInstance,
			Map<String, TaskAssigneeCandidateInstance> taskAssigneeMap) {
		TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
		List<String> processInstanceIdList = new ArrayList<String>(2);
		processInstanceIdList.add(executionInstance.getProcessInstanceId());
		taskInstanceQueryParam.setProcessInstanceIdList(processInstanceIdList);
		taskInstanceQueryParam.setActivityInstanceId(executionInstance.getActivityInstanceId());
		TaskInstanceStorage taskInstanceStorage = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(
		    ExtensionConstant.COMMON,TaskInstanceStorage.class);
		List<TaskInstance> allTaskInstanceList = taskInstanceStorage.findTaskList(taskInstanceQueryParam,
		    this.processEngineConfiguration);
		List<String> taskInstanceIdList = new ArrayList<String>();
		if(CollectionUtil.isNotEmpty(allTaskInstanceList)) {
			for(TaskInstance taskInstance : allTaskInstanceList) {
				taskInstanceIdList.add(taskInstance.getInstanceId());
			}
		}
		TaskAssigneeStorage taskAssigneeStorage = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(
		        ExtensionConstant.COMMON, TaskAssigneeStorage.class);
		Map<String, List<TaskAssigneeInstance>> taskAssigneeInstanceMap = taskAssigneeStorage.findAssigneeOfInstanceList(taskInstanceIdList, processEngineConfiguration);
		if(taskAssigneeInstanceMap != null) {
			for(List<TaskAssigneeInstance> instanceList : taskAssigneeInstanceMap.values()) {
				if(CollectionUtil.isNotEmpty(instanceList)) {
					for(TaskAssigneeInstance instance : instanceList) {
						if(taskAssigneeMap.containsKey(instance.getAssigneeId())) {
							taskAssigneeMap.remove(instance.getAssigneeId());
						}
					}
				}
			}
		}
		if(taskAssigneeMap.size() > 0) {
			List<TaskAssigneeCandidateInstance> newTaskAssigneeList = findLowPriorityTaskAssigneeList(new ArrayList<TaskAssigneeCandidateInstance>(taskAssigneeMap.values()));
		    for (TaskAssigneeCandidateInstance taskAssigneeCandidateInstance : newTaskAssigneeList) {
		        ExecutionInstance newExecutionInstance = this.executionInstanceFactory.create(activityInstance, context);
		        executionInstanceStorage.insert(newExecutionInstance, processEngineConfiguration);

		        TaskInstance taskInstance = super.taskInstanceFactory.create(userTask, newExecutionInstance, context);
		        taskInstance.setPriority(taskAssigneeCandidateInstance.getPriority());
		        taskInstanceStorage.insert(taskInstance, processEngineConfiguration);
		        
		        List<TaskAssigneeInstance> taskAssigneeInstanceList = new ArrayList<TaskAssigneeInstance>(2);
		        IdGenerator idGenerator = context.getProcessEngineConfiguration().getIdGenerator();
		        buildTaskAssigneeInstance(taskAssigneeCandidateInstance, taskAssigneeInstanceList, idGenerator);
		        for(TaskAssigneeInstance taskAssigneeInstance : taskAssigneeInstanceList) {
		        	taskAssigneeInstance.setProcessInstanceId(taskInstance.getProcessInstanceId());
		        	taskAssigneeInstance.setTaskInstanceId(taskInstance.getInstanceId());
		        	taskAssigneeStorage.insert(taskAssigneeInstance, processEngineConfiguration);
		        }
		    }
		}
	}

    protected void abort(ExecutionContext context, ExecutionInstance executionInstance, SmartEngine smartEngine) {
        context.getProcessInstance().setStatus(InstanceStatus.aborted);
        smartEngine.getProcessCommandService().abort(executionInstance.getProcessInstanceId(),
            InstanceStatus.aborted.name());
        context.setNeedPause(true);
    }

}
