# Query API Gap 分析与改进计划

## 1. Gap 总览

基于与 Activiti/Flowable 的对比分析和当前未 Deprecated 旧 API 的梳理，总共识别出 **4 类 Gap**：

| 编号 | Gap 类型 | 影响面 | 优先级 | 状态 |
|------|---------|--------|--------|------|
| G1 | TaskQuery 缺少 Assignee 候选人查询（JOIN 模式） | 4 个旧 API 无法 Deprecated | P0 | **已完成** |
| G2 | TaskQuery / ProcessInstanceQuery 缺少多值/模糊/时间范围过滤 | CompletedTask/Process 旧 API 无法 Deprecated；与 Flowable 有差距 | P1 | **已完成** |
| G3 | 缺少 DeploymentQuery Fluent API | DeploymentQueryService 无法 Deprecated | P1 | **已完成** |
| G4 | 缺少高级查询能力（or/endOr、variableValueEquals） | 与 Activiti/Flowable 有差距但业务场景不常用 | P2 | 远期规划 |

---

## 2. G1：TaskQuery Assignee 候选人查询（P0）✅ 已完成

### 2.1 问题分析

当前 `TaskQuery.taskAssignee(userId)` 映射到 `claim_user_id` 字段，仅能查询**已认领**的任务。

但以下旧 API 使用了 `se_task_instance JOIN se_task_assignee_instance` 的模式查询**候选人待办**：

```
findPendingTaskList(PendingTaskQueryParam)      → assigneeUserId + assigneeGroupIdList (OR 逻辑)
countPendingTaskList(PendingTaskQueryParam)      → 同上 count 版本
findTaskListByAssignee(TaskInstanceQueryByAssigneeParam) → 同上 + 支持 status 过滤
countTaskListByAssignee(TaskInstanceQueryByAssigneeParam) → 同上 count 版本
```

### 2.2 改进方案（已实现）

```java
TaskQuery taskCandidateUser(String userId);
TaskQuery taskCandidateGroup(String groupId);
TaskQuery taskCandidateGroupIn(List<String> groupIds);
TaskQuery taskCandidateGroupIn(String... groupIds);  // default method
TaskQuery taskCandidateOrGroup(String userId, List<String> groupIds);
```

**已 Deprecated**：findPendingTaskList, countPendingTaskList, findTaskListByAssignee, countTaskListByAssignee

**测试**：23 个集成测试用例（`TaskCandidateQueryIntegrationTest`）

---

## 3. G2：多值/模糊/时间范围过滤增强（P1）✅ 已完成

### 3.1 TaskQuery 新增方法

| 方法 | 用途 | 对标 Flowable | 状态 |
|------|------|--------------|------|
| `createdAfter(Date)` | 创建时间范围开始 | `taskCreatedAfter()` | ✅ |
| `createdBefore(Date)` | 创建时间范围结束 | `taskCreatedBefore()` | ✅ |
| `taskTitleLike(String)` | 标题模糊搜索 | `taskNameLike()` | ✅ |
| `taskTitleLike(boolean, String)` | 条件标题模糊搜索 | SmartEngine 特有 | ✅ |
| `taskUnassigned()` | 查询未认领任务 | `taskUnassigned()` | ✅ |
| `processDefinitionTypeIn(List)` | 多类型过滤 | `processDefinitionKeyIn()` | ✅ |
| `processDefinitionTypeIn(String...)` | 多类型过滤（varargs） | 同上 | ✅ |
| `taskAssigneeLike(String)` | 认领人模糊搜索 | `taskAssigneeLike()` | ✅ |
| `taskMinPriority(Integer)` | 最低优先级过滤 | `taskMinPriority()` | ✅ |
| `taskMaxPriority(Integer)` | 最高优先级过滤 | `taskMaxPriority()` | ✅ |

### 3.2 ProcessInstanceQuery 新增方法

| 方法 | 用途 | 对标 Flowable | 状态 |
|------|------|--------------|------|
| `processDefinitionTypeIn(List)` | 多类型过滤 | `processDefinitionKeyIn()` | ✅ |
| `processDefinitionTypeIn(String...)` | 多类型过滤（varargs） | 同上 | ✅ |
| `involvedUser(String)` | 参与人过滤（当前映射为发起人） | `involvedUser()` | ✅ |

