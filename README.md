# SmartEngine

SmartEngine is a lightweight business orchestration engine. It's used widely in Alibaba Group.
It can be used to orchestrate multiple service in microservice architecture, start/signal a process instance in a very high-performance way with low-storage cost, and also can be used in traditional process approval scenarios.

## Design Philosophy

0. KISS(Keep It Simple, Stupid)
1. Standardization: embrace BPMN2.0, specify ubiquitous language.
2. Extensible: such as parser,behavior, storage, user integration etc.
3. High Performance: provide a simple way to improve performance and reduce storage cost in some simple process scenarios.
4. Less Dependent: at the very beginning, we try our best to avoid JAR hell.


## Main Feature

0. CQRS-style APIs to start, signal, query process instance, task, activity.
1. Support basic BPMN symbols : StartEvent,EndEvent,SequenceFlow,ExclusiveGateway,ServiceTask,ReceiveTask.
2. Provide a simple way to improve performance and reduce storage cost in some simple process scenarios.
3. Other: Process Jump; VariablePersister; TaskAssigneeDispatcher; Countersign.


## Documentation

- [Documentation Home](https://github.com/alibaba/SmartEngine/wiki)


## License

SmartEngine is released under the Apache 2.0 license.

## Contact


| DingTalk Group Id                | GitHub issues | WeChat Id  |
| ------------------------------ | ------------- | ---------- |
| 31953929 | [Issues]      | geecoodeer |

[issues]: https://github.com/alibaba/SmartEngine/issues

## Thanks

Inspired by Activiti, MyBatis, Netty etc.