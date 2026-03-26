# Fluent Query API 使用指南

SmartEngine 3.7 引入了 Fluent Query API（链式查询 API），借鉴 Activiti/Flowable 的 `Query<Q, T>` 模式，同时吸收了 MyBatis Plus 的条件过滤思想。

---

## 1. 快速上手

### 1.1 基本用法

```java
// 获取 SmartEngine 实例
SmartEngine smartEngine = ...;

// 查询待办任务
List<TaskInstance> tasks = smartEngine.createTaskQuery()
    .taskAssignee("user001")
    .taskStatus(TaskInstanceConstant.PENDING)
    .orderByCreateTime().desc()
    .listPage(0, 10);

// 查询流程实例
ProcessInstance process = smartEngine.createProcessQuery()
    .processInstanceId("12345")
    .singleResult();

// 统计数量
long count = smartEngine.createTaskQuery()
    .processInstanceId("12345")
    .taskStatus(TaskInstanceConstant.PENDING)
    .count();
```

### 1.2 可用的查询类型

| 入口方法 | 查询接口 | 查询对象 |
|---------|---------|---------|
| `smartEngine.createTaskQuery()` | `TaskQuery` | 任务实例 |
| `smartEngine.createProcessQuery()` | `ProcessInstanceQuery` | 流程实例 |
| `smartEngine.createSupervisionQuery()` | `SupervisionQuery` | 督办实例 |
| `smartEngine.createNotificationQuery()` | `NotificationQuery` | 通知实例 |

### 1.3 终端操作

每个查询必须以终端操作结尾：

| 方法 | 说明 |
|------|------|
| `list()` | 返回全部匹配结果 |
| `listPage(offset, limit)` | 分页查询 |
| `singleResult()` | 返回唯一结果（多个结果时抛异常） |
| `count()` | 返回匹配数量 |

---

## 2. 条件过滤

### 2.1 TaskQuery 完整过滤条件

```java
smartEngine.createTaskQuery()
    // === 主键与关联 ===
    .taskInstanceId("task001")               // 任务实例 ID
    .processInstanceId("proc001")            // 流程实例 ID
    .processInstanceIdIn(idList)             // 多个流程实例 ID
    .activityInstanceId("act001")            // 活动实例 ID

    // === 业务属性 ===
    .taskAssignee("user001")                 // 任务处理人
    .taskStatus("pending")                   // 任务状态（单值）
    .taskStatusIn("pending", "running")      // 任务状态（多值，OR 关系）
    .taskPriority(500)                       // 优先级
    .taskTitle("审批")                       // 标题
    .taskTag("urgent")                       // 标签
    .taskComment("备注")                     // 备注
    .taskExtension("ext_data")               // 扩展字段

    // === 流程定义 ===
    .processDefinitionType("approval")       // 流程定义类型
    .processDefinitionActivityId("userTask1") // 流程定义活动 ID

    // === 时间范围 ===
    .completeTimeAfter(startDate)            // 完成时间 >= startDate
    .completeTimeBefore(endDate)             // 完成时间 < endDate

    // === 分页与租户 ===
    .tenantId("tenant001")                   // 租户 ID
    .pageOffset(0).pageSize(10)              // 分页

    // === 排序（可多字段） ===
    .orderByPriority().desc()
    .orderByCreateTime().asc()

    // === 终端操作 ===
    .list();
```

### 2.2 ProcessInstanceQuery 完整过滤条件

```java
smartEngine.createProcessQuery()
    .processInstanceId("proc001")
    .processInstanceIdIn(idList)
    .startedBy("user001")                    // 发起人
    .processStatus("running")                // 流程状态
    .processDefinitionType("approval")
    .processDefinitionIdAndVersion("myProcess:1")
    .parentInstanceId("parentProc001")       // 父流程实例
    .bizUniqueId("ORDER-2026-001")           // 业务唯一键
    .startedAfter(startDate)                 // 发起时间 >= startDate
    .startedBefore(endDate)                  // 发起时间 < endDate
    .completedAfter(completeStart)           // 完成时间 >= completeStart
    .completedBefore(completeEnd)            // 完成时间 < completeEnd
    .orderByStartTime().desc()
    .orderByCompleteTime().asc()
    .listPage(0, 20);
```

