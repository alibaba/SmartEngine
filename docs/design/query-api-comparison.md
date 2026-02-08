# Smart-Engine vs Activiti vs Flowable: Query API 完整性对比

> 调研日期: 2026-02-08
> 对比版本: Activiti 5.22.0 / 6.0, Flowable 7.x, SmartEngine dev 分支

---

## 一、总体架构差异

| 维度 | Activiti / Flowable | SmartEngine |
|------|-------------------|-------------|
| 查询风格 | 完全链式 fluent API（`createXxxQuery().xxx().list()`） | **双轨制**：旧式 QueryParam DTO + 新版 fluent Query API |
| 历史数据 | 独立 History 表 + 独立 HistoricXxxQuery | 运行/历史混存同一表，通过 status 区分 |
| 变量查询 | 深度集成到每个 Query（可按变量值过滤 Task/Process） | 独立 VariableQueryService，不支持按变量值过滤其他实体 |
| 逻辑运算 | 支持 `or()` / `endOr()` 构建 OR 条件 | 不支持 |
| Native SQL | 提供 NativeXxxQuery 允许直接 SQL | 不提供 |
| BPMN 概念 | 完整 BPMN 2.0 支持（信号/消息事件、子流程、Job等） | 轻量 BPMN 子集（无信号/消息事件订阅、无 Job 机制） |

---

## 二、TaskQuery 对比

### 2.1 Activiti/Flowable TaskInfoQuery + TaskQuery 方法汇总

Activiti 和 Flowable 的 TaskQuery 都继承自 `TaskInfoQuery`，后者定义了绝大部分过滤方法。两者功能基本一致，Flowable 在 7.x 版本增加了少量方法。

#### 过滤条件（约 80+ 方法）

