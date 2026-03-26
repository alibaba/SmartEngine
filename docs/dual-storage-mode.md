# 双存储模式共存方案

## 1. 背景与目标

SmartEngine 原有两个独立的存储模块：

- **storage-mysql**（DATABASE 模式）：基于 MyBatis 持久化到关系型数据库
- **storage-custom**（CUSTOM 模式）：基于 `PersisterSession` 的自定义存储，可用于内存、自定义持久化等场景

两者使用相同的 `@ExtensionBinding(group="common", bindKey=XXXStorage.class)` 注册。当两个模块同时出现在 classpath 时，`SimpleAnnotationScanner.handleDuplicatedKey()` 因 priority 相同直接抛出 `EngineException`，导致引擎初始化失败。

**目标**：两个 storage 模块可以同时依赖，并支持运行时通过 `StorageModeHolder`（ThreadLocal）动态切换存储模式，**服务层零改动**。

---

## 2. 架构设计

### 2.1 整体结构

```
┌─────────────────────────────────────────────────────┐
│                   Service Layer                      │
│  (DefaultProcessCommandService, DefaultTaskQuery..)  │
│         ┌──────────────────────┐                    │
│         │  cached storage ref  │  ← Dynamic Proxy   │
│         └──────────┬───────────┘                    │
├────────────────────┼────────────────────────────────┤
│                    ▼                                 │
│            ┌──────────────┐                         │
│            │ StorageRouter │ ← ThreadLocal 路由      │
│            └──────┬───────┘                         │
│         ┌─────────┼─────────┐                       │
│         ▼                   ▼                       │
│  ┌─────────────┐    ┌──────────────┐               │
│  │  DATABASE    │    │   CUSTOM     │               │
│  │ (MySQL impl) │    │ (Custom impl)│               │
│  └─────────────┘    └──────────────┘               │
└─────────────────────────────────────────────────────┘
```

### 2.2 三个核心机制

| 机制 | 解决的问题 | 关键类 |
|------|-----------|--------|
| **Group 分离** | 消除扫描冲突 | `ExtensionConstant.CUSTOM` |
| **StorageRouter** | 按模式路由到正确的存储实现 | `StorageRouter`, `StorageRegistry` |
| **动态代理** | 服务层缓存的 storage 引用能动态路由 | `SimpleAnnotationScanner.createStorageProxy()` |

---

## 3. 实现细节

### 3.1 Phase 1: Group 分离 — 消除扫描冲突

**问题**：storage-mysql 和 storage-custom 都注册在 `group="common"` 下，同一 bindKey 两个实现导致冲突。

**方案**：新增 `ExtensionConstant.CUSTOM = "custom"` 常量，storage-custom 的 6 个 Storage 实现改为 `group="custom"`。storage-mysql 保持 `group="common"` 不变。

```java
// storage-custom 模块的 6 个 Storage 实现
// Before
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = ProcessInstanceStorage.class)

// After
@ExtensionBinding(group = ExtensionConstant.CUSTOM, bindKey = ProcessInstanceStorage.class)
```

受影响的 Storage 实现：

| 类名 | 模块 |
|------|------|
| `CustomProcessInstanceStorage` | storage-custom |
| `CustomExecutionInstanceStorage` | storage-custom |
| `CustomActivityInstanceStorage` | storage-custom |
| `CustomTaskInstanceStorage` | storage-custom |
| `CustomTaskAssigneeInstanceStorage` | storage-custom |
| `CustomVariableInstanceStorage` | storage-custom |

### 3.2 Phase 2: StorageRouter 集成到引擎

**StorageRouter** 在引擎初始化时注册两组 Storage 实现，运行时按当前模式路由。

模式解析优先级：
1. `StorageModeHolder` ThreadLocal（请求级别覆盖）
2. `StorageContext`（上下文配置）
3. `StorageRouter.defaultMode`（默认 DATABASE）

**引擎初始化流程**（`DefaultSmartEngine.initializeStorageRouter()`）：

```
annotationScanner.scan()
    ↓
从 scanResult["common"] 取出实现 → 注册到 StorageRouter 的 DATABASE 模式
从 scanResult["custom"] 取出实现 → 注册到 StorageRouter 的 CUSTOM 模式
    ↓
如果只有一个模块存在 → 同时注册到两种模式（向后兼容）
    ↓
将 StorageRouter 设置到 Scanner（用于动态代理）
```

**单模块向后兼容**：当 classpath 只有 storage-custom 时，其实现会同时注册到 DATABASE 和 CUSTOM 模式下，确保默认模式（DATABASE）正常工作。

### 3.3 Phase 3: 动态代理 — 服务层零改动

