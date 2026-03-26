-- Add process_instance_id column to archive tables (MySQL version)

ALTER TABLE se_task_transfer_record_archive
  ADD COLUMN `process_instance_id` bigint(20) unsigned DEFAULT NULL COMMENT 'process instance id'
  AFTER `task_instance_id`;

ALTER TABLE se_assignee_operation_record_archive
  ADD COLUMN `process_instance_id` bigint(20) unsigned DEFAULT NULL COMMENT 'process instance id'
  AFTER `task_instance_id`;
