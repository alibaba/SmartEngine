# API 指南：CQRS（start / signal / query）+ 幂等建议

SmartEngine 的对外能力集中在 `SmartEngine` 接口，它把服务分为两类：

- **CommandService**：改变状态（start/signal/abort/complete…）
- **QueryService**：只读查询（find/get/list…）

代码位置：`core/src/main/java/com/alibaba/smart/framework/engine/SmartEngine.java`

---

## 1. SmartEngine 总入口

你在业务系统里通常会把 `SmartEngine` 作为单例注入，然后按需调用其子服务：

- `getRepositoryCommandService()`
- `getProcessCommandService()`
- `getExecutionCommandService()`
- `getTaskCommandService()`
- `getVariableCommandService()`
- `get*QueryService()`

## 2. Command API（按服务分组）

下面根据源码接口自动整理（签名以 dev 分支为准）：

### Command · DeploymentCommandService

- `DeploymentInstance createDeployment(CreateDeploymentCommand createDeploymentCommand) ;`
- `DeploymentInstance updateDeployment(UpdateDeploymentCommand updateDeploymentCommand) ;`
- `void inactivateDeploymentInstance(String deploymentInstanceId);`
- `void inactivateDeploymentInstance(String deploymentInstanceId,String tenantId);`
- `void activateDeploymentInstance(String deploymentInstanceId);`
- `void activateDeploymentInstance(String deploymentInstanceId,String tenantId);`
- `void deleteDeploymentInstanceLogically(String deploymentInstanceId);`
- `void deleteDeploymentInstanceLogically(String deploymentInstanceId,String tenantId);`

### Command · ExecutionCommandService

- `ProcessInstance signal(String executionInstanceId, Map<String, Object> request, Map<String, Object> response);`
- `ProcessInstance signal(String processInstanceId, String executionInstanceId, Map<String, Object> request, Map<String, Object> response);`
- `ProcessInstance signal(String executionInstanceId, Map<String, Object> request);`
- `ProcessInstance signal(String executionInstanceId);`
- `void markDone(String executionInstanceId);`
- `void markDone(String executionInstanceId,String tenantId);`
- `ProcessInstance jumpFrom(ProcessInstance processInstance,String activityId,String executionInstanceId, Map<String, Object> request);`
- `void retry(ProcessInstance processInstance, String activityId, ExecutionContext executionContext);`
- `ExecutionInstance createExecution(ActivityInstance activityInstance);`

### Command · NotificationCommandService

- `void markAsRead(String notificationId, String tenantId);`
- `void batchMarkAsRead(List<String> notificationIds, String tenantId);`

### Command · ProcessCommandService

- `ProcessInstance start(String processDefinitionId, String processDefinitionVersion, Map<String, Object> request, Map<String, Object> response);`
- `ProcessInstance start(String processDefinitionId, String processDefinitionVersion, Map<String, Object> request);`
- `ProcessInstance start(String processDefinitionId, String processDefinitionVersion);`
- `ProcessInstance start(String processDefinitionId, String processDefinitionVersion,String tenantId);`
- `ProcessInstance startWith(String deploymentInstanceId, String userId, Map<String, Object> request, Map<String, Object> response);`
- `ProcessInstance startWith(String deploymentInstanceId, String userId, Map<String, Object> request);`
- `ProcessInstance startWith(String deploymentInstanceId, Map<String, Object> request);`
- `ProcessInstance startWith(String deploymentInstanceId);`
- `ProcessInstance startWith(String deploymentInstanceId,String tenantId);`
- `void abort(String processInstanceId);`
- `void abort(String processInstanceId,String tenantId);`
- `void abort(String processInstanceId, String reason,String tenantId);`
- `void abort(String processInstanceId, Map<String, Object> request);`

### Command · RepositoryCommandService

