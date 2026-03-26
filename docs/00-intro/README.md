# SmartEngine 开源项目文档（全新整理）

> 基于 SmartEngine dev 分支源码（含单元测试）整理。版本参考：`3.7.0-SNAPSHOT`（见根 `pom.xml`）。

## 这份文档写给谁

- **业务系统开发者**：想把 SmartEngine 嵌入现有系统，快速跑通编排/流程/待办。
- **平台/中台团队**：需要理解执行模型、并行网关并发语义、存储与扩展点，做二次开发。
- **贡献者**：需要模块结构、构建测试方式、扩展点规范与发布流程。

## 你该从哪里读起

- 只想**10 分钟跑通**  
  - Custom 模式：`01-getting-started/quickstart-custom.md`  
  - DataBase 模式：`01-getting-started/quickstart-database.md`
- 想先理解**概念与边界**：`02-concepts/`
- 想看**API 用法/变量/跳转/并发**：`03-usage/`
- 想落地**MySQL/PostgreSQL + MyBatis**：`04-persistence/`
- 想做**扩展/二开**：`05-extensibility/`
- 想做**运维与治理**：`06-ops/`
- 想参与**贡献/发布**：`07-dev/`

## 文档结构

本仓库文档按目录组织：

- `00-intro/`：入口、术语、FAQ
- `01-getting-started/`：快速上手（Custom / DataBase）与示例索引
- `02-concepts/`：模式、BPMN 子集、执行模型、领域模型
- `03-usage/`：建模指南、API 指南、变量与高级特性
- `04-persistence/`：存储分层、表结构、MyBatis 注意事项
- `05-extensibility/`：扩展点、解析器、行为、存储、用户集成与并发
- `06-ops/`：性能、失败处理、监控、数据保留
- `07-dev/`：架构、构建测试、贡献、发布

## 重要提示（先读这条）

1. **SmartEngine 是“轻量业务编排引擎”**：核心接口以 CQRS 风格拆分为 *Command/Query* 服务（见 `com.alibaba.smart.framework.engine.SmartEngine`）。
2. **两种运行模式**：  
   - **Custom 模式**：偏“嵌入式 + 自定义存储”，适合你已经有自己的持久化/任务体系。  
   - **DataBase 模式**：引擎提供一套关系型存储（MyBatis），并可支持 userTask 待办等增强权限。
3. **集群一致性**：RepositoryCommandService 将流程定义加载到 **本地内存**（单机）。在集群环境必须自行设计“部署/缓存一致性策略”（见 `02-concepts/modes.md` & `00-intro/faq.md`）。

## 快速索引

- 术语：`00-intro/glossary.md`
- FAQ：`00-intro/faq.md`
- 支持的 BPMN 子集：`02-concepts/bpmn-subset.md`
- 执行模型（token/执行实例/并发语义）：`02-concepts/execution-model.md`
- API 全览（start/signal/query/abort/jump/retry）：`03-usage/api-guide.md`
- 数据库表结构：`04-persistence/database-schema.md`

---

生成日期：2025-12-24
