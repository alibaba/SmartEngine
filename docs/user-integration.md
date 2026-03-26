# 用户/组织/待办分派（DataBase 模式）

DataBase 模式提供了 userTask 的待办表结构，但“谁可以办”必须与你的组织/IAM 模型对齐。

SmartEngine 提供两个关键扩展点：

- InstanceAccessor：如何拿到业务系统的 bean/服务
- TaskAssigneeDispatcher：如何从 BPMN/变量/业务规则生成 assignee 记录

---

## 1. InstanceAccessor：引擎如何拿你的业务服务

典型实现：

- 在 Spring 环境中：通过 ApplicationContext.getBean(name)
- 在非 Spring 环境中：通过自建容器或反射 newInstance

你需要保证：

- delegation/listener/className 能映射到可用实例
- bean 生命周期可控（单例/原型）

---

## 2. TaskAssigneeDispatcher：分派的唯一入口（强烈建议）

DataBase 模式下，userTask 的分派信息最终会落到：

- `se_task_assignee_instance`

Dispatcher 的职责：

- 解析 BPMN 声明的候选规则（user/group/org/position）
- 结合流程变量与业务规则，生成最终 assignee 列表
- 支持动态规则：例如按门店/区域/角色/值班表分派

仓库默认实现：`DefaultTaskAssigneeDispatcher`

你可以做的增强：

- 对接你的 IAM：把 role/org/position 映射成 userId 列表
- 支持“排班/轮转”：把候选人计算交给你的排班服务
- 支持 SLA/优先级：分派时写入 task 扩展字段

---

## 3. 待办查询与权限

生产里“待办查询”通常不是简单按 userId：

- 个人：userId
- 组：groupId
- 组织：orgId
- 岗位：positionId
- 角色：roleId
- 组合：多条件 + 优先级/标签/时间

建议：

- 引擎表负责“写入最小真相”
- 复杂查询在业务系统侧做 read model（例如任务视图表），避免对引擎表做复杂 join

---

## 4. 任务操作语义（建议一致化）

在你的系统侧建议统一定义操作：

- claim（认领）
- unclaim（释放）
- complete（完成）
- transfer（转派）
- addAssignee/removeAssignee（会签/加签/减签）
- rollback（回退）

并与增强表对齐（转派/回退/操作记录）。

