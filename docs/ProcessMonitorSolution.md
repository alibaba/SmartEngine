# SmartEngine 流程监控日志方案

## 概述

本文档提供基于SmartEngine框架的流程监控日志实现方案，通过组合使用以下两种机制实现：

1. **setter注入** - 通过`ProcessEngineConfiguration`的setter方法注入监控组件
2. **@ExtensionBinding机制** - 对于无法通过setter注入的组件（如TransitionBehavior）

**注意**: 不是所有组件都能通过setter注入，需要根据组件类型选择合适的方式。

---

## 一、监控需求分析

### 1.1 核心监控点

| 监控点 | 说明 | 关键类 |
|--------|------|--------|
| 流程启动/终止 | 流程实例创建与销毁 | `ProcessCommandService.start()/abort()` |
| 节点进入 | 活动节点开始执行 | `AbstractActivityBehavior.enter()` |
| 节点执行 | 节点业务逻辑处理 | `AbstractActivityBehavior.execute()` |
| 节点离开 | 节点执行完毕流向下一节点 | `AbstractActivityBehavior.leave()` |
| 网关决策 | 条件判断与路径选择 | `CommonGatewayHelper.calcMatchedTransitions()` |
| 条件表达式 | 表达式计算结果 | `ExpressionUtil.eval()` |
| 异常捕获 | 错误信息记录 | `ExceptionProcessor` |
| 变量变更 | 流程变量读写 | `VariableInstanceStorage` |

### 1.2 监控数据结构

```java
@Data
public class ProcessMonitorEvent {
    private String eventType;           // START/ENTER/EXECUTE/LEAVE/GATEWAY/ERROR
    private String processInstanceId;   // 流程实例ID
    private String processDefinitionId; // 流程定义ID
    private String activityId;          // 当前活动节点ID
    private String activityType;        // 活动节点类型
    private String transitionId;        // 网关决策时的流转ID
    private String conditionExpression;  // 条件表达式内容
    private Boolean conditionResult;    // 条件判断结果
    private Map<String, Object> inputVariables;   // 输入变量
    private Map<String, Object> outputVariables;  // 输出变量
    private long duration;              // 执行耗时(毫秒)
    private String errorMessage;        // 错误信息
    private LocalDateTime timestamp;    // 时间戳
}
```

---

## 二、监控组件实现

### 2.1 MonitoringDelegationExecutor - 委托执行监控

**作用范围**: 所有ServiceTask、ScriptTask、BusinessRuleTask等委托行为

```java
package com.example.smartengine.monitor;

import com.alibaba.smart.framework.engine.configuration.DelegationExecutor;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitoringDelegationExecutor implements DelegationExecutor {

    private static final Logger MONITOR_LOG = LoggerFactory.getLogger("ProcessMonitor");
    private static final Logger ERROR_LOG = LoggerFactory.getLogger("ProcessError");

    private final DelegationExecutor delegate;

    public MonitoringDelegationExecutor(DelegationExecutor delegate) {
        this.delegate = delegate;
    }

    @Override
    public void execute(ExecutionContext context, Activity activity) {
        String processInstanceId = context.getProcessInstance().getInstanceId();
        String activityId = activity.getId();
        String activityType = activity.getClass().getSimpleName();

        MONITOR_LOG.info("[MONITOR] ActivityStart processInstanceId={} activityId={} activityType={}",
            processInstanceId, activityId, activityType);

        long startTime = System.currentTimeMillis();
        try {
            delegate.execute(context, activity);
            long duration = System.currentTimeMillis() - startTime;

            MONITOR_LOG.info("[MONITOR] ActivityComplete processInstanceId={} activityId={} duration={}ms",
                processInstanceId, activityId, duration);

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            ERROR_LOG.error("[MONITOR] ActivityError processInstanceId={} activityId={} duration={}ms error={}",
                processInstanceId, activityId, duration, e.getMessage(), e);
            throw e;
        }
    }
}
```

### 2.2 MonitoringListenerExecutor - 监听器执行监控

**作用范围**: 执行监听器（start、end、take事件）

