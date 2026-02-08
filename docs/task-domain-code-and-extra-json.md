# 任务多维查询：domain_code + extra JSON 字段

不同流程类型的任务有各自不同的分类查询维度。原有 `extension` VARCHAR(255) 仅支持精确匹配，灵活性不足。本次新增两个字段，提供更强大的分类与结构化查询能力：

- **`domain_code`** VARCHAR(64) — 使用方自定义语义的索引列，用于快速过滤
- **`extra`** JSON/JSONB — 原生 JSON 类型，通过 Dialect 抽象支持跨库 JSON path 查询

> **向后兼容**：`extension` 字段完全不动，老客户无需迁移。

---

## 1. 使用场景

| 场景 | 用法 |
|------|------|
| 按业务域分类查询任务 | `domainCode("HR")` |
| 按多个业务域批量查询 | `domainCodeIn("HR", "Finance")` |
| 按 JSON 内的字段精确过滤 | `extraJson("category", "purchase")` |
| 按 JSON 内的字段多值过滤 | `extraJsonIn("priority_level", "high", "critical")` |
| 按 JSON 内的字段模糊搜索 | `extraJsonLike("department", "研发")` |
| 组合查询 | `domainCode("HR").extraJson("category", "leave")` |

---

## 2. Fluent Query API

### 2.1 domain_code 查询

```java
// 精确匹配
List<TaskInstance> tasks = smartEngine.createTaskQuery()
    .domainCode("HR")
    .taskStatus(TaskInstanceConstant.PENDING)
    .list();

// 条件匹配
smartEngine.createTaskQuery()
    .domainCode(StringUtils.isNotBlank(domain), domain)
    .list();

// 多值 IN 查询
smartEngine.createTaskQuery()
    .domainCodeIn("HR", "Finance", "IT")
    .list();

// 模糊查询
smartEngine.createTaskQuery()
    .domainCodeLike("Fin")
    .list();
```

### 2.2 extra JSON 查询

```java
// JSON key 精确匹配
List<TaskInstance> tasks = smartEngine.createTaskQuery()
    .processDefinitionType("approval")
    .extraJson("category", "purchase")
    .taskStatus(TaskInstanceConstant.PENDING)
    .listPage(0, 10);

// JSON key 多值 IN 查询
smartEngine.createTaskQuery()
    .extraJsonIn("priority_level", "high", "critical")
    .list();

// JSON key 模糊查询
smartEngine.createTaskQuery()
    .extraJsonLike("department", "研发")
    .list();
```

### 2.3 组合查询

```java
// domain_code + JSON + assignee 组合
List<TaskInstance> tasks = smartEngine.createTaskQuery()
    .domainCode("HR")
    .extraJson("category", "leave")
    .taskCandidateUser("user001")
    .taskStatus(TaskInstanceConstant.PENDING)
    .orderByCreateTime().desc()
    .listPage(0, 10);
```

### 2.4 设置字段

```java
// 在创建/更新任务时设置
TaskInstance task = ...;
task.setDomainCode("HR");
task.setExtra("{\"category\":\"leave\",\"days\":5,\"type\":\"annual\"}");
```

---

## 3. TaskQuery 接口方法一览

| 方法签名 | 说明 |
|---------|------|
| `domainCode(String)` | domain_code 精确匹配 |
| `domainCode(boolean, String)` | 条件精确匹配 |
| `domainCodeIn(List<String>)` | domain_code IN 查询 |
| `domainCodeIn(String...)` | domain_code IN 查询（varargs） |
| `domainCodeLike(String)` | domain_code 模糊查询 |
| `extraJson(String key, String value)` | JSON key 精确匹配 |
| `extraJsonIn(String key, List<String>)` | JSON key IN 查询 |
| `extraJsonIn(String key, String...)` | JSON key IN 查询（varargs） |
| `extraJsonLike(String key, String)` | JSON key 模糊查询 |

代码位置：`core/src/main/java/com/alibaba/smart/framework/engine/query/TaskQuery.java`

---

## 4. 跨库支持（Dialect 抽象）

### 4.1 extra 列类型

| 数据库 | DDL 列类型 | 说明 |
|--------|-----------|------|
| MySQL 5.7+ | `JSON` | 原生 JSON 类型，自动验证 |
| PostgreSQL 9.4+ | `JSONB` | 二进制 JSON，支持 GIN 索引 |
| Oracle 12c+ | `CLOB` + `CHECK (extra IS JSON)` | CLOB 存储 + JSON 约束 |
| H2 2.x | `CLOB` | 测试用，JSON 函数可用 |
| DM 8 (达梦) | `CLOB` | Oracle 兼容 |
| KingbaseES (人大金仓) | `JSONB` | PostgreSQL 兼容 |
| OceanBase | `JSON` | MySQL 兼容 |
| SQL Server 2016+ | `NVARCHAR(MAX)` | 内置 JSON 函数 |

