# 3.7.0-SNAPSHOT
1. [新功能] 新增数据库分片支持，包含索引表和雪花 ID 生成器
2. [新功能] 新增 suspend/resume/claim API 到 SmartEngine 命令服务
3. [新功能] 新增数据归档模块，支持 MySQL、Oracle 和 PostgreSQL
4. [新功能] 新增 task_instance 动态查询支持，包含 domain_code 和 extra JSON 字段过滤
5. [新功能] 新增链式查询 API，支持流式查询操作
6. [新功能] 增强流式查询 API，支持高级过滤和分页
7. [新功能][实验性] 新增数据库方言支持 - Oracle、DM（达梦）、PostgreSQL、MySQL、OceanBase、Kingbase（人大金仓）、SQL Server、H2
8. [新功能] 新增 SmartIdTypeHandler，支持多种 ID 类型
9. [新功能] 新增 NotificationQueryService 和 SupervisionQueryService
10. [新功能] 新增 NotificationCommandService 和 SupervisionCommandService
11. [新功能] 支持双存储模式（MEMORY + CUSTOM）
12. [新功能] 新增 EventBasedGateway 和 IntermediateCatchEvent 支持，实现事件驱动分支
13. [新功能] 新增 TaskEventPublisher SPI，支持任务生命周期事件（TASK_CREATED、TASK_ASSIGNED、TASK_CLAIMED、TASK_COMPLETED、TASK_CANCELED、TASK_TRANSFERRED、TASK_DELEGATED、TASK_REVOKED）
14. [新功能] 新增任务操作记录表：转办记录、加减签操作记录、流程回退记录
15. [新功能] 新增流程回退功能，通过 TaskCommandService#rollbackTask 实现
16. [新功能] 为 se_process_instance 新增 complete_time 字段，精确记录流程完成时间
17. [非兼容变更] StorageMode.MEMORY 重命名为 StorageMode.CUSTOM
18. [重构] 废弃旧的 QueryService 方法，推荐使用流式查询 API
19. [BUG 修复] 修复任务操作记录未生成 ID 导致 NOT NULL 约束违反的问题
20. [BUG 修复] 将 Java 9+ 的 Map.of()/List.of() 替换为 Java 8 兼容代码
21. [BUG 修复] 修复 H2 测试 Schema：补充缺失的 complete_time 列和 7 张缺失的表
22. [改进] 将 dual 和 archive 模块的测试数据源从 PostgreSQL 切换为内嵌 H2，提升 CI 可移植性
23. [文档] 全面更新文档，包含 API 指南、架构说明和数据库 Schema

# 3.6.0-SNAPSHOT
1. [功能增强] 增加多租户特性,谢谢  @yanricheng1 提交的 PR
2. [模块调整] 移除 MongoDB Storage

# 3.5.0
1. [功能增强] 完整支持了包容网关,包括 default seqFlow,嵌套包容网关和 Unbalanced gateway
2. [细节重构] 重构了包容和并行以及互斥网关的处理逻辑,减少了一些重复代码
3. [微型非兼容升级] 为了支持包容网关，在`se_execution_instance`表增加了 `block_id`字段
4. [BUG 修复] 修复嵌套并行网关功能在某些场景下的异常问题
5. [微型非兼容升级] 修改了 VariablePersister#deserialize 方法签名,允许更加灵活的反序列化方式

# 3.0.0
1. 重点完善对嵌套并行网关,并行网关嵌套子流程在多线程情况下的测试.
2. CallActivity:隔离父子流程的request和response,业务上如果有需要共享的,可以在第一个context里面手动从parentContext获取.
3. 增加 RetryServiceTaskTest

# 2.6.4
1. [微型非兼容升级] 修改IdGenerator 的实现策略, 现在除了实现id策略后,还需要手动给instance 赋值才生效 (使用上有部分退化,为了后续使用表自增主键做准备,以及支持细粒度的id生成策略). 
2. [微型非兼容升级] 修改TaskCommandService#complete的返回值,从void类型改为ProcessInstance

# 2.6.3
1. 微改进,支持了互斥网关的 DefaultSequenceFlow 特性;重构了并行网关下的并发服务编排的逻辑,对可读性做了重点优化.

# 2.6.2
1. [微型非兼容升级]修改TaskAssigneeDispatcher#getTaskAssigneeCandidateInstance 第二个参数,将Map类型修改为Context,便于获得流程定义参数.

# 2.6.1
1. 微改进,允许在执行execute方法时,调用hook逻辑,在特定业务场景下,取消流程环节的推进.

# 2.6.0
1. BUG修复:针对多线程情况下,使用并行网关进行服务编排导致内存泄露的bug. 强烈建议使用该场景的用户进行升级.

# 2.5.7.2
1. 微改进,支持根据流程实例的开始,结束时间进行查询;支持发送流程结束事件.

# 2.5.7.1
1. 微改进,优化了ACTIVITY_START的触发时机,在节点创建后,再触发该事件.

# 2.5.7.0
1. [非兼容升级]仅针对Properties属性,增加PropertyCompositeValue. 

# 2.5.6.x
1. 为分库分表做准备，增加了signal 查询pid参数。

# 2.5.1
1.  [非兼容升级] LockStrategy: 目前流程流转和并行网关join处理都复用了同一个LockStrategy。 这次变更，主要是加入了Context参数，运行外部场景根据context的环节id等值，做些细粒度的锁的策略控制。
 另外，还解决了LockStrategy的子流程和并行网关兼容性问题。

# 2.5.0
1. 支持了并行网关的 anyof 模式，见`ServiceOrchestrationParallelGatewayTest`。

# 2.5.0
1. 支持了Camunda Modeler（使用 https://github.com/camunda/camunda-modeler 4.0.0 这个tag 版本）。可以直接用Camunda Modeler 绘制，导出，然后在系统中无缝使用。   
在这个版本发布中，我们兼容了  smart:class,smart:properties,smart:eventListener 这几个attributes。 

# 2.2.2
0. 完善了父子流程，并行网关的并发服务编排逻辑。
1. [非兼容升级]去掉Mvel表达式引擎的COC约定，改为通过init 传入。两个好处：提升性能，减少多ClassLoader带来的问题。
2. 增加ParallelServiceOrchestration默认实现，支持外部业务灵活扩展。
3. 增加节点多条OutComingTransition来模拟简单的互斥网关，减少画图复杂度。

# 2.2.3
0. BUG修复：针对数据库模式，为se_process_instance 新增了parent_execution_instance_id 字段，用于解决子流程结束后，父流程不自动执行的问题。

# 2.2.3.1
0. 修复transfer 无法正常工作，添加了test作为保障 （果然是不加test，就可能要踩坑啊）
