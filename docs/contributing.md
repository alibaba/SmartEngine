# 贡献指南（Contributing）

本文件是团队内贡献的补充说明；以仓库根目录 `CONTRIBUTING.md` 为准。

## 代码与架构原则

- 保持 `core` 依赖轻量，不引入重型框架。
- 新能力优先通过扩展点实现，避免直接修改核心语义。
- 行为或解析变更必须附带测试与示例 BPMN。

## PR 要求

- PR 描述必须写明：背景、方案、兼容性风险、回滚方式、测试结果。
- 涉及数据库变更时必须同步更新 MySQL/PostgreSQL DDL 与 SQLMap，并提供迁移说明。
- 对外行为变化必须同步更新 docs 与变更日志。

## 回归测试建议

- `core`：parser/behavior 全量测试。
- `storage-custom`：并行网关、跳转等语义回归。
- `storage-mysql`：流程启动、任务、变量、并行网关等集成路径。
