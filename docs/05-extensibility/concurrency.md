# 并发：ParallelGateway 的 LockStrategy / ExecutorService

并行网关是 SmartEngine 最“工程化”的部分之一：它既是 BPMN 语义，又会在生产里遇到真实的并发问题。

本章解释：

- 并行分叉/汇聚时执行实例如何变化
- 引擎如何选择线程池、如何等待、如何避免重复推进
- 你应该如何在集群/多线程下配置与扩展

---

## 1. ParallelGatewayUtil：并行的“配置解释器”

关键类：`core/.../util/ParallelGatewayUtil.java`

它负责从节点 properties 中解析：

- 使用哪个 ExecutorService（默认或指定 poolName）
- latch 的等待时间
- 并发相关的开关与参数

意味着：你可以在 BPMN 中通过扩展属性，为不同并行网关指定不同的线程池策略。

---

## 2. ExecutorService poolsMap：多线程池治理

`ProcessEngineConfiguration` 中存在：

- `Map<String, ExecutorService> getExecutorServiceMap()`（或同等配置项）

常见策略：

- 默认线程池：用于大多数并行网关
- 专用线程池：用于“重任务”分支（外部调用、IO、耗时计算）
- 限流：通过线程池大小/队列大小控制系统背压

---

## 3. LockStrategy（Deprecated）还能用吗？

`LockStrategy` 接口在源码中标记为 Deprecated，原因通常是：

- 锁粒度太细或语义不清
- 在集群环境下无法保证正确性（需要分布式锁/事务锁）

建议：

- 不再依赖 LockStrategy 做核心正确性
- 采用数据库层面的行锁/乐观锁（DataBase 模式）
- 或采用“幂等推进 + 去重”（Custom 模式）

---

## 4. 生产建议（最重要）

- 并行网关 join 的正确性优先于性能
- 任何并行推进都要有幂等保障（避免重复执行业务 side effect）
- 外部调用建议：
  - 幂等 + 重试 + 熔断
  - 失败可补偿（compensation）

仓库里并行相关的测试非常多，建议你把它们当作“语义回归集”：

- storage-custom：ConcurrentParallelGatewayTest 等
- storage-mysql：ConcurrentParallelGatewayDBTest 等

