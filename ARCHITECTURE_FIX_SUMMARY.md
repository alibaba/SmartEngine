# 架构修复总结 - 操作记录功能

## 问题描述

原始实现在 `core` 模块的 `DefaultTaskCommandService` 中直接引用了 `storage-mysql` 模块的 DAO 和 Entity 类，违反了分层架构原则。

## 解决方案

采用SmartEngine的标准架构模式：**接口在core，实现在storage-mysql**

---

## 已完成的工作 ✅

### 1. Core 模块 - Model 接口层
创建了3个模型接口：

- **[TaskTransferRecord.java](core/src/main/java/com/alibaba/smart/framework/engine/model/instance/TaskTransferRecord.java)**
  - 任务移交记录接口

- **[AssigneeOperationRecord.java](core/src/main/java/com/alibaba/smart/framework/engine/model/instance/AssigneeOperationRecord.java)**
  - 加签减签操作记录接口

- **[RollbackRecord.java](core/src/main/java/com/alibaba/smart/framework/engine/model/instance/RollbackRecord.java)**
  - 流程回退记录接口

### 2. Core 模块 - Model 实现层
创建了3个模型实现类（继承AbstractLifeCycleInstance）：

- **[DefaultTaskTransferRecord.java](core/src/main/java/com/alibaba/smart/framework/engine/instance/impl/DefaultTaskTransferRecord.java)**
- **[DefaultAssigneeOperationRecord.java](core/src/main/java/com/alibaba/smart/framework/engine/instance/impl/DefaultAssigneeOperationRecord.java)**
- **[DefaultRollbackRecord.java](core/src/main/java/com/alibaba/smart/framework/engine/instance/impl/DefaultRollbackRecord.java)**

### 3. Core 模块 - Storage 接口层
创建了3个存储接口：

- **[TaskTransferRecordStorage.java](core/src/main/java/com/alibaba/smart/framework/engine/instance/storage/TaskTransferRecordStorage.java)**
  - 定义: insert, findByTaskId, find, update, remove

- **[AssigneeOperationRecordStorage.java](core/src/main/java/com/alibaba/smart/framework/engine/instance/storage/AssigneeOperationRecordStorage.java)**
  - 定义: insert, findByTaskId, find, update, remove

- **[RollbackRecordStorage.java](core/src/main/java/com/alibaba/smart/framework/engine/instance/storage/RollbackRecordStorage.java)**
  - 定义: insert, findByProcessInstanceId, find, update, remove

### 4. Core 模块 - 服务层修改
修改了 **[DefaultTaskCommandService.java](core/src/main/java/com/alibaba/smart/framework/engine/service/command/impl/DefaultTaskCommandService.java)**：

- ✅ 移除了 DAO 和 Entity 的导入
- ✅ 添加了 Storage 接口和 Model 实现类的导入
- ✅ 将字段类型从 DAO 改为 Storage
- ✅ 修改了4个方法的实现：
  - `transferWithReason()` - 使用 TaskTransferRecordStorage
  - `rollbackTask()` - 使用 RollbackRecordStorage
  - `addTaskAssigneeCandidateWithReason()` - 使用 AssigneeOperationRecordStorage
  - `removeTaskAssigneeCandidateWithReason()` - 使用 AssigneeOperationRecordStorage
- ✅ 移除了 `setGmtCreate` 和 `setGmtModified` 调用（这些在Storage层处理）

---

## 待完成的工作 🚧

### Storage-MySQL 模块需要创建的文件

#### 1. Builder类 (3个文件)

位置: `extension/storage/storage-mysql/src/main/java/com/alibaba/smart/framework/engine/persister/database/builder/`

**TaskTransferRecordBuilder.java**
```java
public class TaskTransferRecordBuilder {
    // Entity -> Model
    public static TaskTransferRecord buildFromEntity(TaskTransferRecordEntity entity)

    // Model -> Entity
    public static TaskTransferRecordEntity buildEntityFrom(TaskTransferRecord model)
}
```

**AssigneeOperationRecordBuilder.java**
```java
public class AssigneeOperationRecordBuilder {
    public static AssigneeOperationRecord buildFromEntity(AssigneeOperationRecordEntity entity)
    public static AssigneeOperationRecordEntity buildEntityFrom(AssigneeOperationRecord model)
}
```

