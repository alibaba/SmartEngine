# Smart-Engine vs Activiti vs Flowable: Query API 方法级对比

> 调研日期: 2026-02-08 (Phase 1 候选人查询完成后)
> 对比版本: Activiti 5.22.0 / 6.0, Flowable 7.x, SmartEngine dev 分支

---

## 1. 数量统计汇总

### TaskQuery 方法数量对比

| 类别 | SmartEngine | Activiti | Flowable |
|------|:-----------:|:--------:|:--------:|
| ID/主键过滤 | 3 | 4 | 7 |
| 名称/描述/文本过滤 | 4 | 9 | 9 |
| 状态过滤 | 4 | 3 | 4 |
| 用户/办理人过滤 | 3 | 7 | 11 |
| 候选人/候选组过滤 | **6** | 4 | 4 |
| 时间过滤 | 2 | 7 | 19 |
| 优先级过滤 | 1 | 3 | 3 |
| 流程定义过滤 | 6 | 14 | 15 |
| 分类/标签/扩展 | 4 | 1 | 5 |
| 变量过滤 | 0 | 22 | 35+ |
| 租户过滤 | 2 | 3 | 3 |
| 排序 | 7 | 15 | 16 |
| 分页/结果 | 6 | 4 | 4 |
| 高级特性 | 2 | 6 | 8 |
| CMMN/Scope (Flowable 独有) | 0 | 0 | 17 |
| **总计 (约)** | **~50** | **~102** | **~160+** |

### SmartEngine 独有特性（Activiti/Flowable 都没有的）

| 特性 | 说明 |
|------|------|
| **条件式参数 `(boolean, value)`** | 所有主要过滤方法的条件重载，避免 if-else 拼装 |
| **`taskCandidateOrGroup(userId, groupIds)`** | 单方法同时查候选人 OR 候选组 |
| **`taskTag` / `taskExtension`** | 专属标签和扩展字段过滤 |
| **`taskTitle` / `taskComment`** | 专属标题和评论字段过滤 |
| **SupervisionQuery** | 督办查询（Activiti/Flowable 无此概念） |
| **NotificationQuery** | 通知查询（Activiti/Flowable 无此概念） |
| **统一运行时+历史查询** | 无需分别调用 Runtime/Historic 两个 Service |
| **`pageOffset` / `pageSize` 链式设置** | 分页参数可预设后再 `list()` |

---

## 2. TaskQuery 详细对比

### 2.1 ID / 主键过滤

| 能力 | SmartEngine | Activiti | Flowable |
|------|:-:|:-:|:-:|
| 按任务 ID | ✅ `taskInstanceId` | ✅ `taskId` | ✅ `taskId` |
| 按多个任务 ID | ❌ | ❌ | ✅ `taskIds` |
| 按执行实例 ID | ❌ | ✅ `executionId` | ✅ `executionId` |
| 按活动实例 ID | ✅ `activityInstanceId` | ❌ | ❌ |
| 按 taskDefinitionKey | ❌ | ✅ | ✅ |
| 按 taskDefinitionKey LIKE | ❌ | ✅ | ✅ |

### 2.2 名称/描述/文本过滤

| 能力 | SmartEngine | Activiti | Flowable |
|------|:-:|:-:|:-:|
| 按名称 | ❌ | ✅ `taskName` | ✅ `taskName` |
| 按名称 IN | ❌ | ✅ `taskNameIn` | ✅ `taskNameIn` |
| 按名称 LIKE | ❌ | ✅ `taskNameLike` | ✅ `taskNameLike` |
| 按名称 LIKE 忽略大小写 | ❌ | ✅ | ✅ |
| 按描述 | ❌ | ✅ `taskDescription` | ✅ `taskDescription` |
| 按描述 LIKE | ❌ | ✅ `taskDescriptionLike` | ✅ `taskDescriptionLike` |
| 按标题 | ✅ `taskTitle` | ❌ | ❌ |
| 按标题 (条件式) | ✅ `taskTitle(bool, ...)` | ❌ | ❌ |
| 按评论 | ✅ `taskComment` | ❌ | ❌ |

