# 存储分层：接口、扩展点、事务边界

SmartEngine 的存储设计核心思想是：

- 引擎核心只依赖 **Storage 接口**
- 不同落地模式（Custom / DataBase）通过扩展模块提供实现

---

## 1. Storage 接口清单（核心）

接口位置：`core/src/main/java/com/alibaba/smart/framework/engine/instance/storage/`

- `ActivityInstanceStorage`
- `DeploymentInstanceStorage`
- `ExecutionHistoryInstanceStorage`（注意：仓库默认 DDL 未提供 history 表，通常由你扩展实现）
- `ExecutionInstanceStorage`
- `NotificationInstanceStorage`
- `ProcessDefinitionStorage`（流程定义存储抽象，Custom/DataBase 各自有策略）
- `ProcessInstanceStorage`
- `SupervisionInstanceStorage`
- `TaskAssigneeInstanceStorage`
- `TaskInstanceStorage`
- `VariableInstanceStorage`

> 这些接口的入参里经常包含 `ProcessEngineConfiguration`，用于拿到 tenantId、idGenerator、instanceAccessor、variablePersister 等上下文能力。

---

## 2. DataBase 模式的实现层次

DataBase 模式模块：`extension/storage/storage-mysql`

典型实现结构：

- `persister/database/entity/*`：MyBatis entity
- `persister/database/dao/*`：DAO 接口
- `mybatis/sqlmap/*.xml`：SQLMap
- `persister/database/service/RelationshipDatabase*Storage`：Storage 接口实现（包装 DAO，提供事务边界）

---

## 3. Custom 模式的实现层次

Custom 模式模块：`extension/storage/storage-custom`

它的实现基于：

- `PersisterSession`：线程内 session（Map 存储）
- `Custom*Storage`：实现 Storage 接口
- 单元测试用例用它来验证引擎语义（并行网关、跳转等）

生产环境建议：

- 以 Storage 接口为契约，实现你自己的数据库/事件日志/缓存存储
- 明确事务边界：引擎推进与业务写入的一致性策略（强一致 or 最终一致）

---

## 4. 事务边界（建议）

### 4.1 Custom 模式（推荐强一致）

如果你的 Storage 直接对接业务数据库：

- 引擎推进（写 process/execution/activity/task/variable）与业务写入放同一事务
- start/signal/complete 等外部入口做幂等（见 `03-usage/api-guide.md`）

### 4.2 DataBase 模式（Spring Tx）

典型做法：

- 入口方法（Controller/Service）加 `@Transactional`
- 业务写入 + 引擎调用都在同一事务中
- 失败回滚：避免引擎产生“孤儿实例/孤儿任务”

---

## 5. 表结构策略（TableSchemaStrategy）

引擎配置里存在 `TableSchemaStrategy`（默认 `DefaultTableSchemaStrategy`），用于：

- 决定表名前缀、schema、或多租户策略（例如 schema-per-tenant）
- 影响 MyBatis/DAO 的 SQL 拼接（实现细节以 storage-mysql 为准）

如果你要实现“同一套引擎同时支持 MySQL/PostgreSQL、多租户 schema 隔离”，这里是关键扩展点之一。

---

下一步：

- DataBase 表结构与索引：`database-schema.md`
- MyBatis 兼容与类型坑：`mybatis-notes.md`

