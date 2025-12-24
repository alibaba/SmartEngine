# 运行模式：Custom vs DataBase

SmartEngine 从源码层面把“引擎核心语义”与“存储/集成实现”拆开：

- 核心执行能力在 `core/`（解析、执行模型、行为、服务接口、扩展机制）
- 存储与增强能力通过 `extension/` 提供（Custom / DataBase / Retry 等）

本章说明两种最常见的落地模式，以及它们的边界。

---

## 1. 一句话对比

| 维度 | Custom 模式 | DataBase 模式 |
|---|---|---|
| 流程定义存储 | 通常由你自己决定；`RepositoryCommandService` 只负责“解析并加载到本地内存” | 流程定义可通过 `DeploymentInstance` 持久化到 DB（表 `se_deployment_instance`），并触发加载 |
| 运行期实例 | 你可自己实现 Storage 接口（或只做内存） | 引擎提供关系库实现：process/execution/activity/task/variable… |
| userTask 待办 | 由你系统已有待办体系承担 | 引擎提供任务表 + 分派表（`se_task_instance`/`se_task_assignee_instance`）与分派扩展点 |
| 事务边界 | 由你自己决定（可与业务事务合并） | 通常以 DB 事务为边界（MyBatis + Spring Tx） |
| 集群一致性 | 需要你自行做“定义一致性/缓存一致性” | 仍需要一致性策略，但有 DB deployment 作为统一真相源更易实现 |
| 适用场景 | 业务系统“已有一切”，只需要引擎语义（轻量/可控） | 想要开箱即用的流程 + 存储 + 待办增强 |

---

## 2. Custom 模式的“核心约束”

### 2.1 Repository 只负责“加载到本地内存”

`RepositoryCommandService` 的 deploy 是“把 BPMN XML 解析为内存模型”：

- 单机 OK
- 集群必须解决“每台机器都加载同一套 definition”的一致性

典型做法：

1) BPMN 文件存到 DB/配置中心/对象存储  
2) 通过发布事件通知所有节点 reload  
3) 或服务启动时扫描并预热缓存

### 2.2 你需要实现（或决定）这些集成点

最重要的三个：

- `InstanceAccessor`：引擎如何拿到业务 bean/类实例（delegation、listener 用）
- Storage 接口实现（可选但生产强烈建议）：`core/.../instance/storage/*`
- 变量持久化策略（可选）：`VariablePersister`

仓库的 Custom 模式实现（用于测试）在：

- `extension/storage/storage-custom`（基于 `PersisterSession` 的线程内 Map）

> 注意：它更像“教学/测试存根”，不适合直接上生产。

---

## 3. DataBase 模式的“核心约束”

### 3.1 DataBase 模式到底提供了什么？

它提供“关系库存储实现 + 一组工作流增强表 + MyBatis SQLMap”：

- 存储实现类：`extension/storage/storage-mysql/.../RelationshipDatabase*Storage`
- SQLMap：`extension/storage/storage-mysql/src/main/resources/mybatis/sqlmap/*.xml`
- DDL：`extension/storage/storage-mysql/src/main/resources/sql/*`

### 3.2 仍然要你提供的东西

- `IdGenerator`：生成 instanceId/taskId（测试默认 `TimeBasedIdGenerator`）
- `InstanceAccessor`：通常对接 Spring 容器
- `TaskAssigneeDispatcher`：决定 userTask 的候选人/候选组/组织岗位等规则
- 集群下的部署一致性（推荐：以 `DeploymentInstance` 表为真相源，做全量加载或增量通知）

---

## 4. 模式选择建议（经验法则）

- 如果你有成熟的：**用户/组织、待办、变量、审计、存储、权限** → 选 **Custom**
- 如果你想先跑通 MVP，后续再逐步替换存储/待办 → 先用 **DataBase**（更快）
- 如果你要做“平台化、可插拔” → 推荐：
  - 核心语义沿用 SmartEngine
  - 存储接口自研实现（兼容多 DB、历史表、归档等）
  - 任务分派与权限深度融合（RBAC/ABAC）

---

下一步：

- 支持的 BPMN 子集：`bpmn-subset.md`
- 执行模型：`execution-model.md`

