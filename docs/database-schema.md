# DataBase 模式表结构/索引/清理策略

本章包含 3 部分：

1) SmartEngine 核心表（process/execution/activity/task/variable/deployment）  
2) 工作流增强表（通知/督办/转派/回退等）  
3) 索引与清理策略建议  

---

## 1. 核心表（DDL）

### 1.1 MySQL 版（schema.sql）

```sql


CREATE TABLE `se_deployment_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK'  ,
  `gmt_create` datetime(6) NOT NULL   COMMENT 'create time' ,
  `gmt_modified` datetime(6) NOT NULL  COMMENT 'modification time'  ,
  `process_definition_id` varchar(255) NOT NULL  COMMENT 'process definition id'  ,
  `process_definition_version` varchar(255) DEFAULT NULL  COMMENT 'process definition version'  ,
  `process_definition_type` varchar(255) DEFAULT NULL  COMMENT 'process definition type'  ,
  `process_definition_code` varchar(255) DEFAULT NULL  COMMENT 'process definition code'  ,
  `process_definition_name` varchar(255) DEFAULT NULL  COMMENT 'process definition name'  ,
  `process_definition_desc` varchar(255) DEFAULT NULL  COMMENT 'process definition desc'  ,
  `process_definition_content` mediumtext NOT NULL  COMMENT 'process definition content'  ,
  `deployment_user_id` varchar(128) NOT NULL  COMMENT 'deployment user id' ,
  `deployment_status` varchar(64) NOT NULL   COMMENT 'deployment status' ,
  `logic_status` varchar(64) NOT NULL  COMMENT 'logic status' ,
  `extension` mediumtext DEFAULT NULL  COMMENT 'extension' ,
  `tenant_id` varchar(64) DEFAULT NULL  COMMENT 'tenant id' ,

  PRIMARY KEY (`id`)
)  ;

CREATE TABLE `se_process_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT  COMMENT 'PK'  ,
  `gmt_create` datetime(6) NOT NULL  COMMENT 'create time' ,
  `gmt_modified` datetime(6) NOT NULL  COMMENT 'modification time' ,
  `process_definition_id_and_version` varchar(128) NOT NULL  COMMENT 'process definition id and version'  ,
  `process_definition_type` varchar(255) DEFAULT NULL COMMENT 'process definition type'  ,
  `status` varchar(64) NOT NULL COMMENT ' 1.running 2.completed 3.aborted',
  `parent_process_instance_id` bigint(20) unsigned DEFAULT NULL   COMMENT 'parent process instance id' ,
  `parent_execution_instance_id` bigint(20) unsigned DEFAULT NULL   COMMENT 'parent execution instance id' ,
  `start_user_id` varchar(128) DEFAULT NULL  COMMENT 'start user id' ,
  `biz_unique_id` varchar(255) DEFAULT NULL  COMMENT 'biz unique id' ,
  `reason` varchar(255) DEFAULT NULL   COMMENT 'reason' ,
  `comment` varchar(255) DEFAULT NULL   COMMENT 'comment' ,
  `title` varchar(255) DEFAULT NULL  COMMENT 'title' ,
  `tag` varchar(255) DEFAULT NULL  COMMENT 'tag' ,
  `tenant_id` varchar(64) DEFAULT NULL  COMMENT 'tenant id' ,

  PRIMARY KEY (`id`)
)   ;

CREATE TABLE `se_activity_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT  COMMENT 'PK'  ,
  `gmt_create` datetime(6) NOT NULL   COMMENT 'create time' ,
  `gmt_modified` datetime(6) NOT NULL  COMMENT 'modification time'  ,
  `process_instance_id` bigint(20) unsigned DEFAULT NULL  COMMENT 'process instance id'  ,
  `process_definition_id_and_version` varchar(255) NOT NULL  COMMENT 'process definition id and version'  ,
  `process_definition_activity_id` varchar(64) NOT NULL COMMENT 'process definition activity id'   ,
  `tenant_id` varchar(64) DEFAULT NULL  COMMENT 'tenant id' ,
  PRIMARY KEY (`id`)
)  ;