### 4.2 Dialect.jsonExtractText() 生成的 SQL

`Dialect.jsonExtractText(column, key)` 方法根据数据库类型生成对应的 JSON 提取 SQL 表达式：

| 数据库 | 生成的 SQL | 方言类 |
|--------|-----------|--------|
| MySQL | `JSON_UNQUOTE(JSON_EXTRACT(col, '$.key'))` | `MySqlDialect` |
| PostgreSQL | `(col->>'key')` | `PostgreSqlDialect` |
| Oracle | `JSON_VALUE(col, '$.key')` | `OracleDialect`（继承默认） |
| DM (达梦) | `JSON_VALUE(col, '$.key')` | `DmDialect`（继承默认） |
| H2 | `JSON_VALUE(col FORMAT JSON, '$.key')` | `H2Dialect` |
| KingbaseES | `(col->>'key')` | `KingbaseDialect` |
| OceanBase | `JSON_UNQUOTE(JSON_EXTRACT(col, '$.key'))` | `OceanBaseDialect` |
| SQL Server | `JSON_VALUE(col, '$.key')` | `SqlServerDialect`（继承默认） |

代码位置：`core/src/main/java/com/alibaba/smart/framework/engine/dialect/Dialect.java`

### 4.3 配置 Dialect

```java
// 方式1：显式设置
ProcessEngineConfiguration config = new DefaultProcessEngineConfiguration();
config.setDialect(new PostgreSqlDialect());

// 方式2：通过 DialectRegistry 获取
Dialect dialect = DialectRegistry.getInstance().getDialect("postgresql");
config.setDialect(dialect);

// 方式3：从 JDBC URL 自动检测
Dialect dialect = DialectRegistry.getInstance().detectDialect(jdbcUrl);
config.setDialect(dialect);
```

> 如果未设置 Dialect，查询构建器会回退到 `DialectRegistry.getInstance().getDefaultDialect()`（默认为 MySQL）。

---

## 5. 数据库表结构变更

### 5.1 新增列

在 `se_task_instance` 表的 `extension` 列之后、`tenant_id` 列之前新增两列：

| 列名 | MySQL | PostgreSQL | Oracle/DM | H2 |
|------|-------|-----------|-----------|-----|
| `domain_code` | `varchar(64) DEFAULT NULL` | `varchar(64) DEFAULT NULL` | `VARCHAR2(64) DEFAULT NULL` | `varchar(64) DEFAULT NULL` |
| `extra` | `json DEFAULT NULL` | `jsonb DEFAULT NULL` | `CLOB DEFAULT NULL CHECK (extra IS JSON)` | `clob DEFAULT NULL` |

### 5.2 迁移脚本

已提供迁移脚本，位于 `extension/storage/storage-mysql/src/main/resources/sql/migration/`：

**MySQL** — `V002__add_domain_code_and_extra.sql`

```sql
ALTER TABLE se_task_instance
    ADD COLUMN domain_code varchar(64) DEFAULT NULL COMMENT 'domain code',
    ADD COLUMN extra json DEFAULT NULL COMMENT 'extra JSON data';

CREATE INDEX idx_task_domain_code ON se_task_instance(domain_code);
```

**PostgreSQL** — `V002__add_domain_code_and_extra_postgresql.sql`

```sql
ALTER TABLE se_task_instance
    ADD COLUMN domain_code varchar(64) DEFAULT NULL,
    ADD COLUMN extra jsonb DEFAULT NULL;

CREATE INDEX idx_task_domain_code ON se_task_instance(domain_code);
CREATE INDEX idx_task_extra_gin ON se_task_instance USING GIN (extra);
```

**Oracle/DM** — `V002__add_domain_code_and_extra_oracle.sql`

```sql
ALTER TABLE se_task_instance ADD domain_code VARCHAR2(64) DEFAULT NULL;
ALTER TABLE se_task_instance ADD extra CLOB DEFAULT NULL CHECK (extra IS JSON);

CREATE INDEX idx_task_domain_code ON se_task_instance(domain_code);
```

---

## 6. 安全性

### 6.1 JSON key 校验

所有 `extraJson*()` 方法的 key 参数经过正则校验，仅允许 `[a-zA-Z_][a-zA-Z0-9_.]*`：

```java
private static final Pattern VALID_JSON_KEY = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_.]*$");
```

非法 key 会抛出 `IllegalArgumentException`。

### 6.2 SQL 注入防护