**RollbackRecordBuilder.java**
```java
public class RollbackRecordBuilder {
    public static RollbackRecord buildFromEntity(RollbackRecordEntity entity)
    public static RollbackRecordEntity buildEntityFrom(RollbackRecord model)
}
```

#### 2. Storage实现类 (3个文件)

位置: `extension/storage/storage-mysql/src/main/java/com/alibaba/smart/framework/engine/persister/database/service/`

**RelationshipDatabaseTaskTransferRecordStorage.java**
```java
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = TaskTransferRecordStorage.class)
public class RelationshipDatabaseTaskTransferRecordStorage implements TaskTransferRecordStorage {
    // 实现所有接口方法
    // 使用 TaskTransferRecordDAO
    // 使用 TaskTransferRecordBuilder 进行转换
}
```

**RelationshipDatabaseAssigneeOperationRecordStorage.java**
```java
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = AssigneeOperationRecordStorage.class)
public class RelationshipDatabaseAssigneeOperationRecordStorage implements AssigneeOperationRecordStorage {
    // 实现所有接口方法
}
```

**RelationshipDatabaseRollbackRecordStorage.java**
```java
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = RollbackRecordStorage.class)
public class RelationshipDatabaseRollbackRecordStorage implements RollbackRecordStorage {
    // 实现所有接口方法
}
```

---

## 实现模式参考

参考现有实现: [RelationshipDatabaseSupervisionInstanceStorage.java](extension/storage/storage-mysql/src/main/java/com/alibaba/smart/framework/engine/persister/database/service/RelationshipDatabaseSupervisionInstanceStorage.java)

### 关键点：

1. **获取DAO**:
```java
TaskTransferRecordDAO dao = (TaskTransferRecordDAO) processEngineConfiguration
    .getInstanceAccessor().access("taskTransferRecordDAO");
```

2. **Entity -> Model 转换**:
```java
TaskTransferRecord record = TaskTransferRecordBuilder.buildFromEntity(entity);
```

3. **Model -> Entity 转换**:
```java
TaskTransferRecordEntity entity = TaskTransferRecordBuilder.buildEntityFrom(record);
```

4. **设置时间戳**:
```java
entity.setGmtCreate(DateUtil.getCurrentDate());
entity.setGmtModified(DateUtil.getCurrentDate());
```

5. **返回值处理**:
```java
dao.insert(entity);
record.setInstanceId(entity.getId().toString()); // 设置生成的ID
return record;
```

---

## 验证步骤

### 1. 编译 Core 模块
```bash
cd /Users/ghj/work/startup/phenix/AuraMeta/vendor/smart-engine
mvn compile -pl core -am
```

预期结果: ✅ 成功编译（core不再依赖storage-mysql）

### 2. 创建 Storage-MySQL 实现后编译
```bash
mvn compile -pl extension/storage/storage-mysql -am
```

预期结果: ✅ 成功编译并注册所有@ExtensionBinding

### 3. 运行单元测试
```bash
mvn test -Dtest="*DAOTest,*IntegrationTest"
```

预期结果: ✅ 所有测试通过

---

## 架构优势

### 修复前:
```
core (DefaultTaskCommandService)
  |
  └──> storage-mysql (DAO/Entity) ❌ 错误的依赖方向
```

### 修复后:
```
core (Interface层)
  └──> TaskTransferRecord (接口)
  └──> TaskTransferRecordStorage (接口)
  └──> DefaultTaskTransferRecord (实现)

storage-mysql (Implementation层)
  └──> RelationshipDatabaseTaskTransferRecordStorage
       └──> TaskTransferRecordDAO
       └──> TaskTransferRecordEntity
       └──> TaskTransferRecordBuilder
```

✅ **依赖倒置原则**: core定义接口，storage-mysql提供实现
✅ **单一职责**: Model/Storage/DAO/Entity 各司其职
✅ **可扩展性**: 未来可添加其他存储实现（如Redis、MongoDB）

---

## 下一步行动

1. 创建3个Builder类
2. 创建3个Storage实现类
3. 编译验证
4. 运行测试
5. 更新文档

所有文件创建完成后，整个功能将符合SmartEngine的架构规范。

---

**创建日期**: 2026-01-09
**状态**: Core模块已完成 ✅ | Storage-MySQL模块待实现 🚧
