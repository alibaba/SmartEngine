-- Archive tables for H2 (test only)

CREATE TABLE IF NOT EXISTS `se_process_instance_archive` (
  `id` bigint NOT NULL COMMENT 'PK (from original)',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `process_definition_id_and_version` varchar(128) NOT NULL COMMENT 'process definition id and version',
  `process_definition_type` varchar(255) DEFAULT NULL COMMENT 'process definition type',
  `status` varchar(64) NOT NULL COMMENT 'status',
  `complete_time` datetime(6) DEFAULT NULL COMMENT 'process completion time',
  `parent_process_instance_id` bigint DEFAULT NULL COMMENT 'parent process instance id',
  `parent_execution_instance_id` bigint DEFAULT NULL COMMENT 'parent execution instance id',
  `start_user_id` varchar(128) DEFAULT NULL COMMENT 'start user id',
  `biz_unique_id` varchar(255) DEFAULT NULL COMMENT 'biz unique id',
  `reason` varchar(255) DEFAULT NULL COMMENT 'reason',
  `comment` varchar(255) DEFAULT NULL COMMENT 'comment',
  `title` varchar(255) DEFAULT NULL COMMENT 'title',
  `tag` varchar(255) DEFAULT NULL COMMENT 'tag',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'archive time',
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `se_task_instance_archive` (
  `id` bigint NOT NULL COMMENT 'PK (from original)',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `process_instance_id` bigint NOT NULL COMMENT 'process instance id',
  `process_definition_id_and_version` varchar(128) DEFAULT NULL COMMENT 'process definition id and version',
  `process_definition_type` varchar(255) DEFAULT NULL COMMENT 'process definition type',
  `activity_instance_id` bigint NOT NULL COMMENT 'activity instance id',
  `process_definition_activity_id` varchar(255) NOT NULL COMMENT 'process definition activity id',
  `execution_instance_id` bigint NOT NULL COMMENT 'execution instance id',
  `claim_user_id` varchar(255) DEFAULT NULL COMMENT 'claim user id',
  `title` varchar(255) DEFAULT NULL COMMENT 'title',
  `priority` int DEFAULT 500 COMMENT 'priority',
  `tag` varchar(255) DEFAULT NULL COMMENT 'tag',
  `claim_time` datetime(6) DEFAULT NULL COMMENT 'claim time',
  `complete_time` datetime(6) DEFAULT NULL COMMENT 'complete time',
  `status` varchar(255) NOT NULL COMMENT 'status',
  `comment` varchar(255) DEFAULT NULL COMMENT 'comment',
  `extension` varchar(255) DEFAULT NULL COMMENT 'extension',
  `domain_code` varchar(64) DEFAULT NULL COMMENT 'domain code',
  `extra` clob DEFAULT NULL COMMENT 'extra JSON data',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'archive time',
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `se_execution_instance_archive` (
  `id` bigint NOT NULL COMMENT 'PK (from original)',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `process_instance_id` bigint NOT NULL COMMENT 'process instance id',
  `process_definition_id_and_version` varchar(255) NOT NULL COMMENT 'process definition id and version',
  `process_definition_activity_id` varchar(255) NOT NULL COMMENT 'process definition activity id',
  `activity_instance_id` bigint NOT NULL COMMENT 'activity instance id',
  `block_id` bigint DEFAULT NULL COMMENT 'block_id',
  `active` bigint NOT NULL COMMENT '1:active 0:inactive',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'archive time',
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `se_activity_instance_archive` (
  `id` bigint NOT NULL COMMENT 'PK (from original)',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `process_instance_id` bigint DEFAULT NULL COMMENT 'process instance id',
  `process_definition_id_and_version` varchar(255) NOT NULL COMMENT 'process definition id and version',
  `process_definition_activity_id` varchar(64) NOT NULL COMMENT 'process definition activity id',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'archive time',
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `se_variable_instance_archive` (
  `id` bigint NOT NULL COMMENT 'PK (from original)',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `process_instance_id` bigint NOT NULL COMMENT 'process instance id',
  `execution_instance_id` bigint DEFAULT NULL COMMENT 'execution instance id',
  `field_key` varchar(128) NOT NULL COMMENT 'field key',
  `field_type` varchar(128) NOT NULL COMMENT 'field type',
  `field_double_value` decimal(65,30) DEFAULT NULL COMMENT 'field double value',
  `field_long_value` bigint DEFAULT NULL COMMENT 'field long value',
  `field_string_value` varchar(4000) DEFAULT NULL COMMENT 'field string value',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'archive time',
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `se_task_assignee_instance_archive` (
  `id` bigint NOT NULL COMMENT 'PK (from original)',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `process_instance_id` bigint NOT NULL COMMENT 'process instance id',
  `task_instance_id` bigint NOT NULL COMMENT 'task instance id',
  `assignee_id` varchar(255) NOT NULL COMMENT 'assignee id',
  `assignee_type` varchar(128) NOT NULL COMMENT 'assignee type',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'archive time',
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `se_supervision_instance_archive` (
  `id` bigint NOT NULL COMMENT 'PK (from original)',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `process_instance_id` bigint NOT NULL COMMENT 'process instance id',
  `task_instance_id` bigint NOT NULL COMMENT 'task instance id',
  `supervisor_user_id` varchar(255) NOT NULL COMMENT 'supervisor user id',
  `supervision_reason` varchar(500) DEFAULT NULL COMMENT 'supervision reason',
  `supervision_type` varchar(64) NOT NULL COMMENT 'supervision type',
  `status` varchar(64) NOT NULL COMMENT 'status',
  `close_time` datetime(6) DEFAULT NULL COMMENT 'close time',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'archive time',
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `se_notification_instance_archive` (
  `id` bigint NOT NULL COMMENT 'PK (from original)',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `process_instance_id` bigint NOT NULL COMMENT 'process instance id',
  `task_instance_id` bigint DEFAULT NULL COMMENT 'task instance id',
  `sender_user_id` varchar(255) NOT NULL COMMENT 'sender user id',
  `receiver_user_id` varchar(255) NOT NULL COMMENT 'receiver user id',
  `notification_type` varchar(64) NOT NULL COMMENT 'notification type',
  `title` varchar(255) DEFAULT NULL COMMENT 'notification title',
  `content` varchar(1000) DEFAULT NULL COMMENT 'notification content',
  `read_status` varchar(64) NOT NULL DEFAULT 'unread' COMMENT 'read status',
  `read_time` datetime(6) DEFAULT NULL COMMENT 'read time',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'archive time',
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `se_task_transfer_record_archive` (
  `id` bigint NOT NULL COMMENT 'PK (from original)',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `task_instance_id` bigint NOT NULL COMMENT 'task instance id',
  `process_instance_id` bigint DEFAULT NULL COMMENT 'process instance id',
  `from_user_id` varchar(255) NOT NULL COMMENT 'from user id',
  `to_user_id` varchar(255) NOT NULL COMMENT 'to user id',
  `transfer_reason` varchar(500) DEFAULT NULL COMMENT 'transfer reason',
  `deadline` datetime(6) DEFAULT NULL COMMENT 'processing deadline',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'archive time',
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `se_assignee_operation_record_archive` (
  `id` bigint NOT NULL COMMENT 'PK (from original)',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `task_instance_id` bigint NOT NULL COMMENT 'task instance id',
  `process_instance_id` bigint DEFAULT NULL COMMENT 'process instance id',
  `operation_type` varchar(64) NOT NULL COMMENT 'operation type',
  `operator_user_id` varchar(255) NOT NULL COMMENT 'operator user id',
  `target_user_id` varchar(255) NOT NULL COMMENT 'target user id',
  `operation_reason` varchar(500) DEFAULT NULL COMMENT 'operation reason',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'archive time',
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `se_process_rollback_record_archive` (
  `id` bigint NOT NULL COMMENT 'PK (from original)',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `process_instance_id` bigint NOT NULL COMMENT 'process instance id',
  `task_instance_id` bigint NOT NULL COMMENT 'task instance id',
  `rollback_type` varchar(64) NOT NULL COMMENT 'rollback type',
  `from_activity_id` varchar(255) NOT NULL COMMENT 'from activity id',
  `to_activity_id` varchar(255) NOT NULL COMMENT 'to activity id',
  `operator_user_id` varchar(255) NOT NULL COMMENT 'operator user id',
  `rollback_reason` varchar(500) DEFAULT NULL COMMENT 'rollback reason',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'archive time',
  PRIMARY KEY (`id`)
);