CREATE TABLE `se_task_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT  COMMENT 'PK'  ,
  `gmt_create` datetime(6) NOT NULL   COMMENT 'create time' ,
  `gmt_modified` datetime(6) NOT NULL  COMMENT 'modification time'  ,
  `process_instance_id` bigint(20) unsigned NOT NULL  COMMENT 'process instance id'  ,
  `process_definition_id_and_version` varchar(128) DEFAULT NULL  COMMENT 'process definition id and version'  ,
  `process_definition_type` varchar(255) DEFAULT NULL  COMMENT 'process definition type'  ,
  `activity_instance_id` bigint(20) unsigned NOT NULL   COMMENT 'activity instance id' ,
  `process_definition_activity_id` varchar(255) NOT NULL  COMMENT 'process definition activity id' ,
  `execution_instance_id` bigint(20) unsigned NOT NULL  COMMENT 'execution instance id'  ,
  `claim_user_id` varchar(255) DEFAULT NULL   COMMENT 'claim user id' ,
  `title` varchar(255) DEFAULT NULL COMMENT 'title'   ,
  `priority` int(11) DEFAULT 500 COMMENT 'priority' ,
  `tag` varchar(255) DEFAULT NULL  COMMENT 'tag'  ,
  `claim_time` datetime(6) DEFAULT NULL COMMENT 'claim time'   ,
  `complete_time` datetime(6) DEFAULT NULL COMMENT 'complete time'   ,
  `status` varchar(255) NOT NULL COMMENT 'status'     ,
  `comment` varchar(255) DEFAULT NULL  COMMENT 'comment'  ,
  `extension` varchar(255) DEFAULT NULL COMMENT 'extension'  ,
  `domain_code` varchar(64) DEFAULT NULL COMMENT 'domain code' ,
  `extra` json DEFAULT NULL COMMENT 'extra JSON data' ,
  `tenant_id` varchar(64) DEFAULT NULL  COMMENT 'tenant id' ,

  PRIMARY KEY (`id`)
)   ;

CREATE TABLE `se_execution_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK'   ,
  `gmt_create` datetime(6) NOT NULL   COMMENT 'create time' ,
  `gmt_modified` datetime(6) NOT NULL  COMMENT 'modification time'  ,
  `process_instance_id` bigint(20) unsigned NOT NULL  COMMENT 'process instance id'  ,
  `process_definition_id_and_version` varchar(255) NOT NULL  COMMENT 'process definition id and version'  ,
  `process_definition_activity_id` varchar(255) NOT NULL COMMENT 'process definition activity id'   ,
  `activity_instance_id` bigint(20) unsigned NOT NULL COMMENT 'activity instance id'   ,
  `block_id` bigint(20) unsigned DEFAULT NULL   COMMENT 'block_id' ,
  `active` tinyint(4) NOT NULL COMMENT '1:active 0:inactive',
  `tenant_id` varchar(64) DEFAULT NULL  COMMENT 'tenant id' ,
  PRIMARY KEY (`id`)
)   ;


CREATE TABLE `se_task_assignee_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT  COMMENT 'PK'  ,
  `gmt_create` datetime(6) NOT NULL  COMMENT 'create time'  ,
  `gmt_modified` datetime(6) NOT NULL   COMMENT 'modification time' ,
  `process_instance_id` bigint(20) unsigned NOT NULL  COMMENT 'process instance id'  ,
  `task_instance_id` bigint(20) unsigned NOT NULL  COMMENT 'task instance id'  ,
  `assignee_id` varchar(255) NOT NULL  COMMENT 'assignee id'  ,
  `assignee_type` varchar(128) NOT NULL  COMMENT 'assignee type'  ,
  `tenant_id` varchar(64) DEFAULT NULL  COMMENT 'tenant id' ,
  PRIMARY KEY (`id`)
)  ;


CREATE TABLE `se_variable_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT  COMMENT 'PK'  ,
  `gmt_create` datetime(6) NOT NULL  COMMENT 'create time'  ,
  `gmt_modified` datetime(6) NOT NULL  COMMENT 'modification time'  ,
  `process_instance_id` bigint(20) unsigned NOT NULL   COMMENT 'process instance id' ,
  `execution_instance_id` bigint(20) unsigned DEFAULT NULL   COMMENT 'execution instance id' ,
  `field_key` varchar(128) NOT NULL   COMMENT 'field key' ,
  `field_type` varchar(128) NOT NULL   COMMENT 'field type' ,
  `field_double_value` decimal(65,30) DEFAULT NULL   COMMENT 'field double value' ,
  `field_long_value` bigint(20) DEFAULT NULL  COMMENT 'field long value'  ,
  `field_string_value` varchar(4000) DEFAULT NULL  COMMENT 'field string value' ,
  `tenant_id` varchar(64) DEFAULT NULL  COMMENT 'tenant id' ,

  PRIMARY KEY (`id`)
)  ;
```


### 1.2 PostgreSQL 版（schema-postgre.sql）

```sql
CREATE TABLE se_deployment_instance (
  id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  process_definition_id varchar(255) NOT NULL,
  process_definition_version varchar(255),
  process_definition_type varchar(255),
  process_definition_code varchar(255),
  process_definition_name varchar(255),
  process_definition_desc varchar(255),
  process_definition_content text NOT NULL,
  deployment_user_id varchar(128) NOT NULL,
  deployment_status varchar(64) NOT NULL,
  logic_status varchar(64) NOT NULL,
  extension text,
  tenant_id varchar(64),
  PRIMARY KEY (id)
);

