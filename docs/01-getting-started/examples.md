# 示例清单（Examples）

SmartEngine 的“示例”主要以 **单元测试 / 集成测试** 的形式存在。你可以把它们当成：

- 可运行的用法样例（示范如何 init / deploy / start / signal / query）
- 对执行语义的可验证说明（并行网关、跳转、回退、兼容性等）

下表列出最常用的一组示例入口（建议从上到下跑）：

| 场景 | BPMN 文件 | 关联测试（建议直接打开阅读） |
|---|---|---|
| 并行网关 + ReceiveTask 并发推进 | `test-receivetask-parallel-gateway.bpmn20.xml` | storage-custom: BasicParallelGatewayTest, ConcurrentParallelGatewayTest<br/>storage-mysql: ConcurrentParallelGatewayDBTest, ConcurrentParallelGatewayDBWithTenantIdTest |
| 并行网关 + 全 ServiceTask 编排 | `test-all-servicetask-parallel-gateway.bpmn20.xml` | storage-custom: ServiceOrchestrationParallelGateway* 系列测试 |
| 流程跳转 jumpTo/jumpFrom | `smart-engine/jump1.bpmn20.xml / jump2.bpmn20.xml` | storage-custom: JumpFreeNode1Test, JumpFreeNode2Test |
| CallActivity（父子流程/并行场景） | `parent-callactivity-process.bpmn20.xml + child-callactivity-process.bpmn20.xml` | storage-mysql: callactivity 相关测试资源与用例 |
| userTask 待办 + 分派（用户/组） | `user-task-id-and-group-test.bpmn20.xml` | storage-mysql: TaskServiceTest, TaskServiceWithTenantTest |
| 变量服务（增删改查/持久化） | `user-task-id-and-group-test.bpmn20.xml` | storage-mysql: VariableServiceTest, VariableServiceWithTenantTest |
| 命名空间兼容（Camunda/Flowable/Activiti） | `core/src/test/resources/process-def/extension/*.bpmn20.xml` | core: ExtensionNameSpaceParseTest 等 |
| 扩展属性/扩展元素解析 | `core/src/test/resources/process-def/extend/extend.bpmn20.xml` | core: ExtenstionTest 等 |
| 消息队列/ReceiveTask 场景 | `message-queue-receive-task.bpmn20.xml` | storage-mysql: process 相关测试 |
| 会签/多实例（multi-instance） | `multi-instance-test.bpmn20.xml / multi-instance-activiti-compatiable-test.bpmn20.xml` | storage-mysql: multi-instance 相关测试 |

> 备注：测试类具体路径请在源码中按类名搜索。DataBase 模式多数测试需要本地数据库（参考 `storage-mysql/src/test/resources/spring/application-test.xml`）。