```java
package com.example.smartengine.monitor;

import com.alibaba.smart.framework.engine.configuration.ListenerExecutor;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElementContainer;
import com.alibaba.smart.framework.engine.pvm.event.EventConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitoringListenerExecutor implements ListenerExecutor {

    private static final Logger MONITOR_LOG = LoggerFactory.getLogger("ListenerMonitor");

    private final ListenerExecutor delegate;

    public MonitoringListenerExecutor(ListenerExecutor delegate) {
        this.delegate = delegate;
    }

    @Override
    public void execute(EventConstant event, ExtensionElementContainer container, ExecutionContext context) {
        String eventName = event.name();
        String processInstanceId = context.getProcessInstance().getInstanceId();
        String activityId = container.getId();

        MONITOR_LOG.info("[LISTENER] EventStart event={} processInstanceId={} activityId={}",
            eventName, processInstanceId, activityId);

        long startTime = System.currentTimeMillis();
        try {
            delegate.execute(event, container, context);
            long duration = System.currentTimeMillis() - startTime;

            MONITOR_LOG.info("[LISTENER] EventComplete event={} processInstanceId={} activityId={} duration={}ms",
                eventName, processInstanceId, activityId, duration);

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            MONITOR_LOG.error("[LISTENER] EventError event={} processInstanceId={} activityId={} error={}",
                eventName, processInstanceId, activityId, e.getMessage());
            throw e;
        }
    }
}
```

### 2.3 MonitoringExpressionEvaluator - 表达式计算监控

**作用范围**: 所有表达式计算（条件表达式、任务完成条件等）

**监控原理**: `ExpressionUtil`是静态工具类，内部调用`ExpressionEvaluator`，因此需要包装`ExpressionEvaluator`才能监控表达式计算

```java
package com.example.smartengine.monitor;

import com.alibaba.smart.framework.engine.common.expression.evaluator.ExpressionEvaluator;
import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MonitoringExpressionEvaluator implements ExpressionEvaluator {

    private static final Logger EXPR_LOG = LoggerFactory.getLogger("ExpressionMonitor");

    private final ExpressionEvaluator delegate;

    public MonitoringExpressionEvaluator(ExpressionEvaluator delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object eval(String expression, Map<String, Object> requestContext, boolean cacheEnabled) {
        EXPR_LOG.debug("[EXPR] Evaluating expression={} cacheEnabled={}", expression, cacheEnabled);

        Object result = delegate.eval(expression, requestContext, cacheEnabled);

        EXPR_LOG.info("[EXPR] ExpressionResult expression={} result={}", expression, result);

        return result;
    }
}
```

**配置启用**：
```java
config.setExpressionEvaluator(
    new MonitoringExpressionEvaluator(config.getExpressionEvaluator())
);
```

### 2.4 MonitoringExceptionProcessor - 异常处理监控

**作用范围**: 所有流程执行异常

```java
package com.example.smartengine.monitor;

import com.alibaba.smart.framework.engine.configuration.ExceptionProcessor;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitoringExceptionProcessor implements ExceptionProcessor {

    private static final Logger ERROR_LOG = LoggerFactory.getLogger("ProcessError");

    @Override
    public void process(Exception exception, ExecutionContext context) {
        String processInstanceId = "N/A";
        String activityId = "N/A";

        if (context != null && context.getProcessInstance() != null) {
            processInstanceId = context.getProcessInstance().getInstanceId();
        }

        if (context != null && context.getExecutionInstance() != null) {
            activityId = context.getExecutionInstance().getProcessDefinitionActivityId();
        }

        ERROR_LOG.error("[ERROR] ProcessError processInstanceId={} activityId={} error={}",
            processInstanceId, activityId, exception.getMessage(), exception);
    }
}
```

### 2.5 MonitoringVariablePersister - 变量变更监控

**作用范围**: 流程变量读写操作

