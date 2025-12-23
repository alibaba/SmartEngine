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