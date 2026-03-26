# 领域对象：Deployment / Definition / Process / Activity / Execution / Task / Variable…

本章把 SmartEngine 的“代码对象”与“数据库对象”（DataBase 模式）对齐，帮助你：

- 看懂引擎的 CQRS API 参数与返回值
- 定位你需要扩展/联动的表与数据
- 做二次开发（增加字段、做历史归档、做查询优化）

---

## 1. 代码层：模型所在位置

- 解析/定义模型：`core/.../model/assembly/*`
- 运行期实例模型：`core/.../model/instance/*`
- Param/Command：`core/.../service/param/*`
- 扩展点与行为：`core/.../engine/*` 与 `core/.../behavior/*`

## 2. DataBase 模式表与核心对象映射

| 表 | 代码对象 | 说明 |
|---|---|---|
| `se_deployment_instance` | `DeploymentInstance` | BPMN 文件持久化记录（可作为集群“定义真相源”） |
| `se_process_instance` | `ProcessInstance` | 流程实例 |
| `se_execution_instance` | `ExecutionInstance` | token/执行分支载体 |
| `se_activity_instance` | `ActivityInstance` | 节点运行期实例 |
| `se_task_instance` | `TaskInstance` | userTask 待办任务（增强） |
| `se_task_assignee_instance` | `TaskAssigneeInstance*` | 候选人/组/组织/岗位等分派记录 |
| `se_variable_instance` | `VariableInstance` | 变量持久化记录 |
| `se_supervision_instance` | `SupervisionInstance` | 督办记录（增强） |
| `se_notification_instance` | `NotificationInstance` | 通知记录（增强） |
| `se_task_transfer_record` | `TaskTransferRecordEntity` | 任务转派记录（增强） |
| `se_assignee_operation_record` | `AssigneeOperationRecordEntity` | 分派操作记录（增强） |
| `se_process_rollback_record` | `ProcessRollbackRecordEntity` | 回退记录（增强） |

> DDL 详见 `04-persistence/database-schema.md`。

## 3. 关联关系（典型查询路径）

### 3.1 从流程实例到待办

```text
ProcessInstance
  -> active ExecutionInstance(s)
  -> active ActivityInstance(s)
  -> TaskInstance(s)  (userTask)
  -> TaskAssigneeInstance(s) (候选人/候选组/组织岗位)
```

### 3.2 从 Deployment 到定义

```text
DeploymentInstance
  -> process_definition_content (BPMN XML)
  -> parse & load -> ProcessDefinitionSource -> ProcessDefinition
```

## 4. 多租户（tenant_id）

表结构里大多包含 `tenant_id` 字段；API 也大量提供 tenantId 重载。

同时请求/上下文中还存在一组“特殊 key”（以 `_$_smart_engine_$_` 为前缀）用于传递 tenantId、task 扩展字段等（见 `RequestMapSpecialKeyConstant`）。

建议你的系统在入口层统一封装：

- tenantId 的解析与注入
- 幂等 key 的注入（如果你希望在 start/signal 上做幂等）

---

下一步：

- API 使用与幂等建议：`03-usage/api-guide.md`
- 数据库表结构与索引：`04-persistence/database-schema.md`

