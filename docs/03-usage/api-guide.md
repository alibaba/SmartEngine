# API 指南：CQRS（start / signal / query）+ 幂等建议

SmartEngine 的对外权限集中在 `SmartEngine` 接口，它把服务分为两类：

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


> 提示：同一个权限经常存在 tenantId 重载；多租户系统建议统一封装一层 Facade，把 tenantId 的注入与 request special key 的注入都收敛到入口。

## 3. Query API（常见查询路径）

你最常用的查询通常是：

1) 通过 `ProcessInstance` 查当前运行状态
2) 通过 `ExecutionInstance` 找 active token（尤其是 receiveTask/并行场景）
3) 通过 `TaskInstance` 查待办与办理记录
4) 通过 `VariableInstance` 查业务变量（或用于网关条件）

### 3.1 Fluent Query API（推荐）

**3.7 版本新增**。对于 Task、ProcessInstance、Supervision、Notification 的查询，推荐使用 Fluent Query API 替代旧版 QueryService：

```java
// 推荐：Fluent API
List<TaskInstance> tasks = smartEngine.createTaskQuery()
    .taskAssignee(userId)
    .taskStatus(status != null, status)    // 条件过滤
    .orderByCreateTime().desc()
    .listPage(0, 10);

// 旧版（仍可用，但建议逐步迁移）
TaskInstanceQueryParam param = new TaskInstanceQueryParam();
param.setClaimUserId(userId);
param.setStatus(status);
List<TaskInstance> tasks = taskQueryService.findList(param);
```

完整文档：[Fluent Query API 使用指南](fluent-query-guide.md)

### 3.2 旧版 QueryService API 说明

以下 QueryService 接口目前**仍然需要**，因为 Fluent API 尚未覆盖：

- `ExecutionQueryService` — 执行实例查询
- `ActivityQueryService` — 活动实例查询
- `DeploymentQueryService` — 部署管理查询
- `RepositoryQueryService` — 流程定义缓存查询
- `TaskAssigneeQueryService` — 含 group 关联的任务分配人查询
- `VariableQueryService` — 流程变量查询
- `TaskQueryService.findPendingTaskList()` — 含 assignee group 的待办查询
- `TaskQueryService.findTaskListByAssignee()` — 同上

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

SmartEngine 作为 SDK，**不在内部强制事务控制**，事务边界由调用方决定。这种设计提供了最大的灵活性，但需要调用方正确使用事务。

### 5.1 基本原则

- **Custom 模式**：推荐把引擎推进与业务写入放在同一事务（你掌控存储接口）
- **DataBase 模式**：推荐用 Spring 事务包裹"引擎调用 + 业务写入"，并明确：
  - 引擎写入失败时整体回滚
  - 业务写入失败时引擎回滚（避免产生"孤儿流程实例/任务"）

### 5.2 需要事务保护的关键方法

以下方法涉及**多个数据库操作**，调用方**必须**在事务上下文中使用，否则可能导致数据不一致：

| 方法 | 内部操作 | 不加事务的风险 |
|------|---------|---------------|
| `SupervisionCommandService.createSupervision()` | 插入督办记录 → 更新任务优先级 → 发送通知 | 督办创建成功但优先级未更新 |
| `TaskCommandService.complete()` | 标记任务完成 → 关闭督办 → 触发流程执行 | 任务完成但流程未继续 |
| `TaskCommandService.rollbackTask()` | 记录回退 → 执行跳转 | 回退记录存在但流程未回退 |
| `TaskCommandService.transferWithReason()` | 执行转交 → 记录操作 | 转交成功但无记录 |
| `ProcessCommandService.start()` | 创建流程实例 → 创建执行实例 → 创建任务 | 孤儿流程实例 |

### 5.3 正确用法示例

```java
@Service
public class WorkflowFacade {

    @Autowired
    private SmartEngine smartEngine;

    /**
     * 创建督办 - 必须在事务中调用
     */
    @Transactional(rollbackFor = Exception.class)
    public SupervisionInstance createSupervision(String taskId, String supervisorId,
                                                  String reason, String type, String tenantId) {
        return smartEngine.getSupervisionCommandService()
            .createSupervision(taskId, supervisorId, reason, type, tenantId);
    }

    /**
     * 完成任务 - 必须在事务中调用
     */
    @Transactional(rollbackFor = Exception.class)
    public ProcessInstance completeTask(String taskId, Map<String, Object> request) {
        // 业务校验
        validateBusinessRules(taskId);

        // 引擎操作（会自动加入当前事务）
        ProcessInstance result = smartEngine.getTaskCommandService().complete(taskId, request);

        // 业务后处理
        postProcessCompletion(result);

        return result;
    }

    /**
     * 组合操作 - 多个引擎调用在同一事务中
     */
    @Transactional(rollbackFor = Exception.class)
    public void submitWithSupervision(String taskId, String supervisorId,
                                      Map<String, Object> request) {
        // 先创建督办
        smartEngine.getSupervisionCommandService()
            .createSupervision(taskId, supervisorId, "紧急处理", "urge", getTenantId());

        // 再完成任务
        smartEngine.getTaskCommandService().complete(taskId, request);

        // 如果任一步骤失败，整体回滚
    }
}
```

### 5.4 事务传播行为

SmartEngine 内部的数据库操作会**自动参与调用方的事务**（Spring 事务传播默认是 `REQUIRED`）：

```
调用方事务
├── smartEngine.createSupervision()
│   ├── INSERT supervision_instance  ← 参与外部事务
│   ├── UPDATE task_instance         ← 参与外部事务
│   └── INSERT notification_instance ← 参与外部事务
└── 其他业务操作
```

如果调用方未开启事务，每个数据库操作将**独立提交**，无法保证原子性。

### 5.5 Spring 事务配置示例

```xml
<!-- Spring 事务管理器配置 -->
<bean id="transactionManager"
      class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>

<!-- 启用注解事务 -->
<tx:annotation-driven transaction-manager="transactionManager"/>
```

或使用 Java 配置：

```java
@Configuration
@EnableTransactionManagement
public class TransactionConfig {

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
```

详见：`04-persistence/storage-overview.md`

