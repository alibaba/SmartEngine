# 扩展点总览：parser / behavior / storage / user integration / lock / executor…

SmartEngine 的二次开发权限主要来自：

1) **ExtensionBinding（注解扩展）**：按 group + bindKey 绑定实现  
2) **配置注入（ProcessEngineConfiguration）**：把关键组件换成你的实现  
3) **Storage 接口**：替换持久化实现（Custom/DataBase/混合）

---

## 1. ExtensionBinding：如何“挂载”扩展实现

注解：`core/.../engine/extension/annotation/ExtensionBinding.java`

核心字段：

- `group`：扩展类别（见 `ExtensionConstant`）
- `bindKey`：绑定 key（通常是 BPMN 元素类型、属性名、或行为名）
- `priority`：同一 bindKey 下的优先级（越大越优先）

扩展分组常量见：`core/.../engine/extension/constant/ExtensionConstant.java`

- `element-parser`
- `attribute-parser`
- `activity-behavior`
- 以及 `common` / `SERVICE`

引擎启动时会通过 `AnnotationScanner` 扫描 classpath，构建 ExtensionContainer（扩展容器）。

---

## 2. 最常用的“配置注入”扩展点

这些扩展点不走 ExtensionBinding，而是直接在配置里注入实现：

- `SmartEngine getSmartEngine();`
- `void setSmartEngine(SmartEngine smartEngine);`
- `void setIdGenerator(IdGenerator idGenerator);`
- `IdGenerator getIdGenerator();`
- `void setInstanceAccessor(InstanceAccessor instanceAccessor);`
- `InstanceAccessor getInstanceAccessor();`
- `void setExpressionEvaluator(ExpressionEvaluator expressionEvaluator);`
- `ExpressionEvaluator getExpressionEvaluator();`
- `void setDelegationExecutor(DelegationExecutor delegationExecutor);`
- `DelegationExecutor getDelegationExecutor();`
- `void setListenerExecutor(ListenerExecutor listenerExecutor);`
- `ListenerExecutor getListenerExecutor();`
- `void setAnnotationScanner(AnnotationScanner annotationScanner);`
- `AnnotationScanner getAnnotationScanner();`
- `void setExceptionProcessor(ExceptionProcessor exceptionProcessor);`
- `ExceptionProcessor getExceptionProcessor();`
- `void setParallelServiceOrchestration(ParallelServiceOrchestration parallelServiceOrchestration);`
- `ParallelServiceOrchestration getParallelServiceOrchestration();`
- `void setTaskAssigneeDispatcher(TaskAssigneeDispatcher taskAssigneeDispatcher);`
- `TaskAssigneeDispatcher getTaskAssigneeDispatcher();`
- `void setVariablePersister(VariablePersister variablePersister);`
- `VariablePersister getVariablePersister();`
- `void setMultiInstanceCounter(MultiInstanceCounter multiInstanceCounter);`
- `MultiInstanceCounter getMultiInstanceCounter();`
- `void setLockStrategy(LockStrategy lockStrategy);`
- `LockStrategy getLockStrategy();`
- `void setTableSchemaStrategy(TableSchemaStrategy tableSchemaStrategy);`
- `TableSchemaStrategy getTableSchemaStrategy();`
- `void setExecutorService(ExecutorService executorService);`
- `ExecutorService getExecutorService();`
- `void setExecutorServiceMap(Map<String, ExecutorService> poolsMap);`
- `Map<String, ExecutorService> getExecutorServiceMap();`
- `void setOptionContainer(OptionContainer optionContainer);`
- `OptionContainer getOptionContainer();`
- `void setMagicExtension(Map<String,Object> extension);`
- `Map<String,Object> getMagicExtension();`
- `void setPvmActivityTaskFactory(PvmActivityTaskFactory pvmActivityTaskFactory);`
- `PvmActivityTaskFactory getPvmActivityTaskFactory();`

> 建议阅读 `DefaultProcessEngineConfiguration`，它展示了“引擎默认怎么组装这些权限”。

---

## 3. 典型二开路径（按需求分类）

### 3.1 你想支持更多 BPMN 扩展属性/元素

- 实现 ElementParser / AttributeParser
- 绑定到 `element-parser` / `attribute-parser`
- 参考：`05-extensibility/parser.md`

### 3.2 你想改变某类节点的执行逻辑（例如 ServiceTask）

- 实现/继承 `ActivityBehavior`
- 绑定到 `activity-behavior`
- 参考：`05-extensibility/behaviors.md`

### 3.3 你想替换存储（换 DB、加历史表、做归档）

- 实现 Storage 接口（`core/.../instance/storage/*`）
- 替换 DataBase 模式的 RelationshipDatabase*Storage
- 参考：`05-extensibility/storage.md`

### 3.4 你想把 user/组织/岗位/待办融入你的 IAM/RBAC

- 实现 `TaskAssigneeDispatcher`
- 配置 `InstanceAccessor` 与 `TaskCommandService` 的调用策略
- 参考：`05-extensibility/user-integration.md`

### 3.5 你想加强并行/异步执行权限

- 配置 `ParallelServiceOrchestration` / ExecutorService poolsMap
- 理解 `ParallelGatewayUtil` 的属性语义
- 参考：`05-extensibility/concurrency.md`

