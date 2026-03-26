-- ===========================================
-- Index optimization migration script
-- Version: 1.1
-- Description: Optimize indexes for better query performance
-- ===========================================

-- ===========================================
-- MySQL Version
-- ===========================================

-- Step 1: Add new composite indexes (before dropping old ones)

-- se_supervision_instance: Composite index for findActiveByTaskId
ALTER TABLE `se_supervision_instance`
ADD KEY `idx_task_status_tenant` (`task_instance_id`, `status`, `tenant_id`);

-- se_supervision_instance: Composite index for findBySupervisor
ALTER TABLE `se_supervision_instance`
ADD KEY `idx_supervisor_tenant` (`supervisor_user_id`, `tenant_id`);

-- se_notification_instance: Composite index for findByReceiver (most important)
ALTER TABLE `se_notification_instance`
ADD KEY `idx_receiver_read_tenant` (`receiver_user_id`, `read_status`, `tenant_id`);

-- se_notification_instance: Composite index for findBySender
ALTER TABLE `se_notification_instance`
ADD KEY `idx_sender_tenant` (`sender_user_id`, `tenant_id`);

-- se_task_transfer_record: Composite index
ALTER TABLE `se_task_transfer_record`
ADD KEY `idx_task_tenant` (`task_instance_id`, `tenant_id`);

-- se_assignee_operation_record: Composite index
ALTER TABLE `se_assignee_operation_record`
ADD KEY `idx_task_tenant` (`task_instance_id`, `tenant_id`);

-- se_process_rollback_record: Composite index
ALTER TABLE `se_process_rollback_record`
ADD KEY `idx_process_tenant` (`process_instance_id`, `tenant_id`);

-- Step 2: Drop inefficient indexes

-- Drop low-selectivity single-column indexes (status only has 2 values)
ALTER TABLE `se_supervision_instance` DROP KEY `idx_status`;
ALTER TABLE `se_notification_instance` DROP KEY `idx_read_status`;

-- Drop redundant tenant_id indexes (replaced by composite indexes)
ALTER TABLE `se_supervision_instance` DROP KEY `idx_tenant_id`;
ALTER TABLE `se_notification_instance` DROP KEY `idx_tenant_id`;
ALTER TABLE `se_task_transfer_record` DROP KEY `idx_tenant_id`;
ALTER TABLE `se_assignee_operation_record` DROP KEY `idx_tenant_id`;
ALTER TABLE `se_process_rollback_record` DROP KEY `idx_tenant_id`;

-- Drop single-column indexes replaced by composite indexes
ALTER TABLE `se_supervision_instance` DROP KEY `idx_task_instance_id`;
ALTER TABLE `se_supervision_instance` DROP KEY `idx_supervisor_user_id`;
ALTER TABLE `se_notification_instance` DROP KEY `idx_receiver_user_id`;
ALTER TABLE `se_notification_instance` DROP KEY `idx_sender_user_id`;
ALTER TABLE `se_task_transfer_record` DROP KEY `idx_task_instance_id`;
ALTER TABLE `se_assignee_operation_record` DROP KEY `idx_task_instance_id`;
ALTER TABLE `se_process_rollback_record` DROP KEY `idx_process_instance_id`;

-- ===========================================
-- Verification queries
-- ===========================================
-- SHOW INDEX FROM `se_supervision_instance`;
-- SHOW INDEX FROM `se_notification_instance`;
-- SHOW INDEX FROM `se_task_transfer_record`;
-- SHOW INDEX FROM `se_assignee_operation_record`;
-- SHOW INDEX FROM `se_process_rollback_record`;