### 2.3 状态过滤

| 能力 | SmartEngine | Activiti | Flowable |
|------|:-:|:-:|:-:|
| 按状态 | ✅ `taskStatus` | ❌ | ✅ `taskState` |
| 按状态 (条件式) | ✅ `taskStatus(bool, ...)` | ❌ | ❌ |
| 按多个状态 IN | ✅ `taskStatusIn` | ❌ | ❌ |
| 仅活动 `active()` | ❌ | ✅ | ✅ |
| 仅挂起 `suspended()` | ❌ | ✅ | ✅ |

### 2.4 用户/办理人过滤

| 能力 | SmartEngine | Activiti | Flowable |
|------|:-:|:-:|:-:|
| 按办理人 (assignee) | ✅ `taskAssignee` | ✅ | ✅ |
| 按办理人 (条件式) | ✅ | ❌ | ❌ |
| 按办理人 LIKE | ❌ | ✅ | ✅ |
| 按多个办理人 IDs | ❌ | ❌ | ✅ `taskAssigneeIds` |
| 未分配 `taskUnassigned()` | ❌ | ✅ | ✅ |
| 已分配 `taskAssigned()` | ❌ | ❌ | ✅ |
| 按所有者 (owner) | ❌ | ✅ | ✅ |
| 按参与用户 (involvedUser) | ❌ | ✅ | ✅ |

### 2.5 候选人/候选组过滤

| 能力 | SmartEngine | Activiti | Flowable |
|------|:-:|:-:|:-:|
| 按候选人 | ✅ `taskCandidateUser` | ✅ | ✅ |
| 按候选组 | ✅ `taskCandidateGroup` | ✅ | ✅ |
| 按多个候选组 IN | ✅ `taskCandidateGroupIn` | ✅ | ✅ |
| 按多个候选组 (varargs) | ✅ | ❌ | ❌ |
| 候选人 OR 候选组 | ✅ `taskCandidateOrGroup` | ❌ | ❌ |
| 候选人 OR 已办理人 | ❌ | ✅ `taskCandidateOrAssigned` | ❌ |

### 2.6 时间过滤

| 能力 | SmartEngine | Activiti | Flowable |
|------|:-:|:-:|:-:|
| 创建时间 = | ❌ | ✅ `taskCreatedOn` | ✅ |
| 创建时间 > | ❌ | ✅ `taskCreatedAfter` | ✅ |
| 创建时间 < | ❌ | ✅ `taskCreatedBefore` | ✅ |
| 完成时间 > | ✅ `completeTimeAfter` | ❌* | ❌* |
| 完成时间 < | ✅ `completeTimeBefore` | ❌* | ❌* |
| 到期日 = / > / < | ❌ | ✅ | ✅ |
| 无到期日 | ❌ | ✅ | ✅ |
| 认领时间 = / > / < | ❌ | ❌ | ✅ |

> *Activiti/Flowable 的完成时间过滤在 HistoricTaskInstanceQuery 中，SmartEngine 合并在同一接口

### 2.7 流程定义过滤

| 能力 | SmartEngine | Activiti | Flowable |
|------|:-:|:-:|:-:|
| 按流程实例 ID | ✅ | ✅ | ✅ |
| 按流程实例 ID (条件式) | ✅ | ❌ | ❌ |
| 按多个流程实例 ID | ✅ `processInstanceIdIn` | ✅ | ✅ |
| 按流程定义 ID | ❌ | ✅ | ✅ |
| 按流程定义 Key | ❌ | ✅ | ✅ |
| 按流程定义 Key LIKE | ❌ | ✅ | ✅ |
| 按多个流程定义 Key | ❌ | ✅ | ✅ |
| 按流程定义名称 | ❌ | ✅ | ✅ |
| 按流程定义类型 | ✅ `processDefinitionType` | ❌ | ❌ |
| 按流程定义活动 ID | ✅ | ❌ | ❌ |
| 按部署 ID | ❌ | ✅ | ✅ |
| 按业务 Key | ❌ | ✅ | ✅ |

