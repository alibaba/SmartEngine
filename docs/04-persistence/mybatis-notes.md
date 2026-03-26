# MyBatis SQL 兼容、类型坑、分页/复杂查询扩展建议

DataBase 模式使用 MyBatis（SQLMap）实现持久化，常见问题集中在：

- MySQL ↔ PostgreSQL 的类型差异（bigint/varchar/boolean/json/jsonb）
- 动态 SQL 的参数类型推断
- in (...) 参数展开与 JDBC 类型
- 分页语法（limit/offset）与复杂查询扩展

SQLMap 位置：`extension/storage/storage-mysql/src/main/resources/mybatis/sqlmap/`

当前 SQLMap 文件：

- `activity_instance.xml`
- `deployment_instance.xml`
- `execution_instance.xml`
- `notification_instance.xml`
- `process_instance.xml`
- `supervision_instance.xml`
- `task_assignee_instance.xml`
- `task_instance.xml`
- `variable_instance.xml`

---

## 1. PostgreSQL 最常见报错：operator does not exist

典型错误（你在迁移时很容易遇到）：

- `bigint = character varying`
- `smallint = boolean`

根因：

- MyBatis 绑定参数时按 Java 类型推断 JDBC 类型
- PostgreSQL 对类型更严格（不会像 MySQL 那样隐式转换）

解决建议（优先级从高到低）：

1) **保证 Java 参数类型与列类型一致**（最佳）  
2) 在 XML 中显式指定 `jdbcType`（例如 BIGINT / VARCHAR / BOOLEAN）  
3) 必要时在 SQL 中显式 cast（PostgreSQL `::bigint` / `CAST(... AS bigint)`）  
4) 为 json/jsonb/枚举等使用自定义 `TypeHandler`

> 你之前遇到的 `id in (?)` + `bigint = varchar`，本质也是同类问题：in 参数被当成 String 绑定了。

---

## 2. boolean / smallint 映射

仓库 PostgreSQL DDL 中，部分状态字段使用 `smallint`（0/1），而 Java 可能用 Boolean。

建议：

- 状态字段统一使用 Integer/Short（并用枚举封装）
- 或使用 TypeHandler 显式映射（Boolean ↔ smallint）

---

## 3. in (...) 参数与集合绑定

如果你需要 `WHERE id IN (...)`：

- 用 MyBatis `<foreach>` 展开集合
- 确保集合元素类型与列类型一致（Long 列就传 Long 集合）

---

## 4. 分页与复杂查询扩展

仓库的 QueryParam 多为简单字段过滤。生产常见扩展：

- 多维度待办过滤：人员/组/组织/岗位/优先级/标签/时间区间…
- 组合条件与排序（优先级 + 更新时间 + SLA）
- 按业务字段 join（例如按订单号联查）

建议做法：

- 不要把所有查询都塞进引擎模块，保留“引擎最小查询集”
- 复杂查询在业务系统侧自建 read model（CQRS 的 Query 侧）
  - 例如把 task_instance 与 assignee_instance join 后落到一张 denormalized 表
  - 或用 ES/ClickHouse 做全文与聚合

---

## 5. SQLMap 维护建议

- 为每个 SQLMap 文件建立对应的“回归测试用例”
- PostgreSQL 与 MySQL 需要“双跑”（CI 中分别跑）
- 关键 SQL（待办列表、并行网关查询）做 explain analyze 定期评估