| 方法 | Activiti | Flowable | SmartEngine | 备注 |
|------|:--------:|:--------:|:-----------:|------|
| **基础标识** | | | | |
| `taskId(String)` | Y | Y | Y (`taskInstanceId`) | 名称不同 |
| `taskName(String)` | Y | Y | N | **缺失** |
| `taskNameLike(String)` | Y | Y | N | **缺失** |
| `taskNameIn(List)` | Y | Y | N | **缺失** |
| `taskNameLikeIgnoreCase(String)` | Y | Y | N | **缺失** |
| `taskDescription(String)` | Y | Y | N | **缺失** |
| `taskDescriptionLike(String)` | Y | Y | N | **缺失** |
| **分配/候选人** | | | | |
| `taskAssignee(String)` | Y | Y | Y | |
| `taskAssigneeLike(String)` | Y | Y | N | **缺失** |
| `taskOwner(String)` | Y | Y | N | SmartEngine 无 owner 概念 |
| `taskOwnerLike(String)` | Y | Y | N | 不适用 |
| `taskCandidateUser(String)` | Y | Y | N | 通过 TaskAssigneeQueryService 间接实现 |
| `taskCandidateGroup(String)` | Y | Y | N | 同上 |
| `taskCandidateGroupIn(List)` | Y | Y | N | 同上 |
| `taskCandidateOrAssigned(String)` | Y | Y | N | **缺失**（常用场景） |
| `taskInvolvedUser(String)` | Y | Y | N | **缺失** |
| `taskUnassigned()` | Y | Y | N | **缺失** |
| **优先级** | | | | |
| `taskPriority(Integer)` | Y | Y | Y | |
| `taskMinPriority(Integer)` | Y | Y | N | **缺失**（范围查询） |
| `taskMaxPriority(Integer)` | Y | Y | N | **缺失**（范围查询） |
| **分类/定义** | | | | |
| `taskCategory(String)` | Y | Y | N | 可用 tag 替代 |
| `taskDefinitionKey(String)` | Y | Y | Y (`processDefinitionActivityId`) | 对应关系 |
| `taskDefinitionKeyLike(String)` | Y | Y | N | **缺失** |
| **状态** | | | | |
| `active()` | Y | Y | N | SmartEngine 用 taskStatus 过滤 |
| `suspended()` | Y | Y | N | SmartEngine 无挂起概念 |
| `taskStatus(String)` | N | N | Y | SmartEngine 独有 |
| `taskStatusIn(List)` | N | N | Y | SmartEngine 独有 |
| **委托** | | | | |
| `taskDelegationState(DelegationState)` | Y | Y | N | 不适用（无委托状态机） |
| **时间** | | | | |
| `taskCreatedOn(Date)` | Y | Y | N | **缺失**（精确创建时间） |
| `taskCreatedBefore(Date)` | Y | Y | N | **缺失** |
| `taskCreatedAfter(Date)` | Y | Y | N | **缺失** |
| `taskDueDate(Date)` | Y | Y | N | **缺失**（无到期日） |
| `taskDueBefore(Date)` | Y | Y | N | **缺失** |
| `taskDueAfter(Date)` | Y | Y | N | **缺失** |
| `withoutTaskDueDate()` | Y | Y | N | 不适用 |
| `completeTimeAfter(Date)` | N | N | Y | SmartEngine 独有 |
| `completeTimeBefore(Date)` | N | N | Y | SmartEngine 独有 |
| **流程关联** | | | | |
| `processInstanceId(String)` | Y | Y | Y | |
| `processInstanceIdIn(List)` | Y | Y | Y | |
| `processDefinitionId(String)` | Y | Y | N | 可用 processDefinitionType 替代 |
| `processDefinitionKey(String)` | Y | Y | Y (`processDefinitionType`) | 概念对应 |
| `processDefinitionKeyIn(List)` | Y | Y | N | **缺失** |
| `processDefinitionKeyLike(String)` | Y | Y | N | **缺失** |
| `processDefinitionName(String)` | Y | Y | N | **缺失** |
| `processDefinitionNameLike(String)` | Y | Y | N | **缺失** |
| `processCategoryIn(List)` | Y | Y | N | 不适用 |
| `processCategoryNotIn(List)` | Y | Y | N | 不适用 |
| `processInstanceBusinessKey(String)` | Y | Y | N | **缺失**（可映射为 bizUniqueId） |
| `processInstanceBusinessKeyLike(String)` | Y | Y | N | **缺失** |
| `executionId(String)` | Y | Y | N | **缺失** |
| `deploymentId(String)` | Y | Y | N | 不适用 |
| `deploymentIdIn(List)` | Y | Y | N | 不适用 |
| **租户** | | | | |
| `taskTenantId(String)` | Y | Y | Y (`tenantId`) | |
| `taskTenantIdLike(String)` | Y | Y | N | **缺失** |
| `taskWithoutTenantId()` | Y | Y | N | **缺失** |
| **变量** | | | | |
| `taskVariableValueEquals(name, value)` | Y | Y | N | **重要缺失** |
| `taskVariableValueNotEquals(...)` | Y | Y | N | **重要缺失** |
| `taskVariableValue{Gt/Gte/Lt/Lte/Like}(...)` | Y | Y | N | **重要缺失** |
| `processVariableValueEquals(name, value)` | Y | Y | N | **重要缺失** |
| `processVariableValue{NotEquals/Gt/Gte/Lt/Lte/Like}(...)` | Y | Y | N | **重要缺失** |
| **SmartEngine 独有** | | | | |
| `taskTag(String)` | N | N | Y | 标签过滤 |
| `taskExtension(String)` | N | N | Y | 扩展字段 |
| `taskComment(String)` | N | N | Y | 备注过滤 |
| `taskTitle(String)` | N | N | Y | 标题过滤 |
| `activityInstanceId(String)` | N | N | Y | 节点实例过滤 |
| `processDefinitionType(bool, String)` | N | N | Y | 条件过滤 |
| **结果增强** | | | | |
| `includeProcessVariables()` | Y | Y | N | **缺失** |
| `includeTaskLocalVariables()` | Y | Y | N | **缺失** |
| `excludeSubtasks()` | Y | Y | N | 不适用 |
| **逻辑运算** | | | | |
| `or()` / `endOr()` | Y | Y | N | **缺失** |
| **本地化** | | | | |
| `locale(String)` | Y | Y | N | 不适用 |
| `withLocalizationFallback()` | Y | Y | N | 不适用 |

#### 排序方法