```java
package com.example.smartengine.monitor;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;
import com.alibaba.smart.framework.engine.persister.variable.VariablePersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MonitoringVariablePersister implements VariablePersister {

    private static final Logger VAR_LOG = LoggerFactory.getLogger("VariableMonitor");

    private final VariablePersister delegate;

    public MonitoringVariablePersister(VariablePersister delegate) {
        this.delegate = delegate;
    }

    @Override
    public void save(VariableInstance variableInstance, ProcessEngineConfiguration config) {
        String processInstanceId = variableInstance.getProcessInstanceId();
        String variableName = variableInstance.getName();
        Object variableValue = variableInstance.getValue();

        VAR_LOG.info("[VAR] Save processInstanceId={} name={} value={}",
            processInstanceId, variableName, variableValue);

        delegate.save(variableInstance, config);
    }

    @Override
    public void save(List<VariableInstance> variableInstances, ProcessEngineConfiguration config) {
        for (VariableInstance variableInstance : variableInstances) {
            save(variableInstance, config);
        }
        delegate.save(variableInstances, config);
    }

    @Override
    public Map<String, Object> findByProcessInstanceId(String processInstanceId, ProcessEngineConfiguration config) {
        VAR_LOG.debug("[VAR] FindByProcessInstanceId processInstanceId={}", processInstanceId);
        return delegate.findByProcessInstanceId(processInstanceId, config);
    }

    @Override
    public void update(VariableInstance variableInstance, ProcessEngineConfiguration config) {
        VAR_LOG.info("[VAR] Update processInstanceId={} name={} newValue={}",
            variableInstance.getProcessInstanceId(), variableInstance.getName(), variableInstance.getValue());
        delegate.update(variableInstance, config);
    }

    @Override
    public void remove(Set<String> names, ProcessEngineConfiguration config) {
        VAR_LOG.info("[VAR] Remove names={}", names);
        delegate.remove(names, config);
    }
}
```

### 2.6 MonitoringSequenceFlowBehavior - 网关决策监控

**作用范围**: ExclusiveGateway、InclusiveGateway的条件判断路径选择

**监控原理**: `CommonGatewayHelper`是静态工具类，无法通过setter注入。需要通过`@ExtensionBinding`机制替换默认的`TransitionBehavior`

```java
package com.example.smartengine.monitor;

import com.alibaba.smart.framework.engine.behavior.TransitionBehavior;
import com.alibaba.smart.framework.engine.behavior.base.AbstractTransitionBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.common.expression.ExpressionUtil;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.ConditionExpression;
import com.alibaba.smart.framework.engine.model.assembly.Transition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = TransitionBehavior.class)
public class MonitoringSequenceFlowBehavior extends AbstractTransitionBehavior<SequenceFlow> {

    private static final Logger GATEWAY_LOG = LoggerFactory.getLogger("GatewayMonitor");

    @Override
    public boolean match(ExecutionContext context, Transition transition) {
        ConditionExpression conditionExpression = transition.getConditionExpression();
        String processInstanceId = context.getProcessInstance().getInstanceId();
        String transitionId = transition.getId();

        if (conditionExpression != null) {
            String expressionContent = conditionExpression.getExpressionContent();
            Boolean result = ExpressionUtil.eval(context, conditionExpression);

            GATEWAY_LOG.info("[GATEWAY] ConditionCheck processInstanceId={} transitionId={} expression={} result={}",
                processInstanceId, transitionId, expressionContent, result);

            return result;
        }

        GATEWAY_LOG.info("[GATEWAY] NoCondition processInstanceId={} transitionId={}",
            processInstanceId, transitionId);

        return true;
    }
}
```

**注意**: 此组件使用`@ExtensionBinding`机制，不能通过setter注入，需要确保包扫描能扫描到此注解类。

---

## 三、配置启用

### 3.1 方式一：编程式配置（推荐）

```java
package com.example.smartengine.config;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.example.smartengine.monitor.*;

public class MonitorEnabledProcessEngineConfiguration {

    public static ProcessEngineConfiguration create() {
        // 创建默认配置
        DefaultProcessEngineConfiguration config = new DefaultProcessEngineConfiguration();

        // 包装 DelegationExecutor - 监控节点执行
        config.setDelegationExecutor(
            new MonitoringDelegationExecutor(config.getDelegationExecutor())
        );

        // 包装 ListenerExecutor - 监控生命周期事件
        config.setListenerExecutor(
            new MonitoringListenerExecutor(config.getListenerExecutor())
        );

        // 设置 ExceptionProcessor - 监控异常信息
        config.setExceptionProcessor(new MonitoringExceptionProcessor());

        // 包装 VariablePersister - 监控变量变更
        config.setVariablePersister(
            new MonitoringVariablePersister(config.getVariablePersister())
        );

        return config;
    }
}
```

### 3.2 方式二：Spring Boot 配置类

