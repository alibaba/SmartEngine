
#2.2.2
1. 去掉Mvel表达式引擎的COC约定，改为通过init 传入。两个好处：提升性能，减少多ClassLoader带来的问题 --不兼容老特性。
2. 增加ParallelServiceOrchestration默认实现，支持外部业务灵活扩展。
3. 

