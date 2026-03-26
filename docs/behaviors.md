# 节点行为扩展：ServiceTask / ReceiveTask / 网关…

SmartEngine 的执行核心抽象是 `ActivityBehavior`：

- 每种 BPMN 节点类型对应一个 behavior
- behavior 可以通过扩展机制替换/覆盖

---

## 1. 内置行为在哪？

- BPMN 行为（事件、网关、sequence flow 等）：`core/.../engine/bpmn/behavior/*`
- Task 行为（ServiceTask/ReceiveTask/UserTask 等）：`core/.../engine/behavior/impl/*`

---

## 2. 如何覆盖某类节点行为？

### 2.1 使用 ExtensionBinding 绑定

- group：`ExtensionConstant.ACTIVITY_BEHAVIOR`
- bindKey：节点类型（例如 serviceTask）

实现类示意：

```java
@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = "serviceTask", priority = 100)
public class YourServiceTaskBehavior extends ServiceTaskBehavior {
  @Override
  public void execute(...) { ... }
}
```

### 2.2 你应该优先“组合”而不是“重写全部”

常见推荐做法：

- 在执行前后加埋点、权限校验、幂等校验
- 委派调用仍复用默认 DelegationExecutor
- 异常仍交给 ExceptionProcessor

---

## 3. Delegation（业务委派）怎么对接？

ServiceTask 等节点通常最终会走 `DelegationExecutor`：

- 解析阶段：从 smart:class / 其他扩展属性得到 class/beanName
- 执行阶段：通过 InstanceAccessor 获取实例并调用

你可以替换：

- `InstanceAccessor`（决定如何拿实例）
- `DelegationExecutor`（决定如何调用、是否异步、是否熔断）

---

## 4. 网关行为扩展注意事项

- Exclusive/Inclusive/Parallel 的语义复杂，建议尽量不要“重写语义”
- 如果你确实要扩展：
  - 建议只在“条件表达式求值、并发控制、线程池选择”层面做增强
  - 并配套跑通仓库 gateway/tree 的测试

并发专题见：`05-extensibility/concurrency.md`

