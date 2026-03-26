# 失败语义（异常如何处理）、重试/补偿/告警

生产系统里你最关心的问题是：**某个节点执行失败后，引擎会怎样？我该如何恢复？**

SmartEngine 的关键组件：

- `ExceptionProcessor`：异常统一处理入口（默认 `DefaultExceptionProcessor`）
- `@Retryable`：声明重试策略（见 `core/.../annotation/Retryable`）
- retry 扩展模块：`extension/retry/*`（含 DB 存储）

---

## 1. 异常分类建议（推荐）

- **可重试**：网络超时、外部系统 5xx、资源不足
- **不可重试**：参数校验失败、权限失败、业务规则拒绝
- **需补偿**：已产生副作用（扣款/发货），需要反向补偿

---

## 2. 重试策略

推荐策略：

- 限次（maxAttempts）
- 退避（fixed delay / exponential backoff）
- 可观测（记录每次重试原因与次数）
- 可终止（达到上限后进入人工处理/补偿流程）

`@Retryable` 用法请参考源码与 retry 模块测试。

---

## 3. 补偿与人工干预

你通常需要三种“人工按钮”：

- suspend：暂停实例（停止推进）
- jump：跳过故障节点或跳到修复节点
- markDone：强制标记完成某分支

这些权限在 `ExecutionCommandService` 中提供（见 `03-usage/jump-and-advanced.md`）。

---

## 4. 告警建议

- 失败次数阈值告警（同一流程/同一节点）
- 重试耗尽告警
- 并行 join 长时间等待告警（可能卡死）
- task 超时/SLA 告警

