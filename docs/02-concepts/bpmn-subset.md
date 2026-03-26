# 支持的 BPMN 子集与 Smart 扩展

本章回答两个问题：

1) SmartEngine 识别/执行哪些 BPMN 元素？  
2) SmartEngine 的扩展（smart:xxx）怎么用？与 Camunda/Flowable/Activiti 扩展是否兼容？

---

## 1. BPMN NameSpace 与兼容策略

SmartEngine 默认 BPMN namespace：

- BPMN：`String NAME_SPACE = "http://www.omg.org/spec/BPMN/20100524/MODEL";`（见 `BpmnNameSpaceConstant.NAME_SPACE`）

兼容扩展 namespace（常见于 `camunda:class` / `flowable:class` / `activiti:class`）：

- `http://camunda.org/schema/1.0/bpmn`
- `http://flowable.org/bpmn`
- `http://activiti.org/bpmn`

> 代码位置：`core/.../bpmn/constant/BpmnNameSpaceConstant.java`。

### 1.1 “magicExtension fallback”

`DefaultProcessEngineConfiguration` 中维护了一个“命名空间 fallback 映射”，用于把不同生态的扩展属性映射到 SmartEngine 识别的 key（例如把 camunda/flowable/activiti 的 class 属性映射为统一的 `smart:class` 语义）。

这让你可以在迁移现有 BPMN 时，减少改造成本。

---

## 2. SmartEngine 内置的元素/行为实现

SmartEngine 的执行以 `ActivityBehavior` 为核心抽象，内置实现主要来自：

- `core/.../engine/behavior/impl/*`（任务类）
- `core/.../engine/bpmn/behavior/*`（事件、网关、sequence flow、callActivity）

### 2.1 事件（Event）

- StartEvent：`StartEventBehavior`
- EndEvent：`EndEventBehavior`

### 2.2 Sequence Flow

- SequenceFlow：`SequenceFlowBehavior`

### 2.3 网关（Gateway）

- ExclusiveGateway：`ExclusiveGatewayBehavior`
- ParallelGateway：`ParallelGatewayBehavior`
- InclusiveGateway：`InclusiveGatewayBehavior`

### 2.4 任务（Task）

`core/.../engine/behavior/impl` 内置了多种 TaskBehavior：

- ServiceTask：`ServiceTaskBehavior`
- ReceiveTask：`ReceiveTaskBehavior`
- UserTask：`UserTaskBehavior`（DataBase 模式下结合任务表/分派表更完整）
- 以及 Script/Send/Manual/BusinessRule 等行为类（是否启用取决于你 BPMN 与扩展配置）

---

## 3. Smart 扩展：smart:class / smart:executionListener 等

SmartEngine 的扩展 namespace 常量见：

- `core/.../constant/SmartBase.SMART_NS`（默认：`http://smartengine.org/schema/process`）

你在 BPMN 中常见的扩展用法包括：

### 3.1 smart:class（业务委派）

用于 ServiceTask 等节点执行时，调用你的业务代码。

示意：

```xml
<serviceTask id="task1" smart:class="com.example.workflow.PayOrderDelegation"/>
```

引擎执行时大致流程：

1) 解析阶段把 `smart:class` 记录到节点属性（properties/extension）
2) 执行阶段由 `DelegationExecutor` 通过 `InstanceAccessor` 获取实例并调用
3) 异常由 `ExceptionProcessor` 处理；可结合 retry 扩展

### 3.2 executionListener（执行监听）

在 BPMN 的 `<extensionElements>` 中声明监听器，例如：

```xml
<extensionElements>
  <smart:executionListener event="ACTIVITY_START"
                           class="com.example.workflow.MyListener"/>
</extensionElements>
```

监听器执行由 `ListenerExecutor` 统一调度。

> 仓库示例：`storage-custom/src/test/resources/DelegationAndListenerExecutorExtensionTest.xml`

---

## 4. 建模建议（先看这一条）

- 尽量在同一套规范下管理扩展：  
  - 你可以统一使用 `smart:*`  
  - 或沿用 Camunda/Flowable/Activiti 的扩展属性，但要明确团队约定
- 如果你在做“流程建模工具/低代码设计器”，建议把扩展属性抽象成“可配置项”，而不是直接散落在 XML 里（仓库中 `ecology/designer/*` 有相关基础类）。

下一步：

- 执行模型（token / execution / signal）：`execution-model.md`
- 扩展点总览：`05-extensibility/overview.md`

