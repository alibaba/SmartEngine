# 术语表（Glossary）

本术语表以 SmartEngine 源码命名为准（核心 model 在 `core/src/main/java/com/alibaba/smart/framework/engine/model`）。

## 流程与定义类

- **Process Definition（流程定义）**：BPMN XML 解析后在引擎内存中的模型。对应 `model/assembly/ProcessDefinition`。
- **ProcessDefinitionId / Version**：流程定义的标识与版本（在 API 中经常同时出现）。
- **ProcessDefinitionSource**：部署/解析后返回的容器对象，包含 definition 及元信息。对应 `model/assembly/ProcessDefinitionSource`。
- **DeploymentInstance（部署实例）**：DataBase 模式下，流程定义文件的持久化记录（数据库中保存 BPMN 内容等），并负责触发 Repository 解析加载。对应 `model/instance/DeploymentInstance`，表 `se_deployment_instance`。

## 运行期实例类

- **ProcessInstance（流程实例）**：一次流程运行的顶层实例。对应 `model/instance/ProcessInstance`，表 `se_process_instance`。
- **ExecutionInstance（执行实例）**：更细粒度的“token/执行上下文”载体；并行网关分叉后会出现多个活跃 execution。对应 `model/instance/ExecutionInstance`，表 `se_execution_instance`。
- **ActivityInstance（活动实例）**：某个 BPMN 节点（activity）在运行期的实例化记录。对应 `model/instance/ActivityInstance`，表 `se_activity_instance`。
- **TaskInstance（任务实例）**：主要对应 userTask 的待办/任务记录（DataBase 模式增强能力）。对应 `model/instance/TaskInstance`，表 `se_task_instance`。
- **TaskAssigneeInstance（任务分派实例）**：任务的处理人/候选人/候选组等分派记录。对应 `model/instance/TaskAssigneeInstance` 等，表 `se_task_assignee_instance`。
- **VariableInstance（变量实例）**：持久化的流程变量记录。对应 `model/instance/VariableInstance`，表 `se_variable_instance`。
- **TransitionInstance（流转实例）**：描述从一个节点到另一个节点的流转轨迹（更偏运行期内部）。对应 `model/instance/TransitionInstance`。

## 行为与执行

- **ActivityBehavior（节点行为）**：每类 BPMN activity 的执行逻辑（ServiceTask/ReceiveTask/UserTask/网关等），位于 `engine/behavior/*`。
- **Delegation（委派执行）**：SmartEngine 对“调用业务代码”的抽象，常见于 `smart:class` / ServiceTask。由 `DelegationExecutor` 执行。
- **Listener（监听器）**：节点/执行监听扩展点（ExecutionListener 等），由 `ListenerExecutor` 执行。
- **ExecutionContext（执行上下文）**：信号、变量、租户、扩展字段等运行上下文承载体。见 `engine/context/*`。

## 模式与存储

- **Custom 模式**：通过 `InstanceAccessor` 等机制把引擎与业务系统的 bean/类加载集成，并可使用自定义存储实现（参考 `extension/storage/storage-custom`）。
- **DataBase 模式**：使用 `extension/storage/storage-mysql` 提供的关系库存储实现（含 `schema.sql` / `schema-postgre.sql` 与 MyBatis SQLMap）。
- **InstanceAccessor**：引擎获取“业务对象/bean/类实例”的统一入口。Custom 与 DataBase 测试里都给出示例实现。
- **Storage（存储接口）**：`core/.../instance/storage/*` 定义流程实例、执行实例、变量、任务等持久化接口；不同模块提供实现。

## 并发与网关

- **Parallel Gateway（并行网关）**：分叉会创建多个 execution；join 需要等待全部到达点，涉及并发控制与线程池选择（见 `util/ParallelGatewayUtil`）。
- **LockStrategy（锁策略）**：较早的锁扩展点，源码标记为 Deprecated（粒度太小，不建议继续扩展）。
- **ExecutorService / poolsMap**：并行网关可选择默认线程池或按节点 properties 指定线程池。

## 工作流增强（Enhancement）

- **NotificationInstance（通知实例）**：通知类记录（DataBase 模式），表 `se_notification_instance`。
- **SupervisionInstance（督办实例）**：督办类记录（DataBase 模式），表 `se_supervision_instance`。
- **Transfer / Rollback Record**：任务转派、回退等增强表，位于 `workflow-enhancement-schema-*.sql`。