| 方法 | Activiti | Flowable | SmartEngine |
|------|:--------:|:--------:|:-----------:|
| `orderByTaskId()` | Y | Y | Y |
| `orderByTaskName()` | Y | Y | N |
| `orderByTaskDescription()` | Y | Y | N |
| `orderByTaskAssignee()` | Y | Y | N |
| `orderByTaskOwner()` | Y | Y | N |
| `orderByTaskCreateTime()` | Y | Y | Y (`orderByCreateTime`) |
| `orderByTaskDueDate()` | Y | Y | N |
| `orderByDueDateNullsFirst/Last()` | Y | Y | N |
| `orderByTaskDefinitionKey()` | Y | Y | N |
| `orderByTaskPriority()` | Y | Y | Y (`orderByPriority`) |
| `orderByExecutionId()` | Y | Y | N |
| `orderByProcessInstanceId()` | Y | Y | N |
| `orderByProcessDefinitionId()` | Y | Y | N |
| `orderByTenantId()` | Y | Y | N |
| `orderByClaimTime()` | N | N | Y |
| `orderByCompleteTime()` | N | N | Y |
| `orderByModifyTime()` | N | N | Y |

### 2.2 TaskQuery 关键缺失总结

**高优先级缺失**（影响常见业务场景）：
1. `taskName` / `taskNameLike` - 按任务名称搜索
2. `taskCandidateOrAssigned` - 查询用户待办（含候选）
3. `taskCreatedBefore/After` - 按创建时间范围过滤
4. `taskDueDate/Before/After` - 到期日相关
5. 变量过滤（`taskVariableValueEquals` 等） - 按业务变量过滤任务
6. `processInstanceBusinessKey` - 按业务键过滤

**中优先级缺失**：
7. `taskMinPriority/taskMaxPriority` - 优先级范围
8. `taskDescription/Like` - 描述搜索
9. `taskUnassigned` - 未分配任务
10. `taskDefinitionKeyLike` - 模糊匹配定义键

**不适用方法**（SmartEngine 设计上不需要）：
- `taskDelegationState` - 无委托状态
- `suspended/active` - SmartEngine 用 status 字段替代
- `excludeSubtasks` - 无子任务
- `locale` - 无内置国际化
- `deploymentId` - 部署模型不同

---

## 三、ProcessInstanceQuery 对比

### 3.1 Activiti/Flowable ProcessInstanceQuery 方法

