# 性能模型、压测建议、瓶颈定位

SmartEngine 的性能瓶颈主要来自三部分：

1) 引擎推进频率（节点数量、并行分支数、signal 次数）
2) 存储写入与查询（DataBase 模式：MyBatis + DB）
3) 外部业务调用（ServiceTask 委派调用、外部系统交互）

---

## 1. 影响性能的关键因子

- 并行网关分支数：execution 数量成倍增加
- userTask 数量：task/assignee 写入量增加
- 变量大小：variable 表写入与序列化成本增加
- 查询模式：是否做复杂 join、是否分页、是否按 tenantId 索引

---

## 2. 压测建议（最小可行）

### 2.1 指标

- start TPS、signal TPS、complete TPS
- 平均推进耗时 / P95 / P99
- DB：QPS、慢查询、锁等待
- 线程池：队列积压、拒绝数

### 2.2 场景

- 顺序流程（基线）
- 并行分叉 + join（并发正确性 + 性能）
- ReceiveTask + 外部 signal（事件驱动）
- userTask 大量待办（查询与分页）

---

## 3. DataBase 模式的优化点

- 索引：按 tenantId + status + gmt_modified 组合索引
- 批量写入：尽量减少频繁 update
- 查询下沉：复杂待办查询做 read model

---

## 4. Custom 模式的优化点

- 存储接口实现：避免“每推进一步都落库多表”
- 事件日志：用 append-only 模式减少写放大
- 幂等：避免重复推进导致的额外调用