MyBatis XML 中使用 `${jc.sqlExpression}` 拼接 SQL 表达式，该表达式由引擎内部的 `Dialect.jsonExtractText()` 生成，不接受用户输入。JSON key 经过正则校验，值参数通过 `#{jc.value}` 参数化传递，不存在注入风险。

---

## 7. TypeHandler

PostgreSQL 的 `JSONB` 类型需要 MyBatis TypeHandler 确保正确读写。已提供 `JsonTypeHandler`：

代码位置：`extension/storage/storage-mysql/src/main/java/.../handler/JsonTypeHandler.java`

该 TypeHandler 使用 `ps.setObject(i, parameter, java.sql.Types.OTHER)` 让 JDBC 驱动自动处理 JSON/JSONB 类型转换，兼容 MySQL、PostgreSQL 等数据库。

在 MyBatis mapper XML 中已对 `extra` 字段的 insert/update 指定了 TypeHandler：

```xml
#{extra, typeHandler=com.alibaba.smart.framework.engine.persister.database.handler.JsonTypeHandler}
```

---

## 8. 关键文件清单

### Core 模块

| 文件 | 变更 |
|------|------|
| `dialect/Dialect.java` | +`jsonExtractText()` 接口方法 |
| `dialect/impl/AbstractDialect.java` | 默认实现（Oracle/DM/SQL Server 语法） |
| `dialect/impl/MySqlDialect.java` | MySQL JSON 函数覆盖 |
| `dialect/impl/PostgreSqlDialect.java` | PostgreSQL `->>` 操作符覆盖 |
| `dialect/impl/H2Dialect.java` | H2 `FORMAT JSON` 覆盖 |
| `dialect/impl/KingbaseDialect.java` | 同 PostgreSQL |
| `dialect/impl/OceanBaseDialect.java` | 同 MySQL |
| `configuration/ProcessEngineConfiguration.java` | +`getDialect/setDialect` |
| `configuration/impl/DefaultProcessEngineConfiguration.java` | +`dialect` 字段 |
| `model/instance/TaskInstance.java` | +`domainCode` + `extra` 接口方法 |
| `instance/impl/DefaultTaskInstance.java` | +`domainCode` + `extra` 字段 |
| `query/TaskQuery.java` | +10 个新方法 |
| `query/impl/TaskQueryImpl.java` | 完整实现 + JSON 条件构建 |
| `service/param/query/JsonCondition.java` | **新建** — JSON 精确/模糊条件模型 |
| `service/param/query/JsonInCondition.java` | **新建** — JSON IN 条件模型 |
| `service/param/query/TaskInstanceQueryParam.java` | +domainCode/jsonConditions 字段 |
| `service/param/query/TaskInstanceQueryByAssigneeParam.java` | +domainCode/jsonConditions 字段 |

### Storage-MySQL 模块

| 文件 | 变更 |
|------|------|
| `entity/TaskInstanceEntity.java` | +`domainCode` + `extra` |
| `builder/TaskInstanceBuilder.java` | entity ↔ model 转换 |
| `handler/JsonTypeHandler.java` | **新建** — PostgreSQL JSONB 兼容 |
| `mybatis/sqlmap/task_instance.xml` | baseColumn/insert/update/select/where 全面更新 |
| `sql/schema.sql` | +`domain_code` + `extra` 列（MySQL） |
| `sql/schema-postgre.sql` | +`domain_code` + `extra jsonb` 列 |
| `sql/schema-oracle.sql` | +`domain_code` + `extra CLOB` 列 |
| `sql/schema-h2-only-for-test.sql` | +`domain_code` + `extra clob` 列 |
| `sql/migration/V002__*.sql` | **新建** — MySQL/PG/Oracle 迁移脚本 |

### 测试

| 文件 | 测试数 |
|------|--------|
| `core/.../dialect/DialectTest.java` | +9 个 JSON 方法测试 |
| `storage-mysql/.../dao/TaskInstanceDomainCodeAndExtraTest.java` | **新建** 12 个集成测试 |

---

## 9. 性能建议

| 建议 | 说明 |
|------|------|
| 优先使用 `domain_code` 缩小范围 | `domain_code` 有索引，先过滤再做 JSON 查询 |
| PostgreSQL 使用 GIN 索引 | 迁移脚本已包含 `CREATE INDEX ... USING GIN (extra)` |
| 避免大量 JSON 嵌套查询 | 本实现仅支持单层 key，如 `category`、`department.name` |
| Oracle CLOB 性能 | Oracle 下 `extra` 为 CLOB，JSON 查询性能较差，建议配合 `domain_code` 索引 |

---

## 下一步

- [API 指南](api-guide.md) — 了解完整的 TaskQuery API
- [数据库表结构](database-schema.md) — 查看完整 DDL
- [Dialect 多库支持](dual-storage-mode.md) — 了解存储路由机制