| 方法 | Activiti | Flowable | SmartEngine | 备注 |
|------|:--------:|:--------:|:-----------:|------|
| **基础标识** | | | | |
| `processInstanceId(String)` | Y | Y | Y | |
| `processInstanceIds(Set)` | Y | Y | Y (`processInstanceIdIn`) | |
| `processInstanceBusinessKey(String)` | Y | Y | Y (`bizUniqueId`) | 概念对应 |
| `processInstanceBusinessKey(String, String)` | Y | Y | N | 带定义键 |
| `processInstanceName(String)` | Y | Y | N | **缺失** |
| `processInstanceNameLike(String)` | Y | Y | N | **缺失** |
| `processInstanceNameLikeIgnoreCase(String)` | Y | Y | N | **缺失** |
| **流程定义** | | | | |
| `processDefinitionId(String)` | Y | Y | N | 可用 processDefinitionIdAndVersion 替代 |
| `processDefinitionIds(Set)` | Y | Y | N | **缺失** |
| `processDefinitionKey(String)` | Y | Y | Y (`processDefinitionType`) | |
| `processDefinitionKeys(Set)` | Y | Y | N | **缺失** |
| `processDefinitionCategory(String)` | Y | Y | N | 不适用 |
| `processDefinitionName(String)` | Y | Y | N | **缺失** |
| `processDefinitionVersion(Integer)` | Y | Y | N | 可部分用 processDefinitionIdAndVersion |
| **状态** | | | | |
| `active()` | Y | Y | Y (`processStatus`) | |
| `suspended()` | Y | Y | N | SmartEngine 无挂起 |
| `withJobException()` | Y | Y | N | 不适用 |
| `processStatus(String)` | N | N | Y | SmartEngine 独有 |
| **关系** | | | | |
| `superProcessInstanceId(String)` | Y | Y | Y (`parentInstanceId`) | |
| `subProcessInstanceId(String)` | Y | Y | N | **缺失**（反向查询） |
| `excludeSubprocesses(boolean)` | Y | Y | N | **缺失** |
| `involvedUser(String)` | Y | Y | N | **重要缺失** |
| **部署/租户** | | | | |
| `deploymentId(String)` | Y | Y | N | 不适用 |
| `deploymentIdIn(List)` | Y | Y | N | 不适用 |
| `processInstanceTenantId(String)` | Y | Y | Y (`tenantId`) | |
| `processInstanceTenantIdLike(String)` | Y | Y | N | **缺失** |
| `processInstanceWithoutTenantId()` | Y | Y | N | **缺失** |
| **时间** | | | | |
| `startedBefore(Date)` | Y* | Y* | Y | *HistoricProcessInstanceQuery |
| `startedAfter(Date)` | Y* | Y* | Y | *HistoricProcessInstanceQuery |
| `completedBefore(Date)` | N | N | Y | SmartEngine 独有 |
| `completedAfter(Date)` | N | N | Y | SmartEngine 独有 |
| `startedBy(String)` | Y* | Y* | Y | |
| **变量** | | | | |
| `variableValueEquals(name, value)` | Y | Y | N | **重要缺失** |
| `variableValueNotEquals(...)` | Y | Y | N | **重要缺失** |
| `variableValue{Gt/Gte/Lt/Lte/Like}(...)` | Y | Y | N | **重要缺失** |
| **SmartEngine 独有** | | | | |
| `processDefinitionIdAndVersion(String)` | N | N | Y | 精确版本过滤 |
| `processDefinitionType(bool, String)` | N | N | Y | 条件过滤 |
| `bizUniqueId(bool, String)` | N | N | Y | 条件过滤 |
| **结果增强** | | | | |
| `includeProcessVariables()` | Y | Y | N | **缺失** |
| `limitProcessInstanceVariables(Integer)` | Y | Y | N | **缺失** |
| **逻辑运算** | | | | |
| `or()` / `endOr()` | Y | Y | N | **缺失** |

#### 排序方法

| 方法 | Activiti | Flowable | SmartEngine |
|------|:--------:|:--------:|:-----------:|
| `orderByProcessInstanceId()` | Y | Y | Y |
| `orderByProcessDefinitionKey()` | Y | Y | N |
| `orderByProcessDefinitionId()` | Y | Y | N |
| `orderByTenantId()` | Y | Y | N |
| `orderByStartTime()` | Y* | Y* | Y |
| `orderByModifyTime()` | N | N | Y |
| `orderByCompleteTime()` | N | N | Y |
| `orderByEndTime()` | Y* | Y* | N |
| `orderByDuration()` | Y* | Y* | N |
| `orderByBusinessKey()` | Y* | Y* | N |

> 标注 `Y*` 表示该方法在 HistoricProcessInstanceQuery 中提供，而非运行时 ProcessInstanceQuery。

### 3.2 关键缺失

**高优先级**：
1. `involvedUser` - 查询用户参与的流程（发起 + 处理过的），**审批场景核心需求**
2. 变量过滤 - 按业务变量值过滤流程实例
3. `processInstanceName/Like` - 按流程实例名称搜索

**中优先级**：
4. `subProcessInstanceId` - 从子流程反查父流程
5. `excludeSubprocesses` - 排除子流程
6. `processDefinitionKeys(Set)` - 多定义类型过滤

---

## 四、HistoricTaskInstanceQuery / HistoricProcessInstanceQuery 对比

### 4.1 Activiti/Flowable 的历史查询能力

Activiti/Flowable 将运行数据和历史数据分离存储，提供了专门的 Historic 查询：

#### HistoricTaskInstanceQuery 独有方法

