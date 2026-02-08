# Query API Gap 分析与改进计划

## 1. Gap 总览

基于与 Activiti/Flowable 的对比分析和当前未 Deprecated 旧 API 的梳理，总共识别出 **4 类 Gap**：

| 编号 | Gap 类型 | 影响面 | 优先级 |
|------|---------|--------|--------|
| G1 | TaskQuery 缺少 Assignee 候选人查询（JOIN 模式） | 4 个旧 API 无法 Deprecated | P0 |
| G2 | TaskQuery / ProcessInstanceQuery 缺少多值/模糊/时间范围过滤 | CompletedTask/Process 旧 API 无法 Deprecated；与 Flowable 有差距 | P1 |
| G3 | 缺少 DeploymentQuery Fluent API | DeploymentQueryService 无法 Deprecated | P1 |
| G4 | 缺少高级查询能力（or/endOr、variableValueEquals） | 与 Activiti/Flowable 有差距但业务场景不常用 | P2 |

---

## 2. G1：TaskQuery Assignee 候选人查询（P0）

### 2.1 问题分析

当前 `TaskQuery.taskAssignee(userId)` 映射到 `claim_user_id` 字段，仅能查询**已认领**的任务。

但以下旧 API 使用了 `se_task_instance JOIN se_task_assignee_instance` 的模式查询**候选人待办**：

```
findPendingTaskList(PendingTaskQueryParam)      → assigneeUserId + assigneeGroupIdList (OR 逻辑)
countPendingTaskList(PendingTaskQueryParam)      → 同上 count 版本
findTaskListByAssignee(TaskInstanceQueryByAssigneeParam) → 同上 + 支持 status 过滤
countTaskListByAssignee(TaskInstanceQueryByAssigneeParam) → 同上 count 版本
```

核心 SQL 逻辑（`pending_task_assignee_choose_sql`）：
```sql
-- 当同时提供 userId 和 groupIdList 时，使用 OR 逻辑
WHERE (assignee.assignee_id = #{userId} AND assignee.assignee_type = 'user')
   OR (assignee.assignee_id IN (#{groupIds}) AND assignee.assignee_type = 'group')
```

### 2.2 改进方案

参照 Activiti/Flowable 的命名规范，在 `TaskQuery` 中新增**候选人查询方法**：

```java
// --- TaskQuery 新增方法 ---

// Filter by candidate user (via assignee table)
TaskQuery taskCandidateUser(String userId);

// Filter by candidate group (via assignee table)
TaskQuery taskCandidateGroup(String groupId);

// Filter by multiple candidate groups
TaskQuery taskCandidateGroupIn(List<String> groupIds);

// Combined: candidate user OR candidate groups (OR logic)
TaskQuery taskCandidateOrGroup(String userId, List<String> groupIds);
```

**实现要点**：
1. `TaskQueryImpl` 新增 `candidateUserId` + `candidateGroupIds` 字段
2. `buildQueryParam()` 检测到候选人字段时，构建 `TaskInstanceQueryByAssigneeParam` 而非 `TaskInstanceQueryParam`
3. 内部调用 Storage 的 `findTaskByAssignee` / `countTaskByAssignee` SQL 路径
4. `taskCandidateOrGroup(userId, groupIds)` 等价于同时设置 `candidateUserId` + `candidateGroupIds`

**完成后可 Deprecated**：
- `findPendingTaskList(PendingTaskQueryParam)` → `createTaskQuery().taskCandidateOrGroup(u, groups).taskStatus("pending").list()`
- `countPendingTaskList(PendingTaskQueryParam)` → 同上 `.count()`
- `findTaskListByAssignee(TaskInstanceQueryByAssigneeParam)` → `createTaskQuery().taskCandidateOrGroup(u, groups).taskStatus(s).list()`
- `countTaskListByAssignee(TaskInstanceQueryByAssigneeParam)` → 同上 `.count()`

---

## 3. G2：多值/模糊/时间范围过滤增强（P1）

### 3.1 TaskQuery 缺失字段

| 方法 | 用途 | 对标 Flowable |
|------|------|--------------|
| `processDefinitionTypeIn(List<String>)` | 多类型过滤 | `processDefinitionKeyIn()` |
| `taskTitleLike(String)` | 标题模糊搜索 | `taskNameLike()` |
| `taskCommentLike(String)` | 处理意见模糊搜索 | Flowable 无直接对应 |
| `createdAfter(Date)` | 创建时间范围开始 | `taskCreatedAfter()` |
| `createdBefore(Date)` | 创建时间范围结束 | `taskCreatedBefore()` |
| `taskUnassigned()` | 查询未认领任务 | `taskUnassigned()` |

**完成后可 Deprecated**：
- `findCompletedTaskList(CompletedTaskQueryParam)` → `createTaskQuery().taskAssignee(u).processDefinitionTypeIn(types).completeTimeAfter(s).completeTimeBefore(e).taskTitle(t).taskTag(tag).taskStatus("completed").list()`
- `countCompletedTaskList(CompletedTaskQueryParam)` → 同上 `.count()`

### 3.2 ProcessInstanceQuery 缺失字段

| 方法 | 用途 | 对标 Flowable |
|------|------|--------------|
| `processDefinitionTypeIn(List<String>)` | 多类型过滤 | `processDefinitionKeyIn()` |
| `processTitle(String)` | 流程标题过滤 | Flowable 无直接对应 |
| `processTitleLike(String)` | 流程标题模糊搜索 | Flowable 无直接对应 |
| `processTag(String)` | 流程标签过滤 | Flowable 无直接对应 |
| `involvedUser(String)` | 参与人过滤（发起人或处理过任务的） | `involvedUser()` |