### 3.3 所有查询通用新增方法

| 方法 | 用途 | 对标 Flowable | 状态 |
|------|------|--------------|------|
| `withoutTenantId()` | 跨租户查询 | `withoutTenantId()` | ✅ |

### 3.4 MyBatis SQL 扩展

- `task_instance.xml` where_condition：新增 `titleLike` LIKE、`createTimeStart/End` 时间范围、`processDefinitionTypeList` IN、`unassigned` IS NULL、`claimUserIdLike` LIKE、`minPriority/maxPriority` 范围
- `process_instance.xml` condition：新增 `processDefinitionTypeList` IN

**测试**：38 个集成测试用例（`FluentQueryP1P2IntegrationTest`）

---

## 4. G3：DeploymentQuery Fluent API（P1）✅ 已完成

### 4.1 实现

```java
public interface DeploymentQuery extends Query<DeploymentQuery, DeploymentInstance> {
    DeploymentQuery deploymentInstanceId(String id);
    DeploymentQuery processDefinitionVersion(String version);
    DeploymentQuery processDefinitionType(String type);
    DeploymentQuery processDefinitionType(boolean condition, String type);
    DeploymentQuery processDefinitionCode(String code);
    DeploymentQuery processDefinitionName(String name);
    DeploymentQuery processDefinitionNameLike(String nameLike);
    DeploymentQuery processDefinitionNameLike(boolean condition, String nameLike);
    DeploymentQuery processDefinitionDescLike(String descLike);
    DeploymentQuery deploymentUserId(String userId);
    DeploymentQuery deploymentStatus(String status);
    DeploymentQuery deploymentStatus(boolean condition, String status);
    DeploymentQuery logicStatus(String logicStatus);
    DeploymentQuery orderByDeploymentId();
    DeploymentQuery orderByCreateTime();
    DeploymentQuery orderByModifyTime();
    DeploymentQuery asc();
    DeploymentQuery desc();
}
```

- `SmartEngine.createDeploymentQuery()` 入口
- `deployment_instance.xml` 添加 dynamic order_by 支持
- **已 Deprecated**：`DeploymentQueryService` 全部 4 个方法

---

## 5. G4：高级查询能力（P2 — 远期）

| 能力 | Activiti/Flowable | SmartEngine 计划 |
|------|-------------------|-----------------|
| `or()` / `endOr()` 组合查询 | Flowable 支持 | **暂不实现** - 使用场景有限，复杂度高 |
| `variableValueEquals(name, value)` | 两者都支持 | **暂不实现** - 需要 JOIN 变量表，性能影响大 |
| `includeProcessVariables()` | Flowable 支持 | **暂不实现** - 可在查询结果后单独查变量 |
| `taskDueDate` / `taskDueBefore` / `taskDueAfter` | 两者都支持 | **暂不实现** - 需要 schema 变更添加 due_date 列 |

---

## 6. 完成后 Deprecated 进度表

| QueryService | 方法总数 | Phase 1 前 | G1 完成后 | G2+G3 完成后 | 保留不变 |
|---|---|---|---|---|---|
| TaskQueryService | 12 | 6 | **10** (+4) | **12** (+2) | 0 |
| ProcessQueryService | 6 | 4 | 4 | **6** (+2) | 0 |
| SupervisionQueryService | 4 | 4 | 4 | 4 | 0 |
| NotificationQueryService | 7 | 7 | 7 | 7 | 0 |
| DeploymentQueryService | 4 | 0 | 0 | **4** (+4) | 0 |
| **合计** | **33** | **21** | **25** | **33** | **0** |

> ✅ G1+G2+G3 完成后，所有 QueryService 方法均已标记 @Deprecated，Fluent API 100% 覆盖。

## 7. 测试覆盖

| 测试类 | 测试数量 | 覆盖范围 |
|--------|---------|---------|
| TaskCandidateQueryIntegrationTest | 23 | G1: 候选人查询 |
| FluentQueryP1P2IntegrationTest | 38 | G2+G3: 全部 P1+P2 新功能 |
| **合计** | **61** | 全覆盖 |

**全量回归**：core 91 + storage-mysql 323 + storage-custom 56 = **470 测试全部通过**
