# Quickstart（DataBase 模式）— 10 分钟跑通（含表结构/待办）

本章目标：在关系型数据库中完成 **部署 → 启动 → 生成 userTask 待办 → 查询/完成任务** 的最小闭环。

> 对应源码参考：`extension/storage/storage-mysql/src/test/java/.../DatabaseBaseTestCase` 与各类 integration tests。

## 1. 引入依赖

```xml
<dependency>
  <groupId>com.alibaba.smart.framework</groupId>
  <artifactId>smart-engine-core</artifactId>
  <version>3.7.0-SNAPSHOT</version>
</dependency>

<!-- DataBase 模式：提供关系库存储实现 + MyBatis SQLMap -->
<dependency>
  <groupId>com.alibaba.smart.framework</groupId>
  <artifactId>smart-engine-extension-storage-mysql</artifactId>
  <version>3.7.0-SNAPSHOT</version>
</dependency>
```

> 注：模块名为 *storage-mysql*，但仓库同时提供 `schema-postgre.sql`，测试 `application-test.xml` 默认也是 PostgreSQL。

## 2. 初始化数据库（MySQL / PostgreSQL）

你需要执行 DDL：

- 核心表：`sql/schema.sql`（MySQL）或 `sql/schema-postgre.sql`（PostgreSQL）
- 增强表（督办/通知/转派/回退等）：`sql/workflow-enhancement-schema-*.sql`
- 索引：`sql/index.sql`
- Retry 扩展（可选）：`extension/retry/retry-mysql/src/main/resources/sql/retry-schema.sql`

本仓库核心表清单：

- `se_deployment_instance`
- `se_process_instance`
- `se_activity_instance`
- `se_task_instance`
- `se_execution_instance`
- `se_task_assignee_instance`
- `se_variable_instance`
- `se_supervision_instance`
- `se_notification_instance`
- `se_task_transfer_record`
- `se_assignee_operation_record`
- `se_process_rollback_record`

详见：`04-persistence/database-schema.md`

## 3. MyBatis / Spring 装配（参考测试配置）

仓库测试给出一个完整 Spring XML（含 PostgreSQL、MyBatis、事务）：  
`extension/storage/storage-mysql/src/test/resources/spring/application-test.xml`

关键点：

- `mybatis-config.xml`：开启下划线转驼峰、注册 typeAliases（entity + param）
- `mapperLocations`：`classpath:mybatis/sqlmap/*.xml`
- DataSource：可选 MySQL / PostgreSQL / H2（测试用）

你可以用 Spring JavaConfig / Spring Boot 等价实现，核心是要确保：

- SQLSessionFactory 能加载到 `mybatis/sqlmap/*.xml`
- 扫描到 `com.alibaba.smart.framework.engine.persister` 下的组件（DAO/Storage/Service）

## 4. 初始化引擎（必设项）

DataBase 模式下，建议至少设置：

- `IdGenerator`：生成实例/任务等 ID（测试使用 `TimeBasedIdGenerator`）
- `InstanceAccessor`：用于 delegation/listener 取业务 bean（测试使用 Spring ApplicationContext）
- `TaskAssigneeDispatcher`：userTask 的分派器（候选人/候选组/组织等），database 模式特别重要

参考测试 `DatabaseBaseTestCase.initProcessConfiguration()`：

```java
ProcessEngineConfiguration cfg = new DefaultProcessEngineConfiguration();
cfg.setIdGenerator(new TimeBasedIdGenerator());
cfg.setInstanceAccessor(new DataBaseAccessService());          // 通常对接 Spring 容器
cfg.setTaskAssigneeDispatcher(new DefaultTaskAssigneeDispatcher());
```

## 5. 一次完整闭环（部署 → 启动 → 查询待办 → 完成）

示例流程（包含 userTask 的 BPMN）可参考测试资源：

- `extension/storage/storage-mysql/src/test/resources/user-task-id-and-group-test.bpmn20.xml`

运行步骤（伪代码）：

```java
SmartEngine engine = new DefaultSmartEngine();
engine.init(cfg);

// 1) 创建 deployment（将 BPMN 文件持久化到 DB，并加载到内存）
DeploymentInstance dep = engine.getDeploymentCommandService()
  .createDeploymentInstance(new CreateDeploymentCommand()
    .setName("demo")
    .setProcessDefinitionContent(bpmnXml)
    .setStatus("active"));

// 2) 启动流程实例
ProcessInstance pi = engine.getProcessCommandService()
  .start(dep.getProcessDefinitionId(), dep.getProcessDefinitionVersion(), request, response);

// 3) 查询待办任务
List<TaskInstance> tasks = engine.getTaskQueryService()
  .findActiveTaskList(new TaskInstanceQueryParam()
    .setProcessInstanceId(pi.getInstanceId()));

// 4) 认领/完成任务（具体方法见 TaskCommandService）
engine.getTaskCommandService().claim(tasks.get(0).getInstanceId(), "userA");
engine.getTaskCommandService().complete(tasks.get(0).getInstanceId(), request);
```

> 注意：上面 Command/Param 的具体字段以源码为准（`com.alibaba.smart.framework.engine.service.param.*`）。不同接口有 tenantId 重载，建议你在多租户场景下统一通过 request special key 或显式 tenantId 传入。

## 6. 下一步建议

- 先把 “user/组织/岗位/候选组” 分派逻辑做成你自己的 `TaskAssigneeDispatcher`：`05-extensibility/user-integration.md`
- 阅读 MyBatis 兼容注意事项（尤其是 PostgreSQL）：`04-persistence/mybatis-notes.md`
- 理解并行网关并发语义：`05-extensibility/concurrency.md`
