# Quickstart（Custom 模式）— 10 分钟跑通

本章目标：在不依赖数据库（或不依赖引擎自带表结构）的前提下，完成一次 **deploy → start → signal → query** 的最小闭环。

> 对应源码参考：`extension/storage/storage-custom/src/test/java/.../CustomBaseTestCase` 及其子测试用例。

## 1. 引入依赖

Maven 示例（按需选择版本）：

```xml
<dependency>
  <groupId>com.alibaba.smart.framework</groupId>
  <artifactId>smart-engine-core</artifactId>
  <version>3.7.0-SNAPSHOT</version>
</dependency>

<!-- Custom 存储示例实现（可选；生产环境通常你会自己实现 Storage 接口） -->
<dependency>
  <groupId>com.alibaba.smart.framework</groupId>
  <artifactId>smart-engine-extension-storage-custom</artifactId>
  <version>3.7.0-SNAPSHOT</version>
</dependency>
```

## 2. 准备一份最小 BPMN

下面是一个最小流程：Start → ServiceTask → End。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
             xmlns:smart="http://smartengine.org/schema/process"
             targetNamespace="Examples">

  <process id="demo" isExecutable="true">
    <startEvent id="start"/>
    <sequenceFlow id="flow1" sourceRef="start" targetRef="task"/>

    <!-- 业务委派：smart:class 指向你的业务类（也可以用 InstanceAccessor 映射 beanName） -->
    <serviceTask id="task" name="DoSomething" smart:class="com.example.workflow.DemoDelegation"/>

    <sequenceFlow id="flow2" sourceRef="task" targetRef="end"/>
    <endEvent id="end"/>
  </process>
</definitions>
```

说明：

- `smart:class` 会触发引擎在执行节点时，通过 `DelegationExecutor` 调用你的业务类（详见 `05-extensibility/behaviors.md`）。
- SmartEngine 还做了命名空间兼容（Camunda/Flowable/Activiti），见 `DefaultProcessEngineConfiguration` 的 magicExtension fallback（`02-concepts/bpmn-subset.md`）。

## 3. 初始化引擎（核心步骤）

Custom 模式你通常至少要提供：

- `ProcessEngineConfiguration`：引擎配置
- `InstanceAccessor`：引擎如何拿到业务对象/bean/类实例（用于 delegation、listener 等）

最小可运行示例（纯 Java）：

```java
import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultSmartEngine;

public class Demo {
  public static void main(String[] args) {
    ProcessEngineConfiguration cfg = new DefaultProcessEngineConfiguration();

    // Custom 模式下非常关键：引擎如何拿到你的业务类/Bean
    cfg.setInstanceAccessor(classOrBeanName -> {
      try {
        return Thread.currentThread().getContextClassLoader()
          .loadClass(classOrBeanName).getDeclaredConstructor().newInstance();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    SmartEngine engine = new DefaultSmartEngine();
    engine.init(cfg);

    // 1) deploy（解析到本地内存）
    engine.getRepositoryCommandService().deploy("demo.bpmn20.xml");

    // 2) start
    var pi = engine.getProcessCommandService()
      .start("demo", "1.0.0", null, null);

    // 3) query active executions
    var executions = engine.getExecutionQueryService()
      .findActiveExecutionList(pi.getInstanceId());

    // 4) signal：对 ReceiveTask / 暂停点推进（ServiceTask 一般不需要外部 signal）
    // engine.getExecutionCommandService().signal(executions.get(0).getInstanceId(), null);
  }
}
```

> 说明：`RepositoryCommandService.deploy(...)` 负责解析 XML 并加载到内存。集群环境下需要自行做一致性策略（见 `00-intro/faq.md`）。

## 4. 常用 API（你会最先用到的）

- `RepositoryCommandService`：部署（解析）流程定义
- `ProcessCommandService`：start / abort / suspend 等
- `ExecutionCommandService`：signal / jump / markDone / retry 等
- `*QueryService`：查询实例、任务、变量等

完整 API 列表见：`03-usage/api-guide.md`

## 5. Custom 存储怎么做？

SmartEngine 把持久化抽象成 Storage 接口（位于 `core/.../instance/storage/*`）。你有两条路：

1) **完全自己实现**（推荐生产做法）：对接你的数据库/缓存/事件日志等。  
2) **参考仓库的 Custom 存储示例**：`extension/storage/storage-custom` 通过 `PersisterSession`（线程内 Map）模拟持久化，主要用于单元测试与理解接口边界。

下一步建议阅读：

- `04-persistence/storage-overview.md`
- `05-extensibility/storage.md`