**问题**：服务层（如 `DefaultExecutionQueryService`）在引擎初始化时调用 `getExtensionPoint("common", ExecutionInstanceStorage.class)` 并**缓存**返回值。此后切换 `StorageMode` 对缓存引用无效。

```java
// DefaultExecutionQueryService.java
public void start() {
    // 初始化时调用一次，结果被缓存
    this.executionInstanceStorage = scanner.getExtensionPoint("common", ExecutionInstanceStorage.class);
}

public List<ExecutionInstance> findActiveExecutionList(String id) {
    // 每次调用都使用缓存的 executionInstanceStorage
    return executionInstanceStorage.findActiveExecution(id, null, config);
}
```

**方案**：`SimpleAnnotationScanner.getExtensionPoint()` 对 StorageRouter 注册的 storage 类型返回 `java.lang.reflect.Proxy` 动态代理。代理在每次方法调用时通过 StorageRouter 根据当前 `StorageMode` 路由到正确的实现。

```java
// SimpleAnnotationScanner.java
public <T> T getExtensionPoint(String group, Class<T> clazz) {
    if (storageRouter != null && "common".equals(group) && storageRouter.hasStorageType(clazz)) {
        return createStorageProxy(clazz);  // 返回动态代理
    }
    // 非 storage 类型走原逻辑
    return (T) scanResult.get(group).getBindingMap().get(clazz);
}

private <T> T createStorageProxy(Class<T> storageInterface) {
    return (T) Proxy.newProxyInstance(
        storageInterface.getClassLoader(),
        new Class<?>[]{storageInterface},
        (proxy, method, args) -> {
            T realStorage = storageRouter.getStorage(storageInterface);  // 每次动态路由
            try {
                return method.invoke(realStorage, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();  // 必须解包 InvocationTargetException
            }
        }
    );
}
```

**效果**：19 个服务类 + CommonServiceHelper **全部无需改动**。

---

## 4. 使用方式

### 4.1 Maven 依赖

单模块使用（不变）：

```xml
<!-- 只用数据库存储 -->
<dependency>
    <artifactId>smart-engine-extension-storage-mysql</artifactId>
</dependency>

<!-- 只用自定义存储 -->
<dependency>
    <artifactId>smart-engine-extension-storage-custom</artifactId>
</dependency>
```

双模块共存：

```xml
<dependency>
    <artifactId>smart-engine-extension-storage-mysql</artifactId>
</dependency>
<dependency>
    <artifactId>smart-engine-extension-storage-custom</artifactId>
</dependency>
```

### 4.2 运行时切换存储模式

```java
// 方式 1: ThreadLocal 请求级切换
StorageModeHolder.set(StorageMode.CUSTOM);
try {
    // 此范围内所有引擎操作使用 CUSTOM 存储
    processCommandService.start(defId, version, request);
} finally {
    StorageModeHolder.clear();
}

// 方式 2: 设置全局默认模式
StorageRouter router = processEngineConfiguration.getStorageRouter();
router.setDefaultMode(StorageMode.CUSTOM);

// 方式 3: ThreadLocal 覆盖默认模式
router.setDefaultMode(StorageMode.CUSTOM);  // 默认 CUSTOM
StorageModeHolder.set(StorageMode.DATABASE); // 此请求走 DATABASE
```

### 4.3 StorageMode 枚举值

| 枚举值 | 说明 |
|--------|------|
| `DATABASE` | 数据库存储（默认），由 storage-mysql 提供 |
| `CUSTOM` | 自定义存储（内存/自定义持久化），由 storage-custom 提供 |
| `DUAL_WRITE` | 双写模式，写入两种存储，从 CUSTOM 读 |
| `READ_MEMORY_WRITE_DATABASE` | 读 CUSTOM 写 DATABASE |

---

## 5. 集成测试模块

新增 `extension/storage/storage-dual` 模块，同时依赖 storage-mysql 和 storage-custom，包含以下测试：

| 测试类 | 验证内容 |
|--------|---------|
| `DualStorageEngineInitTest` | 两个模块共存时引擎正常初始化 |
| `StorageRouterRegistryTest` | DATABASE 和 CUSTOM 模式各注册了 6 种 Storage |
| `DefaultDatabaseModeTest` | 默认使用 DATABASE 模式 |
| `MemoryModeTest` | 设置默认模式为 CUSTOM |
| `ThreadLocalModeSwitchTest` | ThreadLocal 动态切换 + 覆盖默认模式 |
| `BackwardCompatibilityTest` | StorageRouter 为 null 时回退到原逻辑 |
| `DualModeProcessTest` | **端到端融合测试**：单方法内先 DATABASE 跑审批流程，再 CUSTOM 跑服务任务流程 |