**完成后可 Deprecated**：
- `findCompletedProcessList(CompletedProcessQueryParam)` → `createProcessQuery().involvedUser(u).processDefinitionTypeIn(types).completedAfter(s).completedBefore(e).processTitle(t).processTag(tag).processStatus("completed").list()`
- `countCompletedProcessList(CompletedProcessQueryParam)` → 同上 `.count()`

> **注意**：`involvedUser` 语义是"参与过此流程的用户"，实现时如果没有独立的参与人表，可降级为 `startUserId` 过滤。

---

## 4. G3：DeploymentQuery Fluent API（P1）

### 4.1 问题分析

`DeploymentQueryService` 有 11 个过滤字段，是除 Task/Process 之外最复杂的查询服务，适合提供 Fluent API。

其他 QueryService（Execution、Activity、Repository、TaskAssignee、Variable）查询模式固定且简单，**不需要 Fluent API**。

### 4.2 改进方案

```java
public interface DeploymentQuery extends Query<DeploymentQuery, DeploymentInstance> {

    DeploymentQuery deploymentInstanceId(String id);
    DeploymentQuery processDefinitionVersion(String version);
    DeploymentQuery processDefinitionType(String type);
    DeploymentQuery processDefinitionCode(String code);
    DeploymentQuery processDefinitionName(String name);
    DeploymentQuery processDefinitionNameLike(String nameLike);
    DeploymentQuery processDefinitionDescLike(String descLike);
    DeploymentQuery deploymentUserId(String userId);
    DeploymentQuery deploymentStatus(String status);
    DeploymentQuery logicStatus(String logicStatus);

    // Ordering
    DeploymentQuery orderByCreateTime();
    DeploymentQuery orderByModifyTime();
    DeploymentQuery asc();
    DeploymentQuery desc();
}
```

**实现要点**：
1. 创建 `DeploymentQuery` 接口 + `DeploymentQueryImpl`
2. `SmartEngine` 新增 `createDeploymentQuery()` 方法
3. 内部复用 `DeploymentInstanceQueryParam` + 已有 Storage 查询

**完成后可 Deprecated**：
- `DeploymentQueryService.findList(DeploymentInstanceQueryParam)` → `createDeploymentQuery().xxx().list()`
- `DeploymentQueryService.count(DeploymentInstanceQueryParam)` → `createDeploymentQuery().xxx().count()`
- `DeploymentQueryService.findById(id)` → `createDeploymentQuery().deploymentInstanceId(id).singleResult()`

---

## 5. G4：高级查询能力（P2 - 远期）

| 能力 | Activiti/Flowable | SmartEngine 计划 |
|------|-------------------|-----------------|
| `or()` / `endOr()` 组合查询 | Flowable 支持 | **暂不实现** - 使用场景有限，复杂度高 |
| `variableValueEquals(name, value)` | 两者都支持 | **暂不实现** - 需要 JOIN 变量表，性能影响大，建议业务层通过 `processInstanceIdIn` 间接实现 |
| `includeProcessVariables()` | Flowable 支持 | **暂不实现** - 可在查询结果后单独查变量 |

---

## 6. 实施路线图

### Phase 1（P0 — 本迭代）

**目标**：完成 G1 - TaskQuery Assignee 候选人查询

```
改动文件：
├── core/.../query/TaskQuery.java                    (+4 methods)
├── core/.../query/impl/TaskQueryImpl.java           (双路径分发逻辑)
├── core/.../service/query/TaskQueryService.java     (@Deprecated +4)
└── 测试验证
```

**预计工作量**：接口 + Impl + 测试 ≈ 半天

### Phase 2（P1 — 本迭代）

**目标**：完成 G2 + G3

```
G2 改动文件：
├── core/.../query/TaskQuery.java                    (+6 methods)
├── core/.../query/impl/TaskQueryImpl.java
├── core/.../query/ProcessInstanceQuery.java         (+5 methods)
├── core/.../query/impl/ProcessInstanceQueryImpl.java
├── core/.../service/param/query/TaskInstanceQueryParam.java (titleLike etc.)
├── core/.../service/param/query/ProcessInstanceQueryParam.java
├── extension/.../mybatis/sqlmap/task_instance.xml   (LIKE 条件)
├── extension/.../mybatis/sqlmap/process_instance.xml (LIKE 条件)
├── core/.../service/query/TaskQueryService.java     (@Deprecated +2)
├── core/.../service/query/ProcessQueryService.java  (@Deprecated +2)
└── 测试验证

G3 改动文件：
├── core/.../query/DeploymentQuery.java              (NEW)
├── core/.../query/impl/DeploymentQueryImpl.java     (NEW)
├── core/.../SmartEngine.java                        (+createDeploymentQuery())
├── core/.../DefaultSmartEngine.java
├── core/.../service/query/DeploymentQueryService.java (@Deprecated)
└── 测试验证
```

**预计工作量**：≈ 1 天

### Phase 3（P2 — 远期规划）

**目标**：根据用户反馈评估 or/endOr、variableValueEquals 等高级能力的实际需求

---

## 7. 完成后 Deprecated 进度表

| QueryService | 方法总数 | 已 Deprecated | Phase 1 后 | Phase 2 后 | 保留不变 |
|---|---|---|---|---|---|
| TaskQueryService | 12 | 6 | **10** (+4) | **12** (+2) | 0 |
| ProcessQueryService | 6 | 4 | 4 | **6** (+2) | 0 |
| SupervisionQueryService | 4 | 4 | 4 | 4 | 0 |
| NotificationQueryService | 7 | 7 | 7 | 7 | 0 |
| DeploymentQueryService | 4 | 0 | 0 | **4** (+4) | 0 |
| **合计** | **33** | **21** | **25** | **33** | **0** |

> Phase 2 完成后，所有 QueryService 方法均可标记 @Deprecated，Fluent API 100% 覆盖。
