# 工作流管理增强功能 - 快速参考指南

## 📌 快速验证

### 1. 验证编译
```bash
cd /Users/ghj/work/startup/phenix/AuraMeta/vendor/smart-engine
mvn clean compile
```

预期结果: `BUILD SUCCESS`

### 2. 验证数据库
```bash
psql -h localhost -p 5432 -U ghj -d aura_meta -c "\d se_process_instance" | grep complete_time
```

预期输出: `complete_time | timestamp(6) without time zone`

### 3. 运行测试
```bash
mvn test -Dtest="*DAOTest"
```

预期结果: 所有测试通过

---

## 📁 新增文件清单

### Core 模块 (9个文件)

**Model 接口**:
```
core/src/main/java/com/alibaba/smart/framework/engine/model/instance/
├── TaskTransferRecord.java
├── AssigneeOperationRecord.java
└── RollbackRecord.java
```

**Model 实现**:
```
core/src/main/java/com/alibaba/smart/framework/engine/instance/impl/
├── DefaultTaskTransferRecord.java
├── DefaultAssigneeOperationRecord.java
└── DefaultRollbackRecord.java
```

**Storage 接口**:
```
core/src/main/java/com/alibaba/smart/framework/engine/instance/storage/
├── TaskTransferRecordStorage.java
├── AssigneeOperationRecordStorage.java
└── RollbackRecordStorage.java
```

### Storage-MySQL 模块 (12个文件)

**MyBatis 映射**:
```
extension/storage/storage-mysql/src/main/resources/mybatis/sqlmap/
├── task_transfer_record.xml (新建)
├── assignee_operation_record.xml (新建)
├── process_rollback_record.xml (新建)
├── process_instance.xml (已更新)
├── task_instance.xml (已更新)
├── supervision_instance.xml (已更新)
└── notification_instance.xml (已更新)
```

**Builder 类**:
```
extension/storage/storage-mysql/src/main/java/.../builder/
├── TaskTransferRecordBuilder.java
├── AssigneeOperationRecordBuilder.java
└── RollbackRecordBuilder.java
```

**Storage 实现**:
```
extension/storage/storage-mysql/src/main/java/.../service/
├── RelationshipDatabaseTaskTransferRecordStorage.java
├── RelationshipDatabaseAssigneeOperationRecordStorage.java
└── RelationshipDatabaseRollbackRecordStorage.java
```

**测试文件**:
```
extension/storage/storage-mysql/src/test/java/.../test/dao/
├── TaskTransferRecordDAOTest.java
├── AssigneeOperationRecordDAOTest.java
└── RollbackRecordDAOTest.java

extension/storage/storage-mysql/src/test/java/.../test/service/
├── TaskOperationRecordIntegrationTest.java
└── ProcessCompleteTimeIntegrationTest.java
```

---

## 🔍 关键代码位置

### 任务移交记录
```java
// Service层调用
taskCommandService.transferWithReason(taskId, fromUserId, toUserId, reason, tenantId);

// 查询移交历史
List<TaskTransferRecord> records = taskTransferRecordStorage.findByTaskId(taskInstanceId, tenantId, config);
```

### 加签减签记录
```java
// 加签
taskCommandService.addTaskAssigneeCandidateWithReason(taskId, tenantId, candidate, reason);

// 减签
taskCommandService.removeTaskAssigneeCandidateWithReason(taskId, tenantId, candidate, reason);

// 查询操作历史
List<AssigneeOperationRecord> records = assigneeOperationRecordStorage.findByTaskId(taskInstanceId, tenantId, config);
```

### 流程回退记录
```java
// 回退流程
ProcessInstance process = taskCommandService.rollbackTask(taskId, targetActivityId, reason, tenantId);

// 查询回退历史
List<RollbackRecord> records = rollbackRecordStorage.findByProcessInstanceId(processInstanceId, tenantId, config);
```

### 流程完成时间查询
```java
// 查询办结流程
ProcessInstanceQueryParam param = new ProcessInstanceQueryParam();
param.setStatus("completed");
param.setCompleteTimeStart(startDate);
param.setCompleteTimeEnd(endDate);
param.setTenantId(tenantId);

List<ProcessInstance> processes = processQueryService.findCompletedProcesses(param);
```