`DualModeProcessTest` 是核心验证用例，证明一个引擎实例、一个 JVM 进程内两种存储模式可以正确共存并动态切换。

---

## 6. 变更文件清单

### 6.1 Core 模块 — 新增文件

| 文件 | 说明 |
|------|------|
| `core/.../storage/StorageMode.java` | 存储模式枚举 |
| `core/.../storage/StorageRouter.java` | 存储路由器 |
| `core/.../storage/StorageRegistry.java` | 存储注册表 |
| `core/.../storage/StorageModeHolder.java` | ThreadLocal 模式持有者 |
| `core/.../storage/StorageContext.java` | 存储上下文 |
| `core/.../storage/StorageStrategy.java` | 存储策略接口 |
| `core/.../storage/strategy/DatabaseStorageStrategy.java` | DATABASE 策略 |
| `core/.../storage/strategy/CustomStorageStrategy.java` | CUSTOM 策略 |
| `core/.../storage/strategy/DualStorageStrategy.java` | 双写策略 |

### 6.2 Core 模块 — 修改文件

| 文件 | 改动 |
|------|------|
| `core/.../extension/constant/ExtensionConstant.java` | 新增 `CUSTOM = "custom"` 常量 |
| `core/.../extension/scanner/SimpleAnnotationScanner.java` | 新增 `storageRouter` 字段 + `getExtensionPoint()` 返回动态代理 + `createStorageProxy()` |
| `core/.../configuration/ProcessEngineConfiguration.java` | 新增 `getStorageRouter()` / `setStorageRouter()` default 方法 |
| `core/.../configuration/impl/DefaultProcessEngineConfiguration.java` | 新增 `storageRouter` 字段 + 构造函数初始化 |
| `core/.../configuration/impl/DefaultSmartEngine.java` | 新增 `initializeStorageRouter()` 方法，在 `init()` 中调用 |

### 6.3 storage-custom 模块 — 修改文件

| 文件 | 改动 |
|------|------|
| `CustomProcessInstanceStorage.java` | `group` 从 `COMMON` 改为 `CUSTOM` |
| `CustomExecutionInstanceStorage.java` | 同上 |
| `CustomActivityInstanceStorage.java` | 同上 |
| `CustomTaskInstanceStorage.java` | 同上 |
| `CustomTaskAssigneeInstanceStorage.java` | 同上 |
| `CustomVariableInstanceStorage.java` | 同上 |

### 6.4 新增模块

| 路径 | 说明 |
|------|------|
| `extension/storage/storage-dual/` | 集成测试模块 |
| `extension/storage/storage-dual/pom.xml` | 依赖 core + storage-mysql + storage-custom |
| `extension/storage/storage-dual/src/test/` | 7 个测试类 + 辅助类 + BPMN + Spring 配置 |

### 6.5 父 POM

| 文件 | 改动 |
|------|------|
| `pom.xml`（根） | 新增 `<module>extension/storage/storage-dual</module>` |

---

## 7. 关键设计决策与陷阱

### 7.1 为什么需要动态代理？

服务类在 `start()` 生命周期中缓存 storage 引用：

```java
this.executionInstanceStorage = scanner.getExtensionPoint("common", ExecutionInstanceStorage.class);
```

如果返回具体实现，切换 `StorageMode` 后缓存引用仍指向旧实现。动态代理在每次方法调用时实时路由。

### 7.2 循环引用陷阱

`StorageRouter.getStorage()` **绝不能** 回退调用 `AnnotationScanner.getExtensionPoint()`，因为 Scanner 会委托回 StorageRouter，造成 StackOverflow。所有 storage 实现在 `initializeStorageRouter()` 中预注册到 registry。

### 7.3 InvocationTargetException 解包

`java.lang.reflect.Proxy` 的 `method.invoke()` 会将异常包装为 `InvocationTargetException`，必须 `catch` 后 `throw e.getCause()` 解包，否则上层收到的异常类型不符预期。

### 7.4 单模块向后兼容

当 classpath 只有一个 storage 模块时，`initializeStorageRouter()` 将其实现注册到**两种模式**下，确保默认模式（DATABASE）总能找到实现。

---

## 8. 验证方式

```bash
# 单 DATABASE 模式不受影响
mvn clean test -pl extension/storage/storage-mysql

# 单 CUSTOM 模式不受影响
mvn clean test -pl extension/storage/storage-custom

# 双模式共存 + 动态切换
mvn clean test -pl extension/storage/storage-dual

# 全量回归
mvn clean test
```