```java
package com.example.smartengine.config;

import com.alibaba.smart.framework.engine.configuration.DelegationExecutor;
import com.alibaba.smart.framework.engine.configuration.ListenerExecutor;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.example.smartengine.monitor.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SmartEngineMonitorConfiguration {

    @Bean
    @Primary
    public ProcessEngineConfiguration processEngineConfiguration() {
        DefaultProcessEngineConfiguration config = new DefaultProcessEngineConfiguration();

        // 包装 DelegationExecutor
        config.setDelegationExecutor(
            new MonitoringDelegationExecutor(config.getDelegationExecutor())
        );

        // 包装 ListenerExecutor
        config.setListenerExecutor(
            new MonitoringListenerExecutor(config.getListenerExecutor())
        );

        // 设置 ExceptionProcessor
        config.setExceptionProcessor(new MonitoringExceptionProcessor());

        // 包装 VariablePersister
        config.setVariablePersister(
            new MonitoringVariablePersister(config.getVariablePersister())
        );

        return config;
    }
}
```

### 3.3 方式三：测试基类中配置

```java
package com.example.smartengine.test;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.example.smartengine.monitor.*;
import org.junit.Before;

public abstract class MonitorEnabledTestCase {

    protected ProcessEngineConfiguration processEngineConfiguration;

    @Before
    public void initMonitorConfiguration() {
        DefaultProcessEngineConfiguration config = new DefaultProcessEngineConfiguration();

        // 包装 DelegationExecutor
        config.setDelegationExecutor(
            new MonitoringDelegationExecutor(config.getDelegationExecutor())
        );

        // 包装 ListenerExecutor
        config.setListenerExecutor(
            new MonitoringListenerExecutor(config.getListenerExecutor())
        );

        // 设置 ExceptionProcessor
        config.setExceptionProcessor(new MonitoringExceptionProcessor());

        // 包装 VariablePersister
        config.setVariablePersister(
            new MonitoringVariablePersister(config.getVariablePersister())
        );

        this.processEngineConfiguration = config;
    }
}
```

### 3.4 方式四：扩展DefaultSmartEngine

```java
package com.example.smartengine.config;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.example.smartengine.monitor.*;

public class MonitorEnabledSmartEngine extends DefaultSmartEngine {

    @Override
    protected ProcessEngineConfiguration createProcessEngineConfiguration() {
        ProcessEngineConfiguration config = super.createProcessEngineConfiguration();

        // 包装 DelegationExecutor
        config.setDelegationExecutor(
            new MonitoringDelegationExecutor(config.getDelegationExecutor())
        );

        // 包装 ListenerExecutor
        config.setListenerExecutor(
            new MonitoringListenerExecutor(config.getListenerExecutor())
        );

        // 设置 ExceptionProcessor
        config.setExceptionProcessor(new MonitoringExceptionProcessor());

        // 包装 VariablePersister
        config.setVariablePersister(
            new MonitoringVariablePersister(config.getVariablePersister())
        );

        return config;
    }
}
```

---

## 四、扩展机制说明

### 4.1 两种扩展机制对比

| 机制 | 适用场景 | 优点 | 缺点 |
|------|---------|------|------|
| **setter注入** | 实现了接口的组件（DelegationExecutor、ListenerExecutor等） | 简单直观、可控性强 | 仅适用于有接口的组件 |
| **@ExtensionBinding** | ActivityBehavior、TransitionBehavior等行为类 | 无需setter、自动发现 | 需要包扫描配合、覆盖逻辑较复杂 |

### 4.2 组件扩展方式选择指南

**可以通过setter注入的组件**：
- `DelegationExecutor` - 委托执行器
- `ListenerExecutor` - 监听器执行器
- `ExceptionProcessor` - 异常处理器
- `VariablePersister` - 变量持久化
- `ExpressionEvaluator` - 表达式计算器
- 其他在`ProcessEngineConfiguration`中定义了setter的组件

**必须通过@ExtensionBinding的组件**：
- `TransitionBehavior` - 流转行为（监控网关决策）
- `ActivityBehavior`实现类 - 活动行为（监控特定节点类型）
- `ElementParser`实现类 - 元素解析器
- `Service`实现类 - 服务实现

### 4.3 静态工具类的处理

以下静态工具类无法直接注入，需要间接监控：

| 工具类 | 间接监控方式 |
|--------|-------------|
| `ExpressionUtil` | 包装`ExpressionEvaluator`（`ExpressionUtil`内部调用它） |
| `CommonGatewayHelper` | 通过`TransitionBehavior`的`@ExtensionBinding`机制 |

