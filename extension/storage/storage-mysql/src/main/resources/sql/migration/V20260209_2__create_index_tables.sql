-- Create user task index table and user notification index table (MySQL version)
-- These tables support fast user-centric queries in sharding mode

-- ===========================================
-- User task index table (not partitioned)
-- Stores only active/pending tasks for fast user-centric queries
-- ===========================================
CREATE TABLE IF NOT EXISTS `se_user_task_index` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `tenant_id` varchar(64) DEFAULT NULL,
  `assignee_id` varchar(255) NOT NULL,
  `assignee_type` varchar(128) NOT NULL DEFAULT 'user',
  `task_instance_id` bigint(20) unsigned NOT NULL,
  `process_instance_id` bigint(20) unsigned NOT NULL,
  `process_definition_type` varchar(255) DEFAULT NULL,
  `domain_code` varchar(64) DEFAULT NULL,
  `extra` json DEFAULT NULL,
  `task_status` varchar(64) NOT NULL,
  `task_gmt_modified` datetime(6) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `priority` int(11) DEFAULT 500,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_task_idx` (`tenant_id`, `assignee_id`, `task_instance_id`),
  KEY `idx_user_task_pending` (`tenant_id`, `assignee_id`, `assignee_type`, `task_status`),
  KEY `idx_user_task_type` (`tenant_id`, `assignee_id`, `process_definition_type`, `task_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User task index for sharding mode';

-- ===========================================
-- User notification index table (not partitioned)
-- Stores only unread notifications for fast user-centric queries
-- ===========================================
CREATE TABLE IF NOT EXISTS `se_user_notification_index` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `tenant_id` varchar(64) DEFAULT NULL,
  `receiver_user_id` varchar(255) NOT NULL,
  `notification_id` bigint(20) unsigned NOT NULL,
  `process_instance_id` bigint(20) unsigned NOT NULL,
  `notification_type` varchar(64) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `read_status` varchar(64) NOT NULL DEFAULT 'unread',
  `gmt_create` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_notif_idx` (`tenant_id`, `receiver_user_id`, `notification_id`),
  KEY `idx_user_notif_receiver` (`tenant_id`, `receiver_user_id`, `read_status`),
  KEY `idx_user_notif_type` (`tenant_id`, `receiver_user_id`, `notification_type`, `read_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User notification index for sharding mode';
