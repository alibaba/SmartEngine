# FAQ

## 1. Custom vs DataBase 模式怎么选？

优先按“你想让引擎负责多少事情”来选：

- 选 **Custom**：你已经有自己的任务/用户/变量/存储体系，或者希望完全控制持久化与事务；SmartEngine 只负责“解析 + 执行 + 并发语义 + 行为扩展”。
- 选 **DataBase**：你需要一套开箱即用的关系库存储，尤其是 userTask 待办、任务分派、变量持久化、督办/通知等增强权限。

详见：`02-concepts/modes.md`

## 2. 集群环境下怎么保证流程定义一致性？

`RepositoryCommandService` 的 deploy 会把 BPMN 解析结果加载到**本地内存**（单机）。集群下你需要自行实现：

- **统一部署源**：例如将 BPMN 存储到数据库（DataBase 模式的 `DeploymentInstance`）、配置中心、对象存储等。
- **一致性策略**：常见做法：
  - 每个节点启动时加载一遍全部 active deployment（冷启动一致）。
  - 通过消息/事件（如 Kafka、Redis pubsub）通知各节点 reload（热更新一致）。
  - 给流程定义增加版本号与缓存 key，确保滚动发布时“新旧版本可并存”。

## 3. 并行网关 join 的语义是什么？会不会线程不安全？

SmartEngine 并行网关在运行期会出现多个 ExecutionInstance；join 的关键在于：

- 如何判断“所有分支都到达 join”；
- 以及如何避免多线程同时通过 join 造成重复推进。

源码中与并行相关的关键点：

- `com.alibaba.smart.framework.engine.util.ParallelGatewayUtil`：线程池选择、latch 等控制参数解析
- `ParallelGatewayBehavior` & 相关常量：并行网关行为与属性
- `LockStrategy`：旧的锁扩展点（Deprecated）

建议阅读：`02-concepts/execution-model.md`、`05-extensibility/concurrency.md`。

## 4. Postgres / MySQL 兼容如何处理？类型坑多吗？

仓库提供 **MySQL 与 PostgreSQL 两套 DDL**：

- `extension/storage/storage-mysql/src/main/resources/sql/schema.sql`
- `extension/storage/storage-mysql/src/main/resources/sql/schema-postgre.sql`
- 以及工作流增强表：`workflow-enhancement-schema-*.sql`

但在运行时仍需注意：

- MyBatis 参数类型与列类型不一致会触发 Postgres 的 `operator does not exist`（例如 bigint vs varchar）；
- boolean / smallint 映射差异；
- in (...) 参数展开与 jdbcType/类型处理器。

详见：`04-persistence/mybatis-notes.md`。

## 5. 变量（Variable）持久化是怎么做的？作用域是什么？

变量相关接口集中在：

- Command：`VariableCommandService`
- Query：`VariableQueryService`
- 持久化策略：`VariablePersister`（默认 `DefaultVariablePersister`）

并且 Request/Context 中存在一组“特殊 key”（如 tenantId、task 扩展字段等），见 `RequestMapSpecialKeyConstant`。详见：`03-usage/variables.md`。

## 6. 引擎异常发生后会怎样？有没有重试权限？

- 引擎层面的异常处理由 `ExceptionProcessor` 统一入口处理（默认 `DefaultExceptionProcessor`）。
- 仓库提供 retry 扩展模块（`extension/retry/*`），并有 `@Retryable` 注解用于声明最大次数与 delay。

详见：`06-ops/failure-handling.md` 与 `05-extensibility/overview.md`。

## 7. SmartEngine 是否必须依赖 Spring Boot？

不必须。核心是纯 Java 组件；测试里既有纯 JUnit，也有 Spring XML 配置的示例（例如 `storage-mysql/src/test/resources/spring/application-test.xml`）。  
你可以在 Spring Boot 中更方便地装配 DataSource/MyBatis/Bean 扫描，但不是必选项。