- `ProcessDefinitionSource deploy(String classPathResource) ;`
- `ProcessDefinitionSource deploy(String classPathResource,String tenantId) ;`
- `ProcessDefinitionSource deploy(InputStream inputStream) ;`
- `ProcessDefinitionSource deploy(InputStream inputStream,String tenantId) ;`
- `ProcessDefinitionSource deployWithUTF8Content(String uTF8ProcessDefinitionContent) ;`
- `ProcessDefinitionSource deployWithUTF8Content(String uTF8ProcessDefinitionContent,String tenantId) ;`

### Command · SupervisionCommandService

- `void closeSupervision(String supervisionId, String tenantId);`
- `void autoCloseSupervisionByTask(String taskInstanceId, String tenantId);`

### Command · TaskCommandService

- `ProcessInstance complete(String taskId, Map<String, Object> request);`
- `ProcessInstance complete(String taskId, String userId, Map<String, Object> request);`
- `ProcessInstance complete(String taskId, Map<String, Object> request, Map<String, Object> response);`
- `void transfer(String taskId, String fromUserId, String toUserId);`
- `void transfer(String taskId, String fromUserId, String toUserId,String tenantId);`
- `TaskInstance createTask(ExecutionInstance executionInstance, String taskInstanceStatus, Map<String, Object> request);`
- `void markDone(String taskId, Map<String, Object> request);`
- `void removeTaskAssigneeCandidate(String taskId,String tenantId, TaskAssigneeCandidateInstance taskAssigneeCandidateInstance);`
- `void addTaskAssigneeCandidate(String taskId,String tenantId, TaskAssigneeCandidateInstance taskAssigneeCandidateInstance);`
- `void transferWithReason(String taskId, String fromUserId, String toUserId, String reason, String tenantId);`
- `ProcessInstance rollbackTask(String taskId, String targetActivityId, String reason, String tenantId);`
- `void addTaskAssigneeCandidateWithReason(String taskId, String tenantId, TaskAssigneeCandidateInstance taskAssigneeCandidateInstance, String reason);`
- `void removeTaskAssigneeCandidateWithReason(String taskId, String tenantId, TaskAssigneeCandidateInstance taskAssigneeCandidateInstance, String reason);`

### Command · VariableCommandService

- `void insert(VariableInstance... variableInstance);`

### Query · ActivityQueryService

- `List<ActivityInstance> findAll(String processInstanceId);`
- `List<ActivityInstance> findAll(String processInstanceId,String tenantId);`

### Query · DeploymentQueryService

- `DeploymentInstance findById(String deploymentInstanceId);`
- `DeploymentInstance findById(String deploymentInstanceId,String tenantId);`
- `List<DeploymentInstance> findList(DeploymentInstanceQueryParam deploymentInstanceQueryParam) ;`
- `Integer count(DeploymentInstanceQueryParam deploymentInstanceQueryParam);`

### Query · ExecutionQueryService

- `List<ExecutionInstance> findActiveExecutionList(String processInstanceId,String tenantId);`
- `List<ExecutionInstance> findActiveExecutionList(String processInstanceId);`
- `List<ExecutionInstance> findAll(String processInstanceId);`
- `List<ExecutionInstance> findAll(String processInstanceId,String tenantId);`

### Query · NotificationQueryService

- `List<NotificationInstance> findNotificationList(NotificationQueryParam param);`
- `Long countNotifications(NotificationQueryParam param);`
- `Long countUnreadNotifications(String receiverUserId, String tenantId);`
- `NotificationInstance findOne(String notificationId, String tenantId);`

### Query · ProcessQueryService

- `ProcessInstance findById(String processInstanceId);`
- `ProcessInstance findById(String processInstanceId,String tenantId);`
- `List<ProcessInstance> findList(ProcessInstanceQueryParam processInstanceQueryParam);`
- `Long count(ProcessInstanceQueryParam processInstanceQueryParam);`
- `List<ProcessInstance> findCompletedProcessList(CompletedProcessQueryParam param);`
- `Long countCompletedProcessList(CompletedProcessQueryParam param);`

### Query · RepositoryQueryService

