# 建模指南：网关 / 并行 / join / receiveTask / userTask

SmartEngine 支持的 BPMN 子集与执行语义偏“工程落地”，建议你在建模时遵循以下规则，避免上线后出现：

- 并行 join 卡死
- ReceiveTask 无法推进
- userTask 任务分派不一致
- 变量作用域混乱

---

## 1. 基础规则（强烈建议）

### 1.1 每个流程必须具备可追踪的唯一标识

- processId（BPMN process id）建议稳定、可版本化
- 建议把“业务幂等 key / bizUniqueId”作为变量或扩展字段，贯穿 start/signal

### 1.2 把“业务执行”放在 ServiceTask，把“等待外部事件”放在 ReceiveTask

- ServiceTask：同步调用你的业务逻辑（委派/脚本/规则）
- ReceiveTask：等待外部 signal（回调、消息、人工确认、异步补偿）

不要在 ServiceTask 里“自旋等待外部事件”。

---

## 2. 网关与并发

### 2.1 ExclusiveGateway（互斥网关）

- 条件表达式应当是纯函数（只依赖变量）
- 避免在条件里调用外部系统（会导致不可重放、不可测试）

### 2.2 ParallelGateway（并行网关）

并行网关会造成：

- execution 数量增加
- join 需要等待全部分支到达

建议：

- 分支数量可控（避免“数据驱动产生大量分支”）
- 每个分支尽量短小，避免长事务
- 在 DataBase 模式下确保索引完备（见 `04-persistence/database-schema.md`）

并发专题：`05-extensibility/concurrency.md`

### 2.3 InclusiveGateway（包容网关）

- 包容网关 join 更复杂：不是“所有分支”，而是“实际激活的分支”
- 建议你先跑仓库对应测试并理解树结构（`gateway/tree/*`）

---

## 3. ReceiveTask 的正确用法

### 3.1 何时会进入等待状态？

当 execution 推进到 ReceiveTask：

- activityInstance 进入等待状态
- executionInstance 通常处于 active 但无法继续推进

### 3.2 你需要怎么 signal？

- 你必须保存 “要 signal 哪个 execution / process” 的关联信息
- 推荐做法：
  - 把 executionInstanceId 存入你的业务表（或事件 payload）
  - 外部事件到达后用 `ExecutionCommandService.signal(executionInstanceId, request)` 推进

---

## 4. userTask 建模（DataBase 模式）

userTask 的关键不在 BPMN，而在“分派规则与组织模型”：

- candidateUser / candidateGroup / org / position 等分派语义
- 认领/完成/转派/会签等操作语义

建议：

- 把分派逻辑收敛到 `TaskAssigneeDispatcher`（统一入口）
- userTask 的业务扩展字段通过 request special key 传入（见 `RequestMapSpecialKeyConstant`）

详见：`05-extensibility/user-integration.md`

---

## 5. 流程跳转与人工干预（慎用）

建模阶段尽量避免“依赖跳转才能跑通的流程”。跳转通常用于：

- 线上故障补偿
- 人工干预修复
- 迁移/升级时的临时手段

详见：`03-usage/jump-and-advanced.md`

