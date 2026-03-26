-- Add process_instance_id column to se_task_transfer_record
ALTER TABLE se_task_transfer_record ADD COLUMN process_instance_id bigint(20) unsigned DEFAULT NULL COMMENT 'process instance id' AFTER task_instance_id;
CREATE INDEX idx_transfer_process_instance_id ON se_task_transfer_record (process_instance_id);

-- Add process_instance_id column to se_assignee_operation_record
ALTER TABLE se_assignee_operation_record ADD COLUMN process_instance_id bigint(20) unsigned DEFAULT NULL COMMENT 'process instance id' AFTER task_instance_id;
CREATE INDEX idx_assignee_op_process_instance_id ON se_assignee_operation_record (process_instance_id);