- `ProcessDefinition getCachedProcessDefinition(String processDefinitionId, String version);`
- `ProcessDefinition getCachedProcessDefinition(String processDefinitionId, String version,String tenantId);`
- `ProcessDefinition getCachedProcessDefinition(String processDefinitionIdAndVersion);`
- `Collection<ProcessDefinition> getAllCachedProcessDefinition();`

### Query · SupervisionQueryService

- `List<SupervisionInstance> findSupervisionList(SupervisionQueryParam param);`
- `Long countSupervision(SupervisionQueryParam param);`
- `List<SupervisionInstance> findActiveSupervisionByTask(String taskInstanceId, String tenantId);`
- `SupervisionInstance findOne(String supervisionId, String tenantId);`

### Query · TaskAssigneeQueryService

- `List<TaskAssigneeInstance> findList(String taskInstanceId);`
- `List<TaskAssigneeInstance> findList(String taskInstanceId,String tenantId);`
- `Map<String, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(List<String> taskInstanceIdList);`
- `Map<String, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(List<String> taskInstanceIdList,String tenantId);`

### Query · TaskQueryService

- `List<TaskInstance> findPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam);`
- `Long countPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam);`
- `List<TaskInstance> findTaskListByAssignee(TaskInstanceQueryByAssigneeParam param);`
- `Long countTaskListByAssignee(TaskInstanceQueryByAssigneeParam param);`
- `List<TaskInstance> findAllPendingTaskList(String processInstanceId);`
- `List<TaskInstance> findAllPendingTaskList(String processInstanceId,String tenantId);`
- `TaskInstance findOne(String taskInstanceId);`
- `TaskInstance findOne(String taskInstanceId,String tenantId);`
- `List<TaskInstance> findList(TaskInstanceQueryParam taskInstanceQueryParam);`
- `Long count(TaskInstanceQueryParam taskInstanceQueryParam);`
- `List<TaskInstance> findCompletedTaskList(CompletedTaskQueryParam param);`
- `Long countCompletedTaskList(CompletedTaskQueryParam param);`

### Query · VariableQueryService

- `List<VariableInstance> findProcessInstanceVariableList(String processInstanceId);`
- `List<VariableInstance> findProcessInstanceVariableList(String processInstanceId,String tenantId);`
- `List<VariableInstance> findList(String processInstanceId, String executionInstanceId);`
- `List<VariableInstance> findList(String processInstanceId, String executionInstanceId,String tenantId);`


> 提示：同一个能力经常存在 tenantId 重载；多租户系统建议统一封装一层 Facade，把 tenantId 的注入与 request special key 的注入都收敛到入口。

## 3. Query API（常见查询路径）

你最常用的查询通常是：

1) 通过 `ProcessInstance` 查当前运行状态  
2) 通过 `ExecutionInstance` 找 active token（尤其是 receiveTask/并行场景）  
3) 通过 `TaskInstance` 查待办与办理记录  
4) 通过 `VariableInstance` 查业务变量（或用于网关条件）

## 4. 幂等建议（生产必做）

SmartEngine 的 API 设计允许你在上层实现幂等（尤其是 start / signal / complete）：

- **start 幂等**：用业务唯一键（订单号、申请单号）做幂等 key  
- **signal 幂等**：用事件 id（messageId）做幂等 key  
- **complete 幂等**：用 taskInstanceId + 操作类型 做幂等 key  

实现方式建议：

- 在你自己的数据库中建立幂等表（key -> result）
- start/signal/complete 前先查幂等表，已执行则直接返回结果
- 幂等写入与引擎调用尽量放同一事务/同一最终一致性链路

> 引擎内部也有一些 request special key 可以承载扩展字段，但幂等更建议由业务层做明确表设计。

## 5. 事务边界建议

- Custom 模式：推荐把引擎推进与业务写入放在同一事务（你掌控存储接口）
- DataBase 模式：推荐用 Spring 事务包裹“引擎调用 + 业务写入”，并明确：
  - 引擎写入失败时整体回滚
  - 业务写入失败时引擎回滚（避免产生“孤儿流程实例/任务”）

详见：`04-persistence/storage-overview.md`

