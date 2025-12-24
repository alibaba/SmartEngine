# 变量与上下文：作用域、序列化、持久化策略

SmartEngine 的变量体系主要用于：

- 业务数据在流程节点间流转
- 网关条件表达式求值
- 外部 signal 携带上下文推进流程

本章说明变量如何进入引擎、如何持久化、以及你需要注意的“特殊 key”。

---

## 1. 变量从哪里来？

常见入口有三类：

1) start 时传入 `request` map  
2) signal 时传入 `request` map  
3) 任务完成/认领等操作的 request map

这些 map 会进入 `ExecutionContext`，并由 `VariableCommandService`/`VariablePersister` 决定是否持久化。

## 2. 变量的持久化与序列化

- `VariablePersister`：决定把哪些变量写入 `se_variable_instance`
- 默认实现：`DefaultVariablePersister`

建议：

- 只持久化“必要变量”（用于恢复推进、用于条件判断、用于审计）
- 大对象（复杂 JSON）建议存引用（如对象存储 key），避免表膨胀
- 统一序列化策略（JSON / JSONB）并在 MyBatis typeHandler 中固定

## 3. 作用域（Scope）

SmartEngine 的变量作用域通常至少区分：

- processInstance 级（全局）
- execution/activity 级（分支/节点局部）
- task 级（userTask 表单数据）

具体字段与实现请参考 `VariableInstance` 与 `VariableCommandService`。

## 4. RequestMapSpecialKeyConstant（非常重要）

仓库定义了一组“特殊 key”，用于传递：

- tenantId
- task 相关扩展字段（如候选人、组、组织等）
- 以及一些引擎内部控制字段

代码位置：`core/.../constant/RequestMapSpecialKeyConstant.java`

建议你的系统在入口层做：

- 白名单化：只允许特定 key 进入引擎
- 命名空间隔离：业务变量不要使用 `_$_smart_engine_$_` 前缀

## 5. 常见坑

- **同名变量覆盖**：不同节点写同名 key，会覆盖旧值（注意作用域）
- **类型不一致**：同一 key 有时是 String、有时是 Long，会导致网关条件判断异常、或 MyBatis 写库异常
- **PostgreSQL 类型坑**：如果 variable 表列是 jsonb/bytea，typeHandler 要严格匹配（见 `04-persistence/mybatis-notes.md`）