### 2.3 SupervisionQuery 完整过滤条件

```java
smartEngine.createSupervisionQuery()
    .supervisionId("sup001")
    .supervisorUserId("supervisor001")       // 督办人
    .taskInstanceId("task001")               // 关联任务
    .taskInstanceIdIn(taskIdList)
    .processInstanceId("proc001")
    .processInstanceIdIn(procIdList)
    .supervisionType("urge")                 // 督办类型
    .supervisionStatus("active")             // 督办状态
    .supervisionTimeAfter(startDate)
    .supervisionTimeBefore(endDate)
    .orderByCreateTime().desc()
    .listPage(0, 10);
```

### 2.4 NotificationQuery 完整过滤条件

```java
smartEngine.createNotificationQuery()
    .notificationId("notif001")
    .senderUserId("sender001")               // 发送人
    .receiverUserId("receiver001")           // 接收人
    .taskInstanceId("task001")
    .taskInstanceIdIn(taskIdList)
    .processInstanceId("proc001")
    .processInstanceIdIn(procIdList)
    .notificationType("task_assign")         // 通知类型
    .readStatus("unread")                    // 已读状态
    .notificationTimeAfter(startDate)
    .notificationTimeBefore(endDate)
    .orderByCreateTime().desc()
    .orderByReadTime().asc()
    .listPage(0, 20);
```

---

## 3. 条件过滤（动态条件）

借鉴 MyBatis Plus 的 `eq(condition, value)` 模式，每个过滤方法都提供 `(boolean condition, value)` 重载。当 `condition = false` 时，该条件不会被添加到查询中。

### 3.1 使用场景

在实际业务中，查询条件通常来自前端表单，有些字段可能为空：

```java
// ❌ 传统方式：大量 if-else
TaskQuery query = smartEngine.createTaskQuery();
if (userId != null) {
    query.taskAssignee(userId);
}
if (status != null) {
    query.taskStatus(status);
}
if (processType != null) {
    query.processDefinitionType(processType);
}
List<TaskInstance> tasks = query.list();

// ✅ 条件过滤：一行搞定
List<TaskInstance> tasks = smartEngine.createTaskQuery()
    .taskAssignee(userId != null, userId)
    .taskStatus(status != null, status)
    .processDefinitionType(processType != null, processType)
    .orderByCreateTime().desc()
    .listPage(0, 10);
```

### 3.2 可用的条件重载方法

**Query（基础）：**
- `tenantId(boolean condition, String tenantId)`
- `pageOffset(boolean condition, int offset)`
- `pageSize(boolean condition, int size)`

**ProcessBoundQuery（TaskQuery / SupervisionQuery / NotificationQuery 共享）：**
- `processInstanceId(boolean condition, String processInstanceId)`

**TaskQuery：**
- `taskAssignee(boolean condition, String claimUserId)`
- `taskStatus(boolean condition, String status)`
- `processDefinitionType(boolean condition, String type)`
- `taskTag(boolean condition, String tag)`
- `taskTitle(boolean condition, String title)`

**ProcessInstanceQuery：**
- `processInstanceId(boolean condition, String processInstanceId)`
- `startedBy(boolean condition, String startUserId)`
- `processStatus(boolean condition, String status)`
- `processDefinitionType(boolean condition, String type)`
- `bizUniqueId(boolean condition, String bizUniqueId)`

**SupervisionQuery：**
- `supervisorUserId(boolean condition, String supervisorUserId)`
- `supervisionType(boolean condition, String type)`
- `supervisionStatus(boolean condition, String status)`

**NotificationQuery：**
- `receiverUserId(boolean condition, String receiverUserId)`
- `notificationType(boolean condition, String type)`
- `readStatus(boolean condition, String readStatus)`

---

## 4. 多值条件

### 4.1 taskStatusIn

同时查询多种状态的任务：

