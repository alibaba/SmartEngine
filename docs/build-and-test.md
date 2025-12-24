# 构建、单测、集成测试、测试数据/用例结构

---

## 1. 构建

在仓库根目录执行：

```bash
mvn -q -DskipTests=false test
```

按模块构建：

```bash
mvn -pl core -am test
mvn -pl extension/storage/storage-custom -am test
mvn -pl extension/storage/storage-mysql -am test
```

> 提示：DataBase 模式测试通常需要本地数据库（默认配置在 `storage-mysql/src/test/resources/spring/application-test.xml`）。

---

## 2. 单元测试 vs 集成测试

- core：以 parser/behavior 的单元测试为主（不依赖数据库）
- storage-custom：以“语义回归测试”为主（并行网关、跳转等）
- storage-mysql：以“存储 + MyBatis + 事务”的集成测试为主（依赖数据库）

---

## 3. 测试资源（BPMN）

常见目录：

- `core/src/test/resources/process-def/*`
- `extension/storage/storage-custom/src/test/resources/*.xml`
- `extension/storage/storage-mysql/src/test/resources/*.bpmn20.xml`

建议你把这些 BPMN 当作“语义金标准”，二开时新增行为/解析器要新增对应 BPMN + 测试。

---

## 4. 测试数据库准备（DataBase 模式）

仓库未提供 docker-compose，但你可以用本地 PostgreSQL / MySQL：

1) 创建数据库与用户  
2) 执行 DDL（见 `04-persistence/database-schema.md`）  
3) 修改 `application-test.xml` 的 JDBC URL/账号密码  
4) 跑 `storage-mysql` 模块测试  

---

## 5. CI 建议（如果你要在团队里用）

- MySQL + PostgreSQL 双跑（避免方言回归）
- 并行网关相关测试要单独作为 “语义回归” job
- 对关键 SQL 做 explain 基线（防止性能回归）