| 方法 | 说明 | SmartEngine 等价 |
|------|------|-----------------|
| `finished()` | 仅查已完成 | `taskStatus("completed")` |
| `unfinished()` | 仅查未完成 | `taskStatus("pending")` |
| `processFinished()` | 任务所属流程已结束 | 需 join 查询，**缺失** |
| `processUnfinished()` | 任务所属流程未结束 | 需 join 查询，**缺失** |
| `taskDeleteReason(String)` | 按删除原因过滤 | 不适用 |
| `taskDeleteReasonLike(String)` | 模糊匹配删除原因 | 不适用 |
| `taskParentTaskId(String)` | 查子任务 | 不适用 |
| `taskCompletedOn(Date)` | 精确完成日期 | **缺失** |
| `taskCompletedBefore(Date)` | 完成时间之前 | Y (`completeTimeBefore`) |
| `taskCompletedAfter(Date)` | 完成时间之后 | Y (`completeTimeAfter`) |
| `orderByHistoricTaskInstanceDuration()` | 按执行时长排序 | **缺失** |
| `orderByHistoricTaskInstanceEndTime()` | 按结束时间排序 | Y (`orderByCompleteTime`) |
| `orderByHistoricTaskInstanceStartTime()` | 按开始时间排序 | Y (`orderByCreateTime`) |
| `orderByDeleteReason()` | 按删除原因排序 | 不适用 |

#### HistoricProcessInstanceQuery 独有方法

| 方法 | 说明 | SmartEngine 等价 |
|------|------|-----------------|
| `finished()` | 仅查已结束流程 | `processStatus("completed")` |
| `unfinished()` | 仅查运行中流程 | `processStatus("running")` |
| `deleted()` | 已删除的流程 | **缺失** |
| `notDeleted()` | 未删除的流程 | **缺失** |
| `finishedBefore(Date)` | 结束时间之前 | Y (`completedBefore`) |
| `finishedAfter(Date)` | 结束时间之后 | Y (`completedAfter`) |
| `orderByProcessInstanceDuration()` | 按流程时长排序 | **缺失** |
| `orderByProcessInstanceEndTime()` | 按结束时间排序 | Y (`orderByCompleteTime`) |
| `orderByProcessInstanceBusinessKey()` | 按业务键排序 | **缺失** |

### 4.2 SmartEngine 的优势

SmartEngine 将运行和历史数据存在同一表中，通过 status 字段区分。这意味着：
- **优势**：不需要维护两套查询接口，一个 TaskQuery / ProcessInstanceQuery 即可覆盖运行+历史场景
- **劣势**：无法做到 `processFinished()` / `processUnfinished()` 这类跨表关联过滤
- **劣势**：大数据量时历史和运行数据混存可能影响性能

---

## 五、其他查询类型对比

### 5.1 DeploymentQuery

| 方法 | Activiti/Flowable | SmartEngine |
|------|:-----------------:|:-----------:|
| `deploymentId(String)` | Y | Y (`findById`) |
| `deploymentName(String)` | Y | Y (`processDefinitionName`) |
| `deploymentNameLike(String)` | Y | Y (`processDefinitionNameLike`) |
| `deploymentCategory(String)` | Y | N |
| `deploymentCategoryNotEquals(String)` | Y | N |
| `deploymentTenantId(String)` | Y | Y (`tenantId`) |
| `deploymentTenantIdLike(String)` | Y | N |
| `deploymentWithoutTenantId()` | Y | N |
| `processDefinitionKey(String)` | Y | Y (`processDefinitionCode`) |
| `processDefinitionKeyLike(String)` | Y | N |
| `orderByDeploymentId()` | Y | N |
| `orderByDeploymentName()` | Y | N |
| `orderByDeploymentTime()` | Y | N |
| `orderByTenantId()` | Y | N |
| **SmartEngine 独有** | | |
| `processDefinitionType` | N | Y |
| `processDefinitionVersion` | N | Y |
| `processDefinitionDescLike` | N | Y |
| `deploymentUserId` | N | Y |
| `deploymentStatus` | N | Y |
| `logicStatus` | N | Y |

**评估**：SmartEngine 的 DeploymentQueryService 功能比较完整，独有的 `deploymentStatus`、`logicStatus`、`deploymentUserId` 等字段是业务增强。主要缺失是没有 fluent API（仍使用 QueryParam DTO），没有 orderBy 能力。

### 5.2 ExecutionQuery

