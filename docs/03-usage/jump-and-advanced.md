# 跳转、会签、分派、VariablePersister 等高级特性

本章聚焦“生产系统一定会遇到，但不是入门必需”的权限：

- jumpTo / jumpFrom：流程跳转（人工干预、补偿、迁移）
- markDone：标记完成
- userTask 分派：候选人/候选组/组织/岗位/会签
- VariablePersister：变量持久化策略
- Retry：失败重试扩展

---

## 1. jumpTo / jumpFrom：流程跳转

入口在 `ExecutionCommandService`（签名见 `03-usage/api-guide.md`）。

典型用法：

- **跳到某个节点**：线上修复，跳过失败节点
- **从某个节点跳出**：跳过一段流程，直接进入下游

强烈建议：

- 跳转前先冻结流程实例（suspend）或做并发控制
- 跳转行为本质是“改变执行图”，请记录审计（建议写入你自己的操作日志表）
- 跳转后要处理：
  - 未完成的任务如何关闭
  - 变量如何清理/补齐

仓库示例：`storage-custom/src/test/java/.../jump/*`

---

## 2. markDone：人工标记完成

用于：

- 外部系统已经完成动作，但引擎推进卡住
- 需要人工强制结束某分支

注意：

- markDone 与 jump 一样需要审计
- DataBase 模式下要同步处理任务表/分派表状态

---

## 3. userTask 分派与会签（DataBase 模式）

### 3.1 分派入口：TaskAssigneeDispatcher

推荐你把所有“谁可以办这个任务”的规则收敛到一个实现类里：

- candidateUser / candidateGroup
- org / position
- 动态规则（按变量、按角色、按业务字段）

然后在 engine 配置中注入：

```java
cfg.setTaskAssigneeDispatcher(new YourTaskAssigneeDispatcher());
```

### 3.2 会签（multi-instance）

仓库存在 multi-instance 相关测试资源，建议你先跑通并理解：

- 会签是产生多个 taskInstance，还是一个 task + 多个 assignee？
- 通过条件/计数何时结束会签？

---

## 4. VariablePersister：控制变量写库

你可以通过自定义 `VariablePersister`：

- 控制哪些变量落库
- 控制序列化格式（JSON/JSONB/加密）
- 控制变量清理策略（只保留最新、只保留白名单）

---

## 5. Retry：失败重试

SmartEngine 提供：

- `ExceptionProcessor`：统一异常入口
- `@Retryable`：声明重试策略
- retry 扩展模块：`extension/retry/*`（含 DB 存储实现与 schema）

生产建议：

- 重试要可观测（日志/指标/告警）
- 重试要可停止（熔断、最大次数）
- 对外部系统调用做幂等（避免重复扣款/重复发货）

详见：`06-ops/failure-handling.md`

