-- Archive tables for MySQL
-- Mirror of runtime tables with additional archived_time column
-- PK is non-auto-increment (preserves original id values)

-- ===========================================
-- 1. se_process_instance_archive
-- ===========================================
CREATE TABLE `se_process_instance_archive` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'PK (from original)',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `process_definition_id_and_version` varchar(128) NOT NULL COMMENT 'process definition id and version',
  `process_definition_type` varchar(255) DEFAULT NULL COMMENT 'process definition type',
  `status` varchar(64) NOT NULL COMMENT '1.running 2.completed 3.aborted',
  `complete_time` datetime(6) DEFAULT NULL COMMENT 'process completion time',
  `parent_process_instance_id` bigint(20) unsigned DEFAULT NULL COMMENT 'parent process instance id',
  `parent_execution_instance_id` bigint(20) unsigned DEFAULT NULL COMMENT 'parent execution instance id',
  `start_user_id` varchar(128) DEFAULT NULL COMMENT 'start user id',
  `biz_unique_id` varchar(255) DEFAULT NULL COMMENT 'biz unique id',
  `reason` varchar(255) DEFAULT NULL COMMENT 'reason',
  `comment` varchar(255) DEFAULT NULL COMMENT 'comment',
  `title` varchar(255) DEFAULT NULL COMMENT 'title',
  `tag` varchar(255) DEFAULT NULL COMMENT 'tag',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'archive time',
  PRIMARY KEY (`id`),
  KEY `idx_archive_pi_tenant_status` (`tenant_id`, `status`),
  KEY `idx_archive_pi_archived_time` (`archived_time`)
);

-- ===========================================
-- 2. se_task_instance_archive
-- ===========================================
CREATE TABLE `se_task_instance_archive` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'PK (from original)',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `process_instance_id` bigint(20) unsigned NOT NULL COMMENT 'process instance id',
  `process_definition_id_and_version` varchar(128) DEFAULT NULL COMMENT 'process definition id and version',
  `process_definition_type` varchar(255) DEFAULT NULL COMMENT 'process definition type',
  `activity_instance_id` bigint(20) unsigned NOT NULL COMMENT 'activity instance id',
  `process_definition_activity_id` varchar(255) NOT NULL COMMENT 'process definition activity id',
  `execution_instance_id` bigint(20) unsigned NOT NULL COMMENT 'execution instance id',
  `claim_user_id` varchar(255) DEFAULT NULL COMMENT 'claim user id',
  `title` varchar(255) DEFAULT NULL COMMENT 'title',
  `priority` int(11) DEFAULT 500 COMMENT 'priority',
  `tag` varchar(255) DEFAULT NULL COMMENT 'tag',
  `claim_time` datetime(6) DEFAULT NULL COMMENT 'claim time',
  `complete_time` datetime(6) DEFAULT NULL COMMENT 'complete time',
  `status` varchar(255) NOT NULL COMMENT 'status',
  `comment` varchar(255) DEFAULT NULL COMMENT 'comment',
  `extension` varchar(255) DEFAULT NULL COMMENT 'extension',
  `domain_code` varchar(64) DEFAULT NULL COMMENT 'domain code',
  `extra` json DEFAULT NULL COMMENT 'extra JSON data',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'archive time',
  PRIMARY KEY (`id`),
  KEY `idx_archive_ti_process_instance_id` (`process_instance_id`),
  KEY `idx_archive_ti_archived_time` (`archived_time`)
);

-- ===========================================
-- 3. se_execution_instance_archive
-- ===========================================
CREATE TABLE `se_execution_instance_archive` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'PK (from original)',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `process_instance_id` bigint(20) unsigned NOT NULL COMMENT 'process instance id',
  `process_definition_id_and_version` varchar(255) NOT NULL COMMENT 'process definition id and version',
  `process_definition_activity_id` varchar(255) NOT NULL COMMENT 'process definition activity id',
  `activity_instance_id` bigint(20) unsigned NOT NULL COMMENT 'activity instance id',
  `block_id` bigint(20) unsigned DEFAULT NULL COMMENT 'block_id',
  `active` tinyint(4) NOT NULL COMMENT '1:active 0:inactive',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'archive time',
  PRIMARY KEY (`id`),
  KEY `idx_archive_ei_process_instance_id` (`process_instance_id`),
  KEY `idx_archive_ei_archived_time` (`archived_time`)
);

-- ===========================================
-- 4. se_activity_instance_archive
-- ===========================================
CREATE TABLE `se_activity_instance_archive` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'PK (from original)',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `process_instance_id` bigint(20) unsigned DEFAULT NULL COMMENT 'process instance id',
  `process_definition_id_and_version` varchar(255) NOT NULL COMMENT 'process definition id and version',
  `process_definition_activity_id` varchar(64) NOT NULL COMMENT 'process definition activity id',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'archive time',
  PRIMARY KEY (`id`),
  KEY `idx_archive_ai_process_instance_id` (`process_instance_id`),
  KEY `idx_archive_ai_archived_time` (`archived_time`)
);

-- ===========================================
-- 5. se_variable_instance_archive
-- ===========================================
CREATE TABLE `se_variable_instance_archive` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'PK (from original)',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `process_instance_id` bigint(20) unsigned NOT NULL COMMENT 'process instance id',
  `execution_instance_id` bigint(20) unsigned DEFAULT NULL COMMENT 'execution instance id',
  `field_key` varchar(128) NOT NULL COMMENT 'field key',
  `field_type` varchar(128) NOT NULL COMMENT 'field type',
  `field_double_value` decimal(65,30) DEFAULT NULL COMMENT 'field double value',
  `field_long_value` bigint(20) DEFAULT NULL COMMENT 'field long value',
  `field_string_value` varchar(4000) DEFAULT NULL COMMENT 'field string value',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'archive time',
  PRIMARY KEY (`id`),
  KEY `idx_archive_vi_process_instance_id` (`process_instance_id`),
  KEY `idx_archive_vi_archived_time` (`archived_time`)
);

-- ===========================================
-- 6. se_task_assignee_instance_archive
-- ===========================================
CREATE TABLE `se_task_assignee_instance_archive` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'PK (from original)',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `process_instance_id` bigint(20) unsigned NOT NULL COMMENT 'process instance id',
  `task_instance_id` bigint(20) unsigned NOT NULL COMMENT 'task instance id',
  `assignee_id` varchar(255) NOT NULL COMMENT 'assignee id',
  `assignee_type` varchar(128) NOT NULL COMMENT 'assignee type',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'archive time',
  PRIMARY KEY (`id`),
  KEY `idx_archive_tai_process_instance_id` (`process_instance_id`),
  KEY `idx_archive_tai_archived_time` (`archived_time`)
);

-- ===========================================
-- 7. se_supervision_instance_archive
-- ===========================================
CREATE TABLE `se_supervision_instance_archive` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'PK (from original)',
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
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'archive time',
  PRIMARY KEY (`id`),
  KEY `idx_archive_si_process_instance_id` (`process_instance_id`),
  KEY `idx_archive_si_archived_time` (`archived_time`)
);

-- ===========================================
-- 8. se_notification_instance_archive
-- ===========================================
CREATE TABLE `se_notification_instance_archive` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'PK (from original)',
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
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'archive time',
  PRIMARY KEY (`id`),
  KEY `idx_archive_ni_process_instance_id` (`process_instance_id`),
  KEY `idx_archive_ni_archived_time` (`archived_time`)
);

-- ===========================================
-- 9. se_task_transfer_record_archive
-- ===========================================
CREATE TABLE `se_task_transfer_record_archive` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'PK (from original)',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `task_instance_id` bigint(20) unsigned NOT NULL COMMENT 'task instance id',
  `from_user_id` varchar(255) NOT NULL COMMENT 'from user id',
  `to_user_id` varchar(255) NOT NULL COMMENT 'to user id',
  `transfer_reason` varchar(500) DEFAULT NULL COMMENT 'transfer reason',
  `deadline` datetime(6) DEFAULT NULL COMMENT 'processing deadline',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'archive time',
  PRIMARY KEY (`id`),
  KEY `idx_archive_ttr_task_instance_id` (`task_instance_id`),
  KEY `idx_archive_ttr_archived_time` (`archived_time`)
);

-- ===========================================
-- 10. se_assignee_operation_record_archive
-- ===========================================
CREATE TABLE `se_assignee_operation_record_archive` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'PK (from original)',
  `gmt_create` datetime(6) NOT NULL COMMENT 'create time',
  `gmt_modified` datetime(6) NOT NULL COMMENT 'modification time',
  `task_instance_id` bigint(20) unsigned NOT NULL COMMENT 'task instance id',
  `operation_type` varchar(64) NOT NULL COMMENT 'operation type: add_assignee/remove_assignee',
  `operator_user_id` varchar(255) NOT NULL COMMENT 'operator user id',
  `target_user_id` varchar(255) NOT NULL COMMENT 'target user id',
  `operation_reason` varchar(500) DEFAULT NULL COMMENT 'operation reason',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'tenant id',
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'archive time',
  PRIMARY KEY (`id`),
  KEY `idx_archive_aor_task_instance_id` (`task_instance_id`),
  KEY `idx_archive_aor_archived_time` (`archived_time`)
);

-- ===========================================
-- 11. se_process_rollback_record_archive
-- ===========================================
CREATE TABLE `se_process_rollback_record_archive` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'PK (from original)',
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
  `archived_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'archive time',
  PRIMARY KEY (`id`),
  KEY `idx_archive_prr_process_instance_id` (`process_instance_id`),
  KEY `idx_archive_prr_archived_time` (`archived_time`)
);