| 方法 | Activiti/Flowable | SmartEngine |
|------|:-----------------:|:-----------:|
| `executionId(String)` | Y | N |
| `processInstanceId(String)` | Y | Y (`findActiveExecutionList`) |
| `processDefinitionKey(String)` | Y | N |
| `processDefinitionId(String)` | Y | N |
| `processInstanceBusinessKey(String)` | Y | N |
| `activityId(String)` | Y | N |
| `parentId(String)` | Y | N |
| `executionTenantId(String)` | Y | Y (重载方法) |
| 变量过滤（12+ 方法） | Y | N |
| 信号/消息事件订阅 | Y | N |
| fluent API | Y | **N** |

**评估**：SmartEngine 的 ExecutionQueryService 非常简单，仅支持按 processInstanceId 查询活跃/全部执行实例。这对于轻量引擎是足够的，因为 Execution 主要是内部概念，业务代码较少直接查询。

### 5.3 ActivityQueryService (vs HistoricActivityInstanceQuery)

| 方法 | Activiti/Flowable | SmartEngine |
|------|:-----------------:|:-----------:|
| `activityInstanceId(String)` | Y | N |
| `processInstanceId(String)` | Y | Y (`findAll`) |
| `processDefinitionId(String)` | Y | N |
| `executionId(String)` | Y | N |
| `activityId(String)` | Y | N |
| `activityName(String)` | Y | N |
| `activityType(String)` | Y | N |
| `taskAssignee(String)` | Y | N |
| `finished()` / `unfinished()` | Y | N |
| `activityTenantId(String)` | Y | Y (重载方法) |
| orderBy（11种排序） | Y | N（默认时间降序） |
| fluent API | Y | **N** |

**评估**：SmartEngine 的 ActivityQueryService 仅提供 `findAll(processInstanceId)` 一个方法（默认时间降序），用于显示流程操作轨迹。缺失所有过滤和排序能力。对于"查看流程审批轨迹"场景足够，但无法按节点类型、处理人等维度筛选。

### 5.4 SmartEngine 不存在的查询类型

| 查询类型 | Activiti/Flowable 功能 | SmartEngine 是否需要 |
|---------|----------------------|---------------------|
| **JobQuery** | 查询定时/异步任务，过滤到期、失败、重试等 | **不适用** - SmartEngine 无内置 Job 机制 |
| **ProcessDefinitionQuery** | 查询流程定义（按key/name/version/category） | 部分覆盖（DeploymentQueryService + RepositoryQueryService） |
| **HistoricDetailQuery** | 查询历史变量变更、表单属性变更 | **不适用** - 无变量变更审计 |
| **HistoricVariableInstanceQuery** | 查询历史变量值，按名称/值过滤 | **部分缺失** - VariableQueryService 仅按 processInstanceId 查询 |
| **EventSubscriptionQuery** | 查询信号/消息事件订阅 | **不适用** - 无事件订阅机制 |
| **ModelQuery** | 查询设计器模型 | **不适用** |
| **UserQuery / GroupQuery** | 查询内置用户/组 | **不适用** - 用户管理外置 |
| **NativeXxxQuery** | 直接 SQL 查询 | **不适用** - 可直接使用 MyBatis |

### 5.5 SmartEngine 独有查询类型

| 查询类型 | 功能 | Activiti/Flowable 等价 |
|---------|------|----------------------|
| **SupervisionQuery** | 督办记录查询（督办人、任务、状态、时间） | 无（需自行扩展） |
| **NotificationQuery** | 知会抄送查询（收发人、读取状态、类型） | 无（需自行扩展） |
| **TaskAssigneeQueryService** | 任务委托人查询 | 内置在 TaskQuery 的 candidate 系列方法中 |
| **CompletedTaskQueryParam** | 已办任务专用查询 | HistoricTaskInstanceQuery.finished() |
| **CompletedProcessQueryParam** | 办结流程专用查询 | HistoricProcessInstanceQuery.finished() |

---

## 六、总结：实用性差距分析

### 6.1 对轻量工作流引擎有实际意义的缺失（建议补充）

| 优先级 | 缺失能力 | 场景 | 建议 |
|:------:|---------|------|------|
| **P0** | `taskName/Like` | 任务列表搜索 | 在 TaskQuery 中添加 `taskTitle` 的 like 匹配模式（当前仅精确匹配） |
| **P0** | `taskCreatedBefore/After` | 按创建时间筛选待办 | 在 TaskQuery 中添加 |
| **P0** | `involvedUser` | "我参与的流程" | 需要跨 process + task 表查询 |
| **P1** | `taskCandidateOrAssigned` | 统一待办查询 | 当前需要两次查询合并 |
| **P1** | `processInstanceName/Like` | 流程列表搜索 | ProcessInstance 模型中需先添加 name 字段 |
| **P1** | `taskNameLike` / `taskTitleLike` | 模糊搜索 | 当前 taskTitle 仅精确匹配 |
| **P1** | `taskUnassigned` | 待认领任务池 | 简单条件：`claim_user_id IS NULL` |
| **P2** | `processDefinitionKeys(Set)` | 多类型过滤 | ProcessInstanceQuery 中添加 |
| **P2** | `orderByDuration` | 效率分析 | 计算字段，需要 endTime - startTime |
| **P2** | `taskDueDate/Before/After` | 到期提醒 | 需先在 TaskInstance 模型中添加 dueDate 字段 |
| **P3** | 变量值过滤 | 按业务数据过滤 | 需要 join variable 表，性能考量较大 |
| **P3** | `or()` / `endOr()` | 复杂条件 | 实现复杂度高，可暂缓 |

### 6.2 SmartEngine 的差异化优势

1. **条件过滤 API** (`condition, value`)：所有过滤方法都有 `(boolean condition, String value)` 重载，方便前端动态条件构建，Activiti/Flowable 没有此设计
2. **督办/知会** (Supervision/Notification)：独立的业务域查询，Activiti/Flowable 完全没有
3. **标签/扩展/备注** (tag/extension/comment)：TaskQuery 的业务增强字段
4. **运行+历史统一查询**：一个接口覆盖运行和历史数据，减少 API 表面积
5. **双轨查询**：同时支持 QueryParam DTO（适合 MyBatis 映射）和 fluent API（适合编程使用）

### 6.3 改进路线图建议

**第一阶段（核心补齐）**：
- TaskQuery: 添加 `taskTitleLike`, `taskCreatedBefore/After`, `taskUnassigned`
- ProcessInstanceQuery: 添加 `involvedUser`
- 在现有 DeploymentQueryService 上层封装 fluent DeploymentQuery

**第二阶段（增强体验）**：
- TaskQuery: 添加 `taskCandidateOrAssigned`, `taskMinPriority/MaxPriority`
- ProcessInstanceQuery: 添加 `processDefinitionTypeIn(List)`, `excludeSubprocesses`
- ActivityQuery: 添加 fluent API + `activityType`, `taskAssignee` 过滤

**第三阶段（高级特性）**：
- 变量值过滤（需评估性能影响）
- `or()` / `endOr()` 逻辑运算
- `includeProcessVariables()` 结果增强
- `orderByDuration` 计算排序

---

## 参考资料

- [Activiti TaskInfoQuery Javadoc](https://www.activiti.org/javadocs/org/activiti/engine/task/TaskInfoQuery.html)
- [Activiti ProcessInstanceQuery Javadoc](https://www.activiti.org/javadocs/org/activiti/engine/runtime/ProcessInstanceQuery.html)
- [Activiti HistoricTaskInstanceQuery Javadoc](https://www.activiti.org/javadocs/org/activiti/engine/history/HistoricTaskInstanceQuery.html)
- [Activiti HistoricProcessInstanceQuery Javadoc](https://www.activiti.org/javadocs/org/activiti/engine/history/HistoricProcessInstanceQuery.html)
- [Activiti HistoricActivityInstanceQuery Javadoc](https://www.activiti.org/javadocs/org/activiti/engine/history/HistoricActivityInstanceQuery.html)
- [Activiti ExecutionQuery Javadoc](https://www.activiti.org/javadocs/org/activiti/engine/runtime/ExecutionQuery.html)
- [Activiti DeploymentQuery Javadoc](https://www.activiti.org/javadocs/org/activiti/engine/repository/DeploymentQuery.html)
- [Flowable TaskQuery Javadoc](https://developer-docs.flowable.com/javadocs/flowable-oss-javadoc/3.16.0/org/flowable/task/api/TaskQuery.html)
- [Flowable JobQuery Javadoc](https://www.flowable.com/open-source/docs/javadocs-5/org/activiti/engine/runtime/JobQuery.html)
