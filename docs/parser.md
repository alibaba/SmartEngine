# BPMN 解析扩展（ElementParser / AttributeParser）

SmartEngine 在解析 BPMN 时会：

1) 解析标准 BPMN 元素（process/startEvent/serviceTask/gateway…）
2) 解析扩展属性与扩展元素（smart:*、camunda:* 等）
3) 将结果写入 ProcessDefinition / 节点 properties，为执行阶段提供依据

扩展点主要有两类：

- ElementParser：解析某类 BPMN 元素
- AttributeParser：解析某个属性/扩展属性

它们都通过 `@ExtensionBinding` 挂载。

---

## 1. ElementParser

绑定组：`ExtensionConstant.ELEMENT_PARSER`

典型 bindKey：

- 元素类型（serviceTask、receiveTask、parallelGateway…）
- 或你自定义的扩展元素名

实现建议：

- 解析阶段只做“结构化提取”，不要做业务 side effect
- 把扩展结果写入节点 properties（或 extension map）
- 为每个 parser 写单元测试（参考 core 的 parser test）

---

## 2. AttributeParser

绑定组：`ExtensionConstant.ATTRIBUTE_PARSER`

典型 bindKey：

- `smart:class`
- `camunda:class`
- 以及各种 property / listener / expression

实现建议：

- 统一做命名空间兼容（建议复用 magicExtension 的映射）
- 明确解析优先级（priority），避免被其他实现覆盖

---

## 3. 参考用例（仓库）

- 命名空间兼容测试：`core/src/test/java/.../ExtensionNameSpaceParseTest`
- 扩展元素解析测试：`core/src/test/resources/process-def/extend/extend.bpmn20.xml`

如果你要支持更多 BPMN 特性（例如 boundary event、timer、event subprocess），建议：

- 先把解析与执行行为拆开
- parser 只负责把结构抽取出来
- behavior 再消费 parser 产物