```java
// 查询所有"待处理"和"处理中"的任务
List<TaskInstance> tasks = smartEngine.createTaskQuery()
    .taskStatusIn("pending", "running")
    .orderByCreateTime().desc()
    .list();

// 或使用 List
List<String> statuses = Arrays.asList("pending", "running", "suspended");
List<TaskInstance> tasks = smartEngine.createTaskQuery()
    .taskStatusIn(statuses)
    .list();
```

> **注意**：`taskStatusIn` 与 `taskStatus` 互斥。同时设置时，`taskStatusIn` 优先生效。

### 4.2 processInstanceIdIn

```java
// 批量查询多个流程实例的任务
List<String> processIds = Arrays.asList("proc001", "proc002", "proc003");
List<TaskInstance> tasks = smartEngine.createTaskQuery()
    .processInstanceIdIn(processIds)
    .list();
```

---

## 5. 排序

### 5.1 单字段排序

```java
// 按创建时间倒序
smartEngine.createTaskQuery()
    .orderByCreateTime().desc()
    .list();
```

### 5.2 多字段排序

排序条件按调用顺序生效（先调用的优先级更高）：

```java
// 先按优先级降序，再按创建时间升序
smartEngine.createTaskQuery()
    .orderByPriority().desc()
    .orderByCreateTime().asc()
    .list();
```

### 5.3 各查询类型支持的排序字段

| TaskQuery | ProcessInstanceQuery | SupervisionQuery | NotificationQuery |
|-----------|---------------------|-----------------|-------------------|
| `orderByTaskId()` | `orderByProcessInstanceId()` | `orderBySupervisionId()` | `orderByNotificationId()` |
| `orderByCreateTime()` | `orderByStartTime()` | `orderByCreateTime()` | `orderByCreateTime()` |
| `orderByModifyTime()` | `orderByModifyTime()` | `orderByModifyTime()` | `orderByModifyTime()` |
| `orderByClaimTime()` | `orderByCompleteTime()` | `orderByCloseTime()` | `orderByReadTime()` |
| `orderByCompleteTime()` | | | |
| `orderByPriority()` | | | |

---

## 6. 接口继承结构

```
Query<Q, T>                          ← 基础：分页、租户、终端操作
├── ProcessBoundQuery<Q, T>          ← 共享：processInstanceId、排序
│   ├── TaskQuery                    ← 任务：assignee、status、priority...
│   ├── SupervisionQuery             ← 督办：supervisor、type...
│   └── NotificationQuery            ← 通知：sender、receiver、readStatus...
└── ProcessInstanceQuery             ← 流程：startUser、bizUniqueId、parentInstance...
```

`ProcessBoundQuery` 是一个共享接口（借鉴 Flowable 的 `TaskInfoQuery` 设计），将 `processInstanceId`、`orderByCreateTime`、`orderByModifyTime`、`asc`、`desc` 等公共方法提取到父接口，减少重复。

---

## 7. 典型业务场景示例

### 7.1 待办任务列表页（带搜索）

```java
public PageResult<TaskInstance> getMyTodoList(String userId, String keyword,
                                               String processType, int page, int size) {
    TaskQuery query = smartEngine.createTaskQuery()
        .taskAssignee(userId)
        .taskStatus(TaskInstanceConstant.PENDING)
        .processDefinitionType(processType != null, processType)
        .taskTitle(keyword != null, keyword)
        .tenantId(getTenantId())
        .orderByPriority().desc()
        .orderByCreateTime().desc();

    long total = query.count();
    List<TaskInstance> list = query.listPage(page * size, size);

    return new PageResult<>(list, total);
}
```

### 7.2 我发起的流程列表

```java
public List<ProcessInstance> getMyStartedProcesses(String userId, String status) {
    return smartEngine.createProcessQuery()
        .startedBy(userId)
        .processStatus(status != null, status)
        .tenantId(getTenantId())
        .orderByStartTime().desc()
        .listPage(0, 20);
}
```

### 7.3 未读通知数 + 通知列表

```java
// 统计未读通知数
long unreadCount = smartEngine.createNotificationQuery()
    .receiverUserId(currentUserId)
    .readStatus("unread")
    .tenantId(getTenantId())
    .count();

// 获取通知列表
List<NotificationInstance> notifications = smartEngine.createNotificationQuery()
    .receiverUserId(currentUserId)
    .readStatus(filterReadStatus != null, filterReadStatus)
    .tenantId(getTenantId())
    .orderByCreateTime().desc()
    .listPage(0, 20);
```