CREATE TABLE se_process_instance (
  id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  process_definition_id_and_version varchar(128) NOT NULL,
  process_definition_type varchar(255),
  status varchar(64) NOT NULL,
  parent_process_instance_id bigint,
  parent_execution_instance_id bigint,
  start_user_id varchar(128),
  biz_unique_id varchar(255),
  reason varchar(255),
  comment varchar(255),
  title varchar(255),
  tag varchar(255),
  tenant_id varchar(64),
  PRIMARY KEY (id)
);

CREATE TABLE se_activity_instance (
  id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  process_instance_id bigint,
  process_definition_id_and_version varchar(255) NOT NULL,
  process_definition_activity_id varchar(64) NOT NULL,
  tenant_id varchar(64),
  PRIMARY KEY (id)
);

CREATE TABLE se_task_instance (
  id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  process_instance_id bigint NOT NULL,
  process_definition_id_and_version varchar(128),
  process_definition_type varchar(255),
  activity_instance_id bigint NOT NULL,
  process_definition_activity_id varchar(255) NOT NULL,
  execution_instance_id bigint NOT NULL,
  claim_user_id varchar(255),
  title varchar(255),
  priority int DEFAULT 500,
  tag varchar(255),
  claim_time timestamp(6),
  complete_time timestamp(6),
  status varchar(255) NOT NULL,
  comment varchar(255),
  extension varchar(255),
  domain_code varchar(64) DEFAULT NULL,
  extra jsonb DEFAULT NULL,
  tenant_id varchar(64),
  PRIMARY KEY (id)
);

CREATE TABLE se_execution_instance (
  id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  process_instance_id bigint NOT NULL,
  process_definition_id_and_version varchar(255) NOT NULL,
  process_definition_activity_id varchar(255) NOT NULL,
  activity_instance_id bigint NOT NULL,
  block_id bigint,
  active smallint NOT NULL,
  tenant_id varchar(64),
  PRIMARY KEY (id)
);

CREATE TABLE se_task_assignee_instance (
  id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  process_instance_id bigint NOT NULL,
  task_instance_id bigint NOT NULL,
  assignee_id varchar(255) NOT NULL,
  assignee_type varchar(128) NOT NULL,
  tenant_id varchar(64),
  PRIMARY KEY (id)
);

CREATE TABLE se_variable_instance (
  id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  process_instance_id bigint NOT NULL,
  execution_instance_id bigint,
  field_key varchar(128) NOT NULL,
  field_type varchar(128) NOT NULL,
  field_double_value decimal(65,30),
  field_long_value bigint,
  field_string_value varchar(4000),
  tenant_id varchar(64),
  PRIMARY KEY (id)
);
```


---

## 2. 工作流增强表（DDL）

### 2.1 MySQL（workflow-enhancement-schema-mysql.sql）

```sql
-- 工作流管理系统增强功能数据库表结构
-- 基于SmartEngine现有表结构设计规范

-- 督办记录表
CREATE TABLE `se_supervision_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `process_instance_id` bigint(20) unsigned NOT NULL COMMENT 'process instance id',
  `task_instance_id` bigint(20) unsigned NOT NULL COMMENT 'task instance id',
  `supervisor_user_id` varchar(255) NOT NULL COMMENT 'supervisor user id',
  `supervision_reason` varchar(500) DEFAULT NULL COMMENT 'supervision reason',
  `supervision_type` varchar(64) NOT NULL COMMENT 'supervision type: urge/track/remind',
  `status` varchar(64) NOT NULL COMMENT 'status: active/closed',
  `close_time` datetime(6) DEFAULT NULL COMMENT 'close time',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  PRIMARY KEY (`id`),
  KEY `idx_process_instance_id` (`process_instance_id`),
  KEY `idx_task_instance_id` (`task_instance_id`),
  KEY `idx_supervisor_user_id` (`supervisor_user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_tenant_id` (`tenant_id`)
);

-- 知会抄送表
CREATE TABLE `se_notification_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `process_instance_id` bigint(20) unsigned NOT NULL COMMENT 'process instance id',
  `task_instance_id` bigint(20) unsigned DEFAULT NULL COMMENT 'task instance id',
  `sender_user_id` varchar(255) NOT NULL COMMENT 'sender user id',
  `receiver_user_id` varchar(255) NOT NULL COMMENT 'receiver user id',
  `notification_type` varchar(64) NOT NULL COMMENT 'notification type: cc/inform',
  `title` varchar(255) DEFAULT NULL COMMENT 'notification title',
  `content` varchar(1000) DEFAULT NULL COMMENT 'notification content',
  `read_status` varchar(64) NOT NULL DEFAULT 'unread' COMMENT 'read status: unread/read',
  `read_time` datetime(6) DEFAULT NULL COMMENT 'read time',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  PRIMARY KEY (`id`),
  KEY `idx_process_instance_id` (`process_instance_id`),
  KEY `idx_task_instance_id` (`task_instance_id`),
  KEY `idx_sender_user_id` (`sender_user_id`),
  KEY `idx_receiver_user_id` (`receiver_user_id`),
  KEY `idx_read_status` (`read_status`),
  KEY `idx_tenant_id` (`tenant_id`)
);

-- 任务移交记录表
CREATE TABLE `se_task_transfer_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `task_instance_id` bigint(20) unsigned NOT NULL COMMENT 'task instance id',
  `from_user_id` varchar(255) NOT NULL COMMENT 'from user id',
  `to_user_id` varchar(255) NOT NULL COMMENT 'to user id',
  `transfer_reason` varchar(500) DEFAULT NULL COMMENT 'transfer reason',
  `deadline` datetime(6) DEFAULT NULL COMMENT 'processing deadline',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  PRIMARY KEY (`id`),
  KEY `idx_task_instance_id` (`task_instance_id`),
  KEY `idx_from_user_id` (`from_user_id`),
  KEY `idx_to_user_id` (`to_user_id`),
  KEY `idx_tenant_id` (`tenant_id`)
);

-- 加签减签操作记录表
CREATE TABLE `se_assignee_operation_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `task_instance_id` bigint(20) unsigned NOT NULL COMMENT 'task instance id',
  `operation_type` varchar(64) NOT NULL COMMENT 'operation type: add_assignee/remove_assignee',
  `operator_user_id` varchar(255) NOT NULL COMMENT 'operator user id',
  `target_user_id` varchar(255) NOT NULL COMMENT 'target user id',
  `operation_reason` varchar(500) DEFAULT NULL COMMENT 'operation reason',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  PRIMARY KEY (`id`),
  KEY `idx_task_instance_id` (`task_instance_id`),
  KEY `idx_operation_type` (`operation_type`),
  KEY `idx_operator_user_id` (`operator_user_id`),
  KEY `idx_target_user_id` (`target_user_id`),
  KEY `idx_tenant_id` (`tenant_id`)
);

-- 流程回退记录表
CREATE TABLE `se_process_rollback_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `process_instance_id` bigint(20) unsigned NOT NULL COMMENT 'process instance id',
  `task_instance_id` bigint(20) unsigned NOT NULL COMMENT 'task instance id',
  `rollback_type` varchar(64) NOT NULL COMMENT 'rollback type: previous/specific',
  `from_activity_id` varchar(255) NOT NULL COMMENT 'from activity id',
  `to_activity_id` varchar(255) NOT NULL COMMENT 'to activity id',
  `operator_user_id` varchar(255) NOT NULL COMMENT 'operator user id',
  `rollback_reason` varchar(500) DEFAULT NULL COMMENT 'rollback reason',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  PRIMARY KEY (`id`),
  KEY `idx_process_instance_id` (`process_instance_id`),
  KEY `idx_task_instance_id` (`task_instance_id`),
  KEY `idx_rollback_type` (`rollback_type`),
  KEY `idx_operator_user_id` (`operator_user_id`),
  KEY `idx_tenant_id` (`tenant_id`)
);
```


### 2.2 PostgreSQL（workflow-enhancement-schema-postgresql.sql）

```sql
CREATE TABLE se_supervision_instance (
  id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  process_instance_id bigint NOT NULL,
  task_instance_id bigint NOT NULL,
  supervisor_user_id varchar(255) NOT NULL,
  supervision_reason varchar(500),
  supervision_type varchar(64) NOT NULL,
  status varchar(64) NOT NULL,
  close_time timestamp(6),
  tenant_id varchar(64)
);

CREATE INDEX idx_supervision_process_instance_id
  ON se_supervision_instance (process_instance_id);

CREATE INDEX idx_supervision_task_instance_id
  ON se_supervision_instance (task_instance_id);

CREATE INDEX idx_supervision_supervisor_user_id
  ON se_supervision_instance (supervisor_user_id);

CREATE INDEX idx_supervision_status
  ON se_supervision_instance (status);

CREATE INDEX idx_supervision_tenant_id
  ON se_supervision_instance (tenant_id);

COMMENT ON TABLE se_supervision_instance IS '督办记录表';
COMMENT ON COLUMN se_supervision_instance.supervision_type IS 'urge/track/remind';
COMMENT ON COLUMN se_supervision_instance.status IS 'active/closed';



CREATE TABLE se_notification_instance (
  id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  process_instance_id bigint NOT NULL,
  task_instance_id bigint,
  sender_user_id varchar(255) NOT NULL,
  receiver_user_id varchar(255) NOT NULL,
  notification_type varchar(64) NOT NULL,
  title varchar(255),
  content varchar(1000),
  read_status varchar(64) NOT NULL DEFAULT 'unread',
  read_time timestamp(6),
  tenant_id varchar(64)
);

CREATE INDEX idx_notification_process_instance_id
  ON se_notification_instance (process_instance_id);

CREATE INDEX idx_notification_task_instance_id
  ON se_notification_instance (task_instance_id);

CREATE INDEX idx_notification_sender_user_id
  ON se_notification_instance (sender_user_id);

CREATE INDEX idx_notification_receiver_user_id
  ON se_notification_instance (receiver_user_id);

CREATE INDEX idx_notification_read_status
  ON se_notification_instance (read_status);

CREATE INDEX idx_notification_tenant_id
  ON se_notification_instance (tenant_id);

COMMENT ON TABLE se_notification_instance IS '知会/抄送记录表';
COMMENT ON COLUMN se_notification_instance.notification_type IS 'cc/inform';
COMMENT ON COLUMN se_notification_instance.read_status IS 'unread/read';

CREATE TABLE se_assignee_operation_record (
  id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  task_instance_id bigint NOT NULL,
  operation_type varchar(64) NOT NULL,
  operator_user_id varchar(255) NOT NULL,
  target_user_id varchar(255) NOT NULL,
  operation_reason varchar(500),
  tenant_id varchar(64)
);

CREATE INDEX idx_assignee_op_task_instance_id
  ON se_assignee_operation_record (task_instance_id);

CREATE INDEX idx_assignee_op_operation_type
  ON se_assignee_operation_record (operation_type);

CREATE INDEX idx_assignee_op_operator_user_id
  ON se_assignee_operation_record (operator_user_id);

CREATE INDEX idx_assignee_op_target_user_id
  ON se_assignee_operation_record (target_user_id);

CREATE INDEX idx_assignee_op_tenant_id
  ON se_assignee_operation_record (tenant_id);

COMMENT ON TABLE se_assignee_operation_record IS '加签/减签操作记录表';
COMMENT ON COLUMN se_assignee_operation_record.operation_type
  IS 'add_assignee/remove_assignee';




CREATE TABLE se_task_transfer_record (
  id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  task_instance_id bigint NOT NULL,
  from_user_id varchar(255) NOT NULL,
  to_user_id varchar(255) NOT NULL,
  transfer_reason varchar(500),
  deadline timestamp(6),
  tenant_id varchar(64)
);

CREATE INDEX idx_task_transfer_task_instance_id
  ON se_task_transfer_record (task_instance_id);

CREATE INDEX idx_task_transfer_from_user_id
  ON se_task_transfer_record (from_user_id);

CREATE INDEX idx_task_transfer_to_user_id
  ON se_task_transfer_record (to_user_id);

CREATE INDEX idx_task_transfer_tenant_id
  ON se_task_transfer_record (tenant_id);

COMMENT ON TABLE se_task_transfer_record IS '任务移交记录表';


CREATE TABLE se_process_rollback_record (
  id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  process_instance_id bigint NOT NULL,
  task_instance_id bigint NOT NULL,
  rollback_type varchar(64) NOT NULL,
  from_activity_id varchar(255) NOT NULL,
  to_activity_id varchar(255) NOT NULL,
  operator_user_id varchar(255) NOT NULL,
  rollback_reason varchar(500),
  tenant_id varchar(64)
);

CREATE INDEX idx_rollback_process_instance_id
  ON se_process_rollback_record (process_instance_id);

CREATE INDEX idx_rollback_task_instance_id
  ON se_process_rollback_record (task_instance_id);

CREATE INDEX idx_rollback_type
  ON se_process_rollback_record (rollback_type);

CREATE INDEX idx_rollback_operator_user_id
  ON se_process_rollback_record (operator_user_id);

CREATE INDEX idx_rollback_tenant_id
  ON se_process_rollback_record (tenant_id);

COMMENT ON TABLE se_process_rollback_record IS '流程回退记录表';
COMMENT ON COLUMN se_process_rollback_record.rollback_type
  IS 'previous/specific';
```


---

## 3. 索引（index.sql）

```sql


alter table `se_execution_instance`
    add key `idx_tenant_id_process_instance_id_and_status_tenant_id` (tenant_id,process_instance_id,active),
    add key `idx_tenant_id_process_instance_id_and_activity_instance_id` (tenant_id,process_instance_id,activity_instance_id);

alter table `se_task_assignee_instance`
    add key `idx_tenant_id_task_instance_id` (tenant_id,task_instance_id),
    add key `idx_tenant_id_assignee_id_and_type` (tenant_id,assignee_id,assignee_type);

alter table `se_activity_instance`
    add key `idx_tenant_id_process_instance_id` (tenant_id,process_instance_id);

alter table `se_deployment_instance`
    add key `idx_tenant_id_user_id_and_logic_status` (tenant_id,logic_status,deployment_user_id);

alter table `se_process_instance`
    add key `idx_tenant_id_start_user_id` (tenant_id,start_user_id),
    add key `idx_tenant_id_status` (tenant_id,status);

alter table `se_task_instance`
    add key `idx_tenant_id_status` (tenant_id,status),
    add key `idx_tenant_id_process_instance_id_and_status` (tenant_id,process_instance_id,status),
    add key `idx_tenant_id_process_definition_type` (tenant_id,process_definition_type),
    add key `idx_tenant_id_process_instance_id` (tenant_id,process_instance_id),
    add key `idx_tenant_id_claim_user_id` (tenant_id,claim_user_id),
    add key `idx_tenant_id_tag` (tenant_id,tag),
    add key `idx_tenant_id_activity_instance_id` (tenant_id,activity_instance_id),
    add key `idx_tenant_id_process_definition_activity_id` (tenant_id,process_definition_activity_id);

alter table `se_variable_instance`
    add key `idx_tenant_id_process_instance_id_and_execution_instance_id` (tenant_id,process_instance_id,execution_instance_id);
```


---

## 4. 清理策略（建议）

SmartEngine 默认表结构没有单独的“历史表”体系，常见做法是：

- 使用 `status` 字段区分 active/closed（或 deleted）
- 定期清理：
  - 已完成且超过保留期的 process/execution/activity/task/variable
  - 已关闭的通知/督办/转派/回退记录

建议你在业务系统侧明确：

- 保留期（如 90/180/365 天）
- 清理批大小（避免大事务）
- 清理顺序（先 task/assignee/variable，再 activity/execution，再 process/deployment）

如果你需要更完善的审计/回放权限，建议自建 history 表或引入事件日志（event sourcing），并把引擎推进过程输出为可重放事件。

