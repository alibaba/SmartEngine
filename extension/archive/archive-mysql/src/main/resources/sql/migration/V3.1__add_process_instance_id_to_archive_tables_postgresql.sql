-- Add process_instance_id column to archive tables (PostgreSQL version)

ALTER TABLE se_task_transfer_record_archive
  ADD COLUMN IF NOT EXISTS process_instance_id bigint DEFAULT NULL;

ALTER TABLE se_assignee_operation_record_archive
  ADD COLUMN IF NOT EXISTS process_instance_id bigint DEFAULT NULL;