### 7.4 查询某流程的督办记录

```java
List<SupervisionInstance> supervisions = smartEngine.createSupervisionQuery()
    .processInstanceId("proc001")
    .supervisionStatus("active")
    .tenantId(getTenantId())
    .orderByCreateTime().desc()
    .list();
```

---

## 8. 与旧版 QueryService API 的关系

### 8.1 共存策略

Fluent Query API 是旧版 `XxxQueryService` + `XxxQueryParam` 的**上层封装**，两套 API 可以共存使用。

### 8.2 迁移映射

| 旧 API | Fluent API 等价写法 |
|--------|-------------------|
| `taskQueryService.findList(param)` | `smartEngine.createTaskQuery()...list()` |
| `taskQueryService.count(param)` | `smartEngine.createTaskQuery()...count()` |
| `processQueryService.findList(param)` | `smartEngine.createProcessQuery()...list()` |
| `processQueryService.findById(id)` | `smartEngine.createProcessQuery().processInstanceId(id).singleResult()` |
| `taskQueryService.findOne(taskId)` | `smartEngine.createTaskQuery().taskInstanceId(taskId).singleResult()` |
| `taskQueryService.findAllPendingTaskList(procId)` | `smartEngine.createTaskQuery().processInstanceId(procId).taskStatus("pending").list()` |
| `supervisionQueryService.findSupervisionList(param)` | `smartEngine.createSupervisionQuery()...list()` |
| `notificationQueryService.findNotificationList(param)` | `smartEngine.createNotificationQuery()...list()` |

### 8.3 尚未覆盖的旧 API

以下旧 API 目前没有直接的 Fluent 等价物，建议继续使用旧 API：

| 旧 API | 原因 |
|--------|------|
| `ExecutionQueryService` | 执行实例查询较底层，使用频率低 |
| `ActivityQueryService` | 活动实例查询较底层 |
| `DeploymentQueryService` | 部署管理查询 |
| `RepositoryQueryService` | 缓存查询，不走数据库 |
| `TaskAssigneeQueryService` | 任务分配人查询（含 group 逻辑） |
| `VariableQueryService` | 变量查询 |
| `findPendingTaskList(PendingTaskQueryParam)` | 含 assignee group 关联查询，Fluent API 暂不支持 |
| `findTaskListByAssignee(TaskInstanceQueryByAssigneeParam)` | 同上 |
| `findCompletedTaskList(CompletedTaskQueryParam)` | 含 participantUserId 关联查询 |

---

## 9. 与 Activiti/Flowable 的对比

### 9.1 设计差异

| 维度 | Activiti/Flowable | SmartEngine |
|------|-------------------|-------------|
| 运行/历史分离 | 两套表 + 两套 Query | 同表通过 status 区分 |
| 查询类型数量 | 10+ 种 | 4 种（Task/Process/Supervision/Notification） |
| 条件动态应用 | 无（需手动 if-else） | ✅ `(boolean condition, value)` 重载 |
| 督办/通知查询 | 无 | ✅ 独有 |
| OR 条件 | `or()/endOr()` | 暂不支持 |
| 变量值过滤 | `variableValueEquals(name, value)` | 暂不支持 |

### 9.2 覆盖度

在轻量引擎定位下，SmartEngine 的 Fluent API 覆盖了 Activiti/Flowable 约 **60-65%** 的实用过滤方法。主要缺失项（后续迭代补充）：

| 缺失方法 | 优先级 | 场景 |
|---------|:------:|------|
| `taskTitleLike` / `taskNameLike` | P0 | 模糊搜索 |
| `taskCreatedBefore/After` | P0 | 按创建时间过滤 |
| `taskUnassigned()` | P1 | 待认领任务池 |
| `processDefinitionTypeIn(List)` | P1 | 多流程类型过滤 |
| `or()/endOr()` | P2 | 复杂条件组合 |
| `variableValueEquals()` | P2 | 变量值过滤 |
