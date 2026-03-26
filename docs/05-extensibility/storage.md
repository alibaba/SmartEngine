# 自定义存储实现：实例/变量/任务/活动

如果你要把 SmartEngine 深度嵌入你的业务系统，最常见的二开需求就是：

- 用你自己的数据库（或多租户 schema）
- 增加业务字段
- 增加历史表/归档表
- 做读写分离/复杂查询

这些都通过 Storage 接口完成。

---

## 1. Storage 接口在哪里？

`core/.../instance/storage/*`

每个接口对应一个“领域对象族”的持久化：

- Process / Execution / Activity
- Task / Assignee
- Variable
- Deployment / Definition
- Notification / Supervision（增强）

---

## 2. 参考实现：DataBase 模式

模块：`extension/storage/storage-mysql`

实现特点：

- RelationshipDatabase*Storage 作为 Storage 层
- DAO + SQLMap 作为数据访问层
- schema.sql / schema-postgre.sql 作为 DDL

如果你要适配 PostgreSQL/MySQL 双栈：

- 建议把“SQL 方言差异”收敛到 MyBatis provider 或专用 SQLMap
- 并为关键查询（待办、并发 join）做双库回归测试

---

## 3. 参考实现：Custom 模式

模块：`extension/storage/storage-custom`

实现特点：

- PersisterSession（线程内）做存根
- 主要用于测试与理解接口


---

## 4. 设计建议（生产）

- 写入路径：尽量少的表更新、明确索引
- 查询路径：不要依赖“引擎核心表”支撑所有复杂查询
  - 复杂待办建议做 read model（CQRS Query 侧）
- 数据保留：没有 history 表时，建议自己补齐“历史事件/操作日志”
- 多租户：建议把 tenantId 作为一级过滤条件，并建立复合索引（tenant_id + status + gmt_modified 等）