---

## 🗄️ 数据库表结构

### se_task_transfer_record
```sql
id BIGSERIAL PRIMARY KEY
gmt_create TIMESTAMP(6)
gmt_modified TIMESTAMP(6)
task_instance_id BIGINT
from_user_id VARCHAR(255)
to_user_id VARCHAR(255)
transfer_reason TEXT
deadline TIMESTAMP(6)
tenant_id VARCHAR(255)
```

### se_assignee_operation_record
```sql
id BIGSERIAL PRIMARY KEY
gmt_create TIMESTAMP(6)
gmt_modified TIMESTAMP(6)
task_instance_id BIGINT
operation_type VARCHAR(50)  -- 'add_assignee' or 'remove_assignee'
operator_user_id VARCHAR(255)
target_user_id VARCHAR(255)
operation_reason TEXT
tenant_id VARCHAR(255)
```

### se_process_rollback_record
```sql
id BIGSERIAL PRIMARY KEY
gmt_create TIMESTAMP(6)
gmt_modified TIMESTAMP(6)
process_instance_id BIGINT
task_instance_id BIGINT
rollback_type VARCHAR(50)  -- 'specific' or 'previous'
from_activity_id VARCHAR(255)
to_activity_id VARCHAR(255)
operator_user_id VARCHAR(255)
rollback_reason TEXT
tenant_id VARCHAR(255)
```

### se_process_instance (新增字段)
```sql
complete_time TIMESTAMP(6)  -- 流程完成时间
INDEX idx_complete_time
```

---

## 🧪 测试命令速查

```bash
# 编译
mvn compile

# 运行所有测试
mvn test

# 运行DAO测试
mvn test -Dtest="*DAOTest"

# 运行集成测试
mvn test -Dtest="*IntegrationTest"

# 运行单个测试类
mvn test -Dtest="TaskTransferRecordDAOTest"

# 运行单个测试方法
mvn test -Dtest="TaskTransferRecordDAOTest#testInsertAndSelect"

# 跳过测试编译
mvn compile -DskipTests

# 清理编译
mvn clean compile
```

---

## 📊 架构图示

```
请求流程:
1. Controller/API
   ↓
2. DefaultTaskCommandService (core)
   ↓
3. TaskTransferRecordStorage 接口 (core)
   ↓
4. RelationshipDatabaseTaskTransferRecordStorage 实现 (storage-mysql)
   ↓
5. TaskTransferRecordBuilder 转换
   ↓
6. TaskTransferRecordDAO 数据访问
   ↓
7. MyBatis 执行SQL
   ↓
8. PostgreSQL 数据库
```

---

## ⚠️ 注意事项

### 1. ID类型转换
- Model层使用 `String` 类型ID
- Entity层使用 `Long` 类型ID
- Builder负责转换: `Long.valueOf()` 和 `.toString()`

### 2. 时间字段
- `gmtCreate` / `gmtModified` - 仅在Entity层
- `startTime` / `completeTime` - 在Model层
- Builder映射: `startTime ↔ gmtCreate`

### 3. 租户隔离
- 所有查询必须传入 `tenantId`
- WHERE子句自动添加 `tenant_id` 条件

### 4. 事务管理
- Storage层操作在Service层事务中
- 失败自动回滚

---

## 🔧 常见问题

### Q: 编译失败 "cannot find symbol"
**A**: 确保先编译core模块: `mvn compile -pl core -am`

### Q: 测试失败 "Connection refused"
**A**: 检查PostgreSQL是否运行: `psql -h localhost -p 5432 -U ghj -d aura_meta`

### Q: MyBatis映射找不到
**A**: 检查mybatis-config.xml是否注册了新的mapper文件

### Q: DAO注入失败
**A**: 检查Spring配置文件中是否配置了DAO bean

---

## 📚 相关文档

- **[IMPLEMENTATION_COMPLETE.md](IMPLEMENTATION_COMPLETE.md)** - 完整实施总结
- **[ARCHITECTURE_FIX_SUMMARY.md](ARCHITECTURE_FIX_SUMMARY.md)** - 架构修复说明
- **[TESTING_SUMMARY.md](TESTING_SUMMARY.md)** - 测试执行总结

---

**最后更新**: 2026-01-09