### 2.8 变量过滤

| 能力 | SmartEngine | Activiti | Flowable |
|------|:-:|:-:|:-:|
| 任务变量 =, !=, >, >=, <, <=, LIKE | ❌ | ✅ (完整10+方法) | ✅ (完整12+方法) |
| 流程变量 =, !=, >, >=, <, <=, LIKE | ❌ | ✅ (完整10+方法) | ✅ (完整12+方法) |
| 变量 EXISTS / NOT EXISTS | ❌ | ❌ | ✅ |
| Case 变量 (全套) | ❌ | ❌ | ✅ |

### 2.9 高级特性

| 能力 | SmartEngine | Activiti | Flowable |
|------|:-:|:-:|:-:|
| OR 查询 `or()` / `endOr()` | ❌ | ✅ | ✅ |
| 包含流程变量 | ❌ | ✅ | ✅ |
| 包含任务本地变量 | ❌ | ✅ | ✅ |
| 包含身份链接 | ❌ | ❌ | ✅ |
| 本地化 locale | ❌ | ✅ | ✅ |
| 条件式参数 (boolean guard) | ✅ | ❌ | ❌ |

---

## 3. ProcessInstanceQuery 详细对比

### 3.1 核心过滤

| 能力 | SmartEngine | Activiti | Flowable |
|------|:-:|:-:|:-:|
| 按实例 ID | ✅ | ✅ | ✅ |
| 按多个实例 ID | ✅ `processInstanceIdIn` | ✅ | ✅ |
| 按状态 | ✅ `processStatus` | ❌ | ❌ |
| 仅活动 / 仅挂起 | ❌ | ✅ | ✅ |
| 按发起人 | ✅ `startedBy` | ❌ | ✅ |
| 按参与用户 | ❌ | ✅ `involvedUser` | ✅ |
| 按流程定义 Key | ❌ | ✅ | ✅ |
| 按流程定义类型 | ✅ | ❌ | ❌ |
| 按流程定义 ID+版本 | ✅ | ❌ | ❌ |
| 按父流程实例 | ✅ `parentInstanceId` | ✅ `superProcessInstanceId` | ✅ |
| 按业务唯一 ID | ✅ `bizUniqueId` | ✅ `processInstanceBusinessKey` | ✅ |
| 按业务 Key LIKE | ❌ | ❌ | ✅ |
| 启动时间 > / < | ✅ | ❌ | ✅ |
| 完成时间 > / < | ✅ | ❌ | ❌* |
| 变量过滤 (全套) | ❌ | ✅ | ✅ |
| OR 查询 | ❌ | ✅ | ✅ |
| 按子流程实例 | ❌ | ✅ | ✅ |
| 排除子流程 | ❌ | ✅ | ✅ |

> *Flowable 的完成时间在 HistoricProcessInstanceQuery 中

### 3.2 Historic 查询（Activiti/Flowable 独立接口）

SmartEngine **没有独立的 Historic 查询接口**，运行时和历史合并在同一个 Query 接口中。

| 能力 | SmartEngine | Activiti `HistoricProcessInstanceQuery` | Flowable |
|------|:-:|:-:|:-:|
| `finished()` | ❌ (用 `processStatus("completed")`) | ✅ | ✅ |
| `unfinished()` | ❌ (用 `processStatus("running")`) | ✅ | ✅ |
| `deleted()` / `notDeleted()` | ❌ | ✅ | ✅ |
| 完成时间范围 | ✅ `completedAfter/Before` | ✅ `finishedAfter/Before` | ✅ |
| 按持续时间排序 | ❌ | ✅ | ✅ |

---

## 4. DeploymentQuery 对比

SmartEngine **尚无 Fluent DeploymentQuery 接口**（计划在 Phase 2 实现）。

