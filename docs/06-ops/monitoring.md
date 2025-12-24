# 日志、指标、链路追踪建议

SmartEngine 本身是一个嵌入式组件，监控能力需要你在宿主系统里补齐。

---

## 1. 日志（Logging）

建议：

- 为每次 start/signal/complete 生成 traceId（贯穿业务日志）
- 在 delegation/listener 执行时记录：
  - processInstanceId / executionInstanceId / activityId
  - 业务幂等 key
  - 耗时与异常堆栈

如果你使用 MDC（SLF4J），建议把关键 id 写入 MDC。

---

## 2. 指标（Metrics）

建议你为以下能力暴露指标：

- start/signal/complete TPS
- 执行耗时分布（histogram）
- 并行网关分支数/等待时间
- 重试次数、重试耗尽次数
- task backlog（待办量）

---

## 3. 链路追踪（Tracing）

在 ServiceTask 的委派调用边界是天然 span：

- span.name = processId + activityId
- attributes：processInstanceId, executionInstanceId, tenantId, bizKey

可用 OpenTelemetry / SkyWalking / Zipkin 等实现。

---

## 4. 数据库观测

DataBase 模式建议：

- 打开慢 SQL 日志
- 为关键 SQL 做 explain analyze
- 关注锁等待（并行 join 时更明显）