### 4.4 混合使用示例

```java
public static ProcessEngineConfiguration create() {
    DefaultProcessEngineConfiguration config = new DefaultProcessEngineConfiguration();

    // 1. setter注入方式
    config.setDelegationExecutor(
        new MonitoringDelegationExecutor(config.getDelegationExecutor())
    );
    config.setExpressionEvaluator(
        new MonitoringExpressionEvaluator(config.getExpressionEvaluator())
    );
    config.setExceptionProcessor(new MonitoringExceptionProcessor());

    // 2. @ExtensionBinding方式（通过包扫描自动发现）
    // 无需手动配置，框架会自动扫描@ExtensionBinding注解的类

    return config;
}
```

---

## 五、可配置组件列表

### 4.1 DefaultProcessEngineConfiguration 可用setter

| setter方法 | 默认实现 | 监控作用 |
|------------|---------|---------|
| `setDelegationExecutor` | `DefaultDelegationExecutor` | 监控节点业务逻辑执行 |
| `setListenerExecutor` | `DefaultListenerExecutor` | 监控生命周期事件 |
| `setExceptionProcessor` | `DefaultExceptionProcessor` | 监控异常信息 |
| `setVariablePersister` | `DefaultVariablePersister` | 监控变量读写 |
| `setExpressionEvaluator` | `MvelExpressionEvaluator` | 表达式计算（可扩展监控） |
| `setIdGenerator` | `DefaultIdGenerator` | ID生成监控 |
| `setInstanceAccessor` | `DefaultInstanceAccessor` | 实例访问监控 |
| `setAnnotationScanner` | `SimpleAnnotationScanner` | 注解扫描监控 |
| `setParallelServiceOrchestration` | `DefaultParallelServiceOrchestration` | 并行编排监控 |
| `setTaskAssigneeDispatcher` | - | 任务分配监控 |
| `setMultiInstanceCounter` | - | 会签计数器监控 |
| `setLockStrategy` | - | 锁策略监控 |
| `setExecutorService` | - | 线程池监控 |
| `setStorageRouter` | `StorageRouter` | 存储路由监控 |
| `setDialect` | - | 方言监控 |

### 4.2 配置优先级

```
显式setter > 默认配置
```

当通过setter显式设置组件时，会覆盖框架的默认实现。

---

## 五、性能优化建议

### 5.1 异步日志处理

```java
package com.example.smartengine.monitor;

import com.alibaba.smart.framework.engine.configuration.DelegationExecutor;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncMonitoringDelegationExecutor implements DelegationExecutor {

    private static final Logger MONITOR_LOG = LoggerFactory.getLogger("ProcessMonitor");
    private static final ExecutorService MONITOR_EXECUTOR = Executors.newFixedThreadPool(4);

    private final DelegationExecutor delegate;

    public AsyncMonitoringDelegationExecutor(DelegationExecutor delegate) {
        this.delegate = delegate;
    }

    @Override
    public void execute(ExecutionContext context, Activity activity) {
        String processInstanceId = context.getProcessInstance().getInstanceId();
        String activityId = activity.getId();

        // 异步记录开始日志
        MONITOR_EXECUTOR.submit(() ->
            MONITOR_LOG.info("[MONITOR] ActivityStart processInstanceId={} activityId={}",
                processInstanceId, activityId)
        );

        long startTime = System.currentTimeMillis();
        try {
            delegate.execute(context, activity);
            long duration = System.currentTimeMillis() - startTime;

            // 异步记录完成日志
            MONITOR_EXECUTOR.submit(() ->
                MONITOR_LOG.info("[MONITOR] ActivityComplete processInstanceId={} activityId={} duration={}ms",
                    processInstanceId, activityId, duration)
            );

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            MONITOR_LOG.error("[MONITOR] ActivityError processInstanceId={} activityId={} duration={}ms error={}",
                processInstanceId, activityId, duration, e.getMessage(), e);
            throw e;
        }
    }
}
```

### 5.2 采样策略

```java
package com.example.smartengine.monitor;

public class SamplingMonitorConfig {

    private static final double DEFAULT_SAMPLE_RATE = 0.1; // 默认10%采样

    private final double sampleRate;

    public SamplingMonitorConfig(double sampleRate) {
        this.sampleRate = sampleRate;
    }

    public SamplingMonitorConfig() {
        this(DEFAULT_SAMPLE_RATE);
    }

    public boolean shouldSample() {
        return Math.random() < sampleRate;
    }

    public static SamplingMonitorConfig createByRate(double rate) {
        return new SamplingMonitorConfig(rate);
    }
}
```

### 5.3 日志级别控制

```yaml
# application.yml
logging:
  level:
    root: INFO
    ProcessMonitor: DEBUG
    ListenerMonitor: DEBUG
    ExpressionMonitor: DEBUG
    VariableMonitor: DEBUG
    ProcessError: ERROR
```

### 5.4 敏感数据脱敏

```java
package com.example.smartengine.monitor;

import java.util.*;

public class VariableMasker {

    private static final Set<String> SENSITIVE_KEYS = new HashSet<>(Arrays.asList(
        "password", "secret", "token", "creditCard", "cvv"
    ));

    public static Map<String, Object> maskSensitiveData(Map<String, Object> variables) {
        if (variables == null) {
            return null;
        }
        Map<String, Object> masked = new HashMap<>(variables);
        for (String key : SENSITIVE_KEYS) {
            if (masked.containsKey(key)) {
                masked.put(key, "***MASKED***");
            }
        }
        return masked;
    }

    public static boolean isSensitiveKey(String key) {
        return SENSITIVE_KEYS.stream().anyMatch(key::toLowerCase);
    }
}
```

### 5.5 批量日志发送

```java
package com.example.smartengine.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class BatchMonitorLog {

    private static final Logger BATCH_LOG = LoggerFactory.getLogger("BatchMonitor");
    private static final int BATCH_SIZE = 100;
    private static final long FLUSH_INTERVAL_MS = 5000;

    private final Queue<ProcessMonitorEvent> eventQueue = new LinkedBlockingQueue<>(10000);
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public BatchMonitorLog() {
        scheduler.scheduleAtFixedRate(this::flush, FLUSH_INTERVAL_MS, FLUSH_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }

    public void addEvent(ProcessMonitorEvent event) {
        eventQueue.offer(event);
        if (eventQueue.size() >= BATCH_SIZE) {
            flush();
        }
    }

    private synchronized void flush() {
        if (eventQueue.isEmpty()) {
            return;
        }

        List<ProcessMonitorEvent> batch = new ArrayList<>();
        eventQueue.drainTo(batch, BATCH_SIZE);

        if (!batch.isEmpty()) {
            BATCH_LOG.info("[BATCH_MONITOR] BatchSize={} Events={}", batch.size(), batch);
        }
    }
}
```

---

## 六、监控日志示例

### 6.1 流程启动日志

```
[MONITOR] ActivityStart processInstanceId=PI_123456 activityId=start_event activityType=StartEventBehavior
[MONITOR] ActivityComplete processInstanceId=PI_123456 activityId=start_event duration=1ms
```

### 6.2 节点执行日志

```
[MONITOR] ActivityStart processInstanceId=PI_123456 activityId=validate_order activityType=ServiceTaskBehavior
[MONITOR] ActivityComplete processInstanceId=PI_123456 activityId=validate_order duration=15ms
```

### 6.3 网关决策日志

```
[EXPR] Evaluating type=uel expression=order.amount > 1000 variables={order={amount=1500}}
[EXPR] ExpressionResult type=uel expression=order.amount > 1000 result=true
```

### 6.4 变量变更日志

```
[VAR] Save processInstanceId=PI_123456 name=orderStatus value=APPROVED
[VAR] Update processInstanceId=PI_123456 name=orderAmount newValue=1500.00
```

### 6.5 异常日志

```
[ERROR] ProcessError processInstanceId=PI_123456 activityId=calculate_discount error=NullPointerException
java.lang.NullPointerException
    at com.example.DiscountCalculator.calculate(DiscountCalculator.java:45)
    at com.alibaba.smart.framework.engine.delegation.JavaDelegation.execute(JavaDelegation.java:32)
    ...
```

---

## 七、集成建议

### 7.1 日志收集

推荐使用ELK（Elasticsearch + Logstash + Kibana）或EFK（Elasticsearch + Fluentd + Kibana）收集和查询监控日志。

### 7.2 链路追踪

可集成SkyWalking或Zipkin实现分布式链路追踪：

```java
// 添加TraceID
String traceId = Tracer.currentSpan().getTraceId();
MONITOR_LOG.info("[MONITOR] ActivityStart traceId={} processInstanceId={} ...",
    traceId, processInstanceId);
```

### 7.3 告警规则

建议配置以下告警规则：

| 规则 | 阈值 | 级别 |
|------|------|------|
| 流程执行超时 | 单个节点 > 30s | WARNING |
| 流程执行超时 | 单个节点 > 60s | CRITICAL |
| 节点执行失败 | 连续3次 | WARNING |
| 异常重复出现 | 同一流程实例 > 5次 | CRITICAL |
| 网关决策异常 | 无匹配路径 | CRITICAL |

---

## 八、完整配置示例

```java
package com.example.smartengine.config;

import com.alibaba.smart.framework.engine.configuration.DelegationExecutor;
import com.alibaba.smart.framework.engine.configuration.ExceptionProcessor;
import com.alibaba.smart.framework.engine.configuration.ListenerExecutor;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.persister.variable.VariablePersister;
import com.example.smartengine.monitor.*;

public class CompleteMonitorConfiguration {

    public static ProcessEngineConfiguration create() {
        DefaultProcessEngineConfiguration config = new DefaultProcessEngineConfiguration();

        // 1. 包装 DelegationExecutor - 监控所有委托行为
        config.setDelegationExecutor(
            new MonitoringDelegationExecutor(config.getDelegationExecutor())
        );

        // 2. 包装 ListenerExecutor - 监控生命周期事件
        config.setListenerExecutor(
            new MonitoringListenerExecutor(config.getListenerExecutor())
        );

        // 3. 设置 ExceptionProcessor - 监控异常
        config.setExceptionProcessor(new MonitoringExceptionProcessor());

        // 4. 包装 VariablePersister - 监控变量
        config.setVariablePersister(
            new MonitoringVariablePersister(config.getVariablePersister())
        );

        // 5. 可选：设置自定义ID生成器
        // config.setIdGenerator(new CustomIdGenerator());

        // 6. 可选：设置自定义表达式计算器（带监控）
        // config.setExpressionEvaluator(new MonitoringExpressionEvaluator(config.getExpressionEvaluator()));

        return config;
    }
}
```

---

## 九、总结

本方案组合使用`setter注入`和`@ExtensionBinding`两种机制，实现完整的流程监控。

### 监控组件与扩展机制对应关系

| 监控目标 | 扩展机制 | 配置方式 |
|---------|---------|---------|
| 委托执行（ServiceTask等） | setter注入 | `config.setDelegationExecutor()` |
| 生命周期事件监听 | setter注入 | `config.setListenerExecutor()` |
| 异常处理 | setter注入 | `config.setExceptionProcessor()` |
| 变量读写 | setter注入 | `config.setVariablePersister()` |
| 表达式计算 | setter注入 | `config.setExpressionEvaluator()` |
| 网关条件决策 | @ExtensionBinding | `@ExtensionBinding(group="ACTIVITY_BEHAVIOR", bindKey=TransitionBehavior.class)` |

### 核心要点

1. **setter注入** - 适用于实现了接口的组件（DelegationExecutor、ListenerExecutor等）
2. **@ExtensionBinding** - 适用于ActivityBehavior、TransitionBehavior等行为类
3. **静态工具类处理** - ExpressionUtil需包装ExpressionEvaluator，CommonGatewayHelper需通过TransitionBehavior监控
4. **委托模式** - 使用装饰器模式包装原有组件，保持原有功能的同时添加监控逻辑
5. **职责分离** - 监控组件与业务组件解耦，便于维护和测试

### 推荐配置组合

```java
// 编程式配置（setter注入）
config.setDelegationExecutor(new MonitoringDelegationExecutor(config.getDelegationExecutor()));
config.setExpressionEvaluator(new MonitoringExpressionEvaluator(config.getExpressionEvaluator()));
config.setListenerExecutor(new MonitoringListenerExecutor(config.getListenerExecutor()));
config.setExceptionProcessor(new MonitoringExceptionProcessor());
config.setVariablePersister(new MonitoringVariablePersister(config.getVariablePersister()));

// @ExtensionBinding方式（网关决策监控）
// 添加 @ExtensionBinding 注解的 MonitoringSequenceFlowBehavior 类
// 确保包扫描能发现此注解类
```