| 能力 | SmartEngine | Activiti | Flowable |
|------|:-:|:-:|:-:|
| Fluent 接口 | ❌ | ✅ | ✅ |
| 按部署 ID | ❌ | ✅ | ✅ |
| 按名称 | ❌ | ✅ | ✅ |
| 按名称 LIKE | ❌ | ✅ | ✅ |
| 按分类 | ❌ | ✅ | ✅ |
| 按租户 ID | ❌ | ✅ | ✅ |
| 按流程定义 Key | ❌ | ✅ | ✅ |
| 仅最新版本 | ❌ | ❌ | ✅ `latest()` |
| 排序 | ❌ | ✅ | ✅ |

---

## 5. 差距评估与优先级

### P1 — 高价值差距（建议尽快补齐）

| 差距 | 说明 | 改进复杂度 |
|------|------|:---------:|
| `createdAfter` / `createdBefore` | 创建时间范围过滤，高频查询条件 | 低 |
| `taskTitleLike` | 标题模糊搜索，已有 title 字段，只需加 LIKE | 低 |
| `taskUnassigned()` | 查未认领任务，审批场景常用 | 低 |
| `processDefinitionTypeIn(List)` | 多类型过滤，CompletedTask/Process 需要 | 低 |
| DeploymentQuery | 部署查询 Fluent API，11 个过滤字段 | 中 |
| `involvedUser` | 按参与用户查流程（ProcessInstanceQuery） | 中 |

### P2 — 中价值差距

| 差距 | 说明 | 改进复杂度 |
|------|------|:---------:|
| `taskAssigneeLike` | 办理人模糊搜索 | 低 |
| `taskMinPriority` / `taskMaxPriority` | 优先级范围查询 | 低 |
| `processDefinitionKey` / `processDefinitionKeyLike` | 按定义 Key 查任务 | 低 |
| `taskNameLike` / `taskDescriptionLike` | SmartEngine 用 title 代替 name | 低 |
| `taskDueDate` / `taskDueBefore` / `taskDueAfter` | 到期日（需表结构支持） | 中 |
| `withoutTenantId()` | 无租户过滤 | 低 |

### P3 — 低优先级（暂不实现）

| 差距 | 理由 |
|------|------|
| 变量过滤 (22+ 方法) | 需 JOIN 变量表，性能影响大，建议业务层通过 `processInstanceIdIn` 间接实现 |
| `or()` / `endOr()` | 实现复杂度高，使用场景有限 |
| `includeProcessVariables()` | 查询后单独查变量即可 |
| 本地化 `locale()` | SmartEngine 目前不涉及 i18n |
| CMMN/Scope (17 个方法) | Flowable 独有，SmartEngine 不支持 CMMN |
| `taskOwner` / `taskOwnerLike` | SmartEngine 无 owner 概念 |
| `taskDelegationState` | SmartEngine 无委派机制 |

---

## 6. 总结

| 维度 | SmartEngine | Activiti | Flowable |
|------|:-:|:-:|:-:|
| TaskQuery 方法总数 | ~50 | ~102 | ~160+ |
| ProcessInstanceQuery 方法总数 | ~25 | ~40 | ~60+ |
| 核心业务场景覆盖度 | **85%** | 95% | 100% |
| 候选人查询 | ✅ (含独有 OR 组合) | ✅ | ✅ |
| 条件式动态查询 | ✅ (独有优势) | ❌ | ❌ |
| 变量查询 | ❌ | ✅ | ✅ |
| DeploymentQuery | ❌ (Phase 2) | ✅ | ✅ |
| 督办/通知查询 | ✅ (独有) | ❌ | ❌ |
| Historic 独立接口 | ❌ (合并设计) | ✅ | ✅ |

**结论**：SmartEngine 在核心 BPMN 查询场景（待办、已办、候选人、流程状态）上已达到 **85% 覆盖**。主要差距集中在：创建时间过滤、模糊搜索、多值过滤等 P1 项（6 项，改进复杂度低-中），以及变量查询等 P3 项（复杂度高但使用频率低）。SmartEngine 的**条件式参数**和**督办/通知查询**是独有优势。
