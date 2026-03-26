# 贡献指南（Contributing）

本指南面向团队内二开/贡献场景，建议与你仓库根目录的 `CONTRIBUTING` 保持一致（若存在）。

---

## 1. 代码风格与原则

- 保持 core 依赖轻：不要在 core 引入重型框架
- 新增权限优先通过扩展点实现，而不是改动核心语义
- 每个新特性必须配套：
  - 单元测试 / 集成测试
  - 示例 BPMN（如涉及解析/行为）
  - 文档（建议同步更新本 docs 目录）

---

## 2. PR 要求（建议）

- PR 描述必须包含：
  - 背景/问题
  - 方案
  - 风险与兼容性
  - 测试用例与结果
- 如果涉及数据库变更：
  - 同步更新 MySQL 与 PostgreSQL DDL
  - 同步更新 SQLMap
  - 增加迁移说明

---

## 3. 回归测试清单（推荐）

- core：parser/behavior 全量测试
- storage-custom：并行网关 + 跳转测试
- storage-mysql：流程启动 + userTask + 变量 + 并行网关（至少一套）

---

## 4. 文档同步原则

- 任何对外行为变更，必须同步更新：
  - `02-concepts/`（概念/语义）
  - `03-usage/`（用法）
  - `04-persistence/`（表结构/SQL）
  - `05-extensibility/`（扩展点）

