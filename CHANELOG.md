# 2.6.4
1. [微型非兼容升级] 修改IdGenerator 的实现策略, 现在除了实现id策略后,还需要手动给instance 赋值才生效 (使用上有部分退化,为了后续使用表自增主键做准备,以及支持细粒度的id生成策略). 
2. [微型非兼容升级] 修改TaskCommandService#complete的返回值,从void类型改为ProcessInstance

# 2.6.3
1. 微改进,支持了互斥网关的 DefaultSequenceFlow 特性;重构了并行网关下的并发服务编排的逻辑,对可读性做了重点优化.

# 2.6.2
1. [微型非兼容升级]修改TaskAssigneeDispatcher#getTaskAssigneeCandidateInstance 第二个参数,将Map类型修改为Context,便于获得流程定义参数.

# 2.6.1
1. 微改进,允许在执行execute方法时,调用hook逻辑,在特定业务场景下,取消流程环节的推进.

# 2.6.0
1. BUG修复:针对多线程情况下,使用并行网关进行服务编排导致内存泄露的bug. 强烈建议使用该场景的用户进行升级.

# 2.5.7.2
1. 微改进,支持根据流程实例的开始,结束时间进行查询;支持发送流程结束事件.

# 2.5.7.1
1. 微改进,优化了ACTIVITY_START的触发时机,在节点创建后,再触发该事件.

# 2.5.7.0
1. [非兼容升级]仅针对Properties属性,增加PropertyCompositeValue. 

# 2.5.6.x
1. 为分库分表做准备，增加了signal 查询pid参数。

# 2.5.1
1.  [非兼容升级] LockStrategy: 目前流程流转和并行网关join处理都复用了同一个LockStrategy。 这次变更，主要是加入了Context参数，运行外部场景根据context的环节id等值，做些细粒度的锁的策略控制。
 另外，还解决了LockStrategy的子流程和并行网关兼容性问题。

# 2.5.0
1. 支持了并行网关的 anyof 模式，见`ServiceOrchestrationParallelGatewayTest`。

# 2.5.0
1. 支持了Camunda Modeler（使用 https://github.com/camunda/camunda-modeler 4.0.0 这个tag 版本）。可以直接用Camunda Modeler 绘制，导出，然后在系统中无缝使用。   
在这个版本发布中，我们兼容了  smart:class,smart:properties,smart:eventListener 这几个attributes。 

# 2.2.2
0. 完善了父子流程，并行网关的并发服务编排逻辑。
1. [非兼容升级]去掉Mvel表达式引擎的COC约定，改为通过init 传入。两个好处：提升性能，减少多ClassLoader带来的问题。
2. 增加ParallelServiceOrchestration默认实现，支持外部业务灵活扩展。
3. 增加节点多条OutComingTransition来模拟简单的互斥网关，减少画图复杂度。

# 2.2.3
0. BUG修复：针对数据库模式，为se_process_instance 新增了parent_execution_instance_id 字段，用于解决子流程结束后，父流程不自动执行的问题。

# 2.2.3.1
0. 修复transfer 无法正常工作，添加了test作为保障 （果然是不加test，就可能要踩坑啊）
