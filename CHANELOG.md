#2.5.1
1. LockStrategy 不兼容升级。 目前流程流转和并行网关join处理都复用了同一个LockStrategy。 这次变更，主要是加入了Context参数，运行外部场景根据context的环节id等值，做些细粒度的锁的策略控制。
 另外，还解决了LockStrategy的子流程和并行网关兼容性问题。

#2.5.0
1. 支持了并行网关的 anyof 模式，见`ServiceOrchestrationParallelGatewayTest`。

#2.5.0
1. 支持了Camunda Modeler（使用 https://github.com/camunda/camunda-modeler 4.0.0 这个tag 版本）。可以直接用Camunda Modeler 绘制，导出，然后在系统中无缝使用。   
在这个版本发布中，我们兼容了  smart:class,smart:properties,smart:eventListener 这几个attributes。 

#2.2.2
0. 完善了父子流程，并行网关的并发服务编排逻辑。
1. 去掉Mvel表达式引擎的COC约定，改为通过init 传入。两个好处：提升性能，减少多ClassLoader带来的问题 --不兼容老特性。
2. 增加ParallelServiceOrchestration默认实现，支持外部业务灵活扩展。
3. 增加节点多条OutComingTransition来模拟简单的互斥网关，减少画图复杂度。。

#2.2.3
0. BUG修复：针对数据库模式，为se_process_instance 新增了parent_execution_instance_id 字段，用于解决子流程结束后，父流程不自动执行的问题。

#2.2.3.1
0. 修复transfer 无法正常工作，添加了test作为保障 （果然是不加test，就可能要踩坑啊）
