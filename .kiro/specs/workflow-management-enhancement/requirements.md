# 工作流管理系统增强功能需求文档

## 介绍

基于现有的SmartEngine工作流引擎，扩展实现完整的工作流管理功能。SmartEngine已具备基础的流程启动、任务处理、流程终止等核心功能，本需求旨在补充和增强业务场景所需的高级功能。

## 术语表

- **SmartEngine**: 现有的工作流引擎核心
- **TaskInstance**: 任务实例，代表需要处理的工作项
- **ProcessInstance**: 流程实例，代表一个完整的业务流程  
- **TaskCommandService**: 任务操作服务，已支持complete、transfer等基础操作
- **TaskQueryService**: 任务查询服务，已支持待办任务查询和通用任务查询
- **ExecutionCommandService**: 执行控制服务，已支持jumpTo/jumpFrom等跳转功能
- **ProcessCommandService**: 流程控制服务，已支持start、abort等操作
- **Supervisor**: 督办人，负责跟踪和催办任务
- **Notification**: 知会通知，仅告知不需处理

## 需求

### 需求 1: 已办任务查询增强

**用户故事:** 作为系统用户，我想查看我已经处理过的任务，以便跟踪我的工作历史和处理记录。

#### 验收标准

1. WHEN 用户查询已办任务 THEN 系统 SHALL 基于TaskQueryService.findList()返回状态为completed的任务列表
2. WHEN 查询已办任务 THEN 系统 SHALL 显示任务标题、完成时间、处理意见和流程状态
3. WHEN 已办任务列表为空 THEN 系统 SHALL 返回空列表而不是错误
4. WHEN 查询已办任务 THEN 系统 SHALL 支持按时间范围、流程类型等条件筛选
5. WHEN 查询已办任务 THEN 系统 SHALL 支持分页查询以处理大量数据

### 需求 2: 办结流程查询增强

**用户故事:** 作为系统用户，我想查看已经完成的流程，以便了解业务处理结果和归档记录。

#### 验收标准

1. WHEN 用户查询办结流程 THEN 系统 SHALL 基于ProcessQueryService.findList()返回状态为completed的流程列表
2. WHEN 查询办结流程 THEN 系统 SHALL 显示流程标题、完成时间、发起人和最终状态
3. WHEN 办结流程列表为空 THEN 系统 SHALL 返回空列表而不是错误
4. WHEN 查询办结流程 THEN 系统 SHALL 支持按完成时间、流程类型筛选
5. WHEN 查询办结流程 THEN 系统 SHALL 支持分页查询

### 需求 3: 督办管理

**用户故事:** 作为流程管理员，我想对超时或重要的任务进行督办，以便确保业务及时处理。

#### 验收标准

1. WHEN 管理员发起督办 THEN 系统 SHALL 向任务处理人发送催办通知
2. WHEN 督办任务 THEN 系统 SHALL 记录督办时间、督办人和督办原因
3. WHEN 查询督办记录 THEN 系统 SHALL 显示所有督办历史和处理状态
4. WHEN 任务被督办 THEN 系统 SHALL 提高任务优先级
5. WHEN 督办任务已完成 THEN 系统 SHALL 自动关闭督办状态

### 需求 4: 加签功能增强

**用户故事:** 作为任务处理人，我想在当前节点临时增加审批人，以便获得更多意见或分担责任。

#### 验收标准

1. WHEN 用户发起加签 THEN 系统 SHALL 基于TaskCommandService.addTaskAssigneeCandidate()在当前节点增加指定的审批人
2. WHEN 加签完成 THEN 系统 SHALL 通知被加签人处理任务
3. WHEN 所有加签人完成处理 THEN 系统 SHALL 继续原流程流转
4. WHEN 加签任务 THEN 系统 SHALL 记录加签发起人、被加签人和加签原因
5. WHEN 加签人处理任务 THEN 系统 SHALL 支持同意、拒绝等操作

### 需求 5: 流程回退增强

**用户故事:** 作为任务处理人，我想将流程退回到上一步，以便重新处理或补充信息。

#### 验收标准

1. WHEN 用户选择回退 THEN 系统 SHALL 基于ExecutionCommandService.jumpTo()将流程退回到上一个节点
2. WHEN 流程回退 THEN 系统 SHALL 通知上一节点处理人重新处理
3. WHEN 流程回退 THEN 系统 SHALL 记录回退原因和回退时间
4. WHEN 流程回退 THEN 系统 SHALL 保留原有的处理记录和意见
5. WHEN 流程在起始节点 THEN 系统 SHALL 禁止回退操作

### 需求 6: 指定节点回退增强

**用户故事:** 作为任务处理人，我想将流程退回到任意指定的历史节点，以便从特定环节重新开始。

#### 验收标准

1. WHEN 用户选择指定回退 THEN 系统 SHALL 基于ActivityQueryService.findAll()显示可回退的历史节点列表
2. WHEN 确认指定回退 THEN 系统 SHALL 使用ExecutionCommandService.jumpTo()将流程退回到指定节点
3. WHEN 指定回退完成 THEN 系统 SHALL 通知目标节点处理人
4. WHEN 指定回退 THEN 系统 SHALL 清除回退节点之后的所有处理记录
5. WHEN 指定回退 THEN 系统 SHALL 记录回退路径和回退原因

### 需求 7: 知会抄送功能

**用户故事:** 作为流程参与者，我想向相关人员发送知会抄送，以便让他们了解流程进展但不需要处理。

#### 验收标准

1. WHEN 用户发起知会抄送 THEN 系统 SHALL 向指定人员发送通知消息
2. WHEN 接收知会抄送 THEN 系统 SHALL 在知会列表中显示通知内容
3. WHEN 知会抄送 THEN 系统 SHALL 不影响正常流程流转
4. WHEN 查看知会 THEN 系统 SHALL 标记为已读状态
5. WHEN 知会列表 THEN 系统 SHALL 支持按时间、发送人筛选

### 需求 8: 减签功能增强

**用户故事:** 作为任务处理人，我想移除不必要的审批人，以便简化流程和提高效率。

#### 验收标准

1. WHEN 用户发起减签 THEN 系统 SHALL 基于TaskCommandService.removeTaskAssigneeCandidate()从当前节点移除指定的审批人
2. WHEN 减签完成 THEN 系统 SHALL 通知被减签人任务已取消
3. WHEN 减签后 THEN 系统 SHALL 根据剩余人员重新计算会签规则
4. WHEN 减签操作 THEN 系统 SHALL 记录减签发起人、被减签人和减签原因
5. WHEN 所有人被减签 THEN 系统 SHALL 禁止操作并提示错误

### 需求 9: 任务移交增强

**用户故事:** 作为任务处理人，我想将任务移交给其他人处理，并支持移交原因和时限设置。

#### 验收标准

1. WHEN 用户移交任务 THEN 系统 SHALL 基于TaskCommandService.transfer()将任务转给指定的接收人
2. WHEN 任务移交 THEN 系统 SHALL 扩展transfer方法支持记录移交原因和移交时间
3. WHEN 任务移交 THEN 系统 SHALL 通知接收人有新任务待处理
4. WHEN 移交任务 THEN 系统 SHALL 支持设置处理时限
5. WHEN 查询移交记录 THEN 系统 SHALL 显示完整的移交链路