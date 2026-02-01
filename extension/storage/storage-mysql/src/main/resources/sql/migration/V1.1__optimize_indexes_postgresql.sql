-- ===========================================
-- Index optimization migration script for PostgreSQL
-- Version: 1.1
-- Description: Optimize indexes for better query performance
-- ===========================================

-- Step 1: Add new composite indexes (before dropping old ones)

-- se_supervision_instance: Composite index for findActiveByTaskId
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_supervision_task_status_tenant
  ON se_supervision_instance (task_instance_id, status, tenant_id);

-- se_supervision_instance: Composite index for findBySupervisor
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_supervision_supervisor_tenant
  ON se_supervision_instance (supervisor_user_id, tenant_id);

-- se_notification_instance: Composite index for findByReceiver (most important)
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_notification_receiver_read_tenant
  ON se_notification_instance (receiver_user_id, read_status, tenant_id);

-- se_notification_instance: Composite index for findBySender
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_notification_sender_tenant
  ON se_notification_instance (sender_user_id, tenant_id);

-- se_task_transfer_record: Composite index
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_task_transfer_task_tenant
  ON se_task_transfer_record (task_instance_id, tenant_id);

-- se_assignee_operation_record: Composite index
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_assignee_op_task_tenant
  ON se_assignee_operation_record (task_instance_id, tenant_id);

-- se_process_rollback_record: Composite index
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_rollback_process_tenant
  ON se_process_rollback_record (process_instance_id, tenant_id);

-- Step 2: Drop inefficient indexes

-- Drop low-selectivity single-column indexes (status only has 2 values)
DROP INDEX IF EXISTS idx_supervision_status;
DROP INDEX IF EXISTS idx_notification_read_status;

-- Drop redundant tenant_id indexes (replaced by composite indexes)
DROP INDEX IF EXISTS idx_supervision_tenant_id;
DROP INDEX IF EXISTS idx_notification_tenant_id;
DROP INDEX IF EXISTS idx_task_transfer_tenant_id;
DROP INDEX IF EXISTS idx_assignee_op_tenant_id;
DROP INDEX IF EXISTS idx_rollback_tenant_id;

-- Drop single-column indexes replaced by composite indexes
DROP INDEX IF EXISTS idx_supervision_task_instance_id;
DROP INDEX IF EXISTS idx_supervision_supervisor_user_id;
DROP INDEX IF EXISTS idx_notification_receiver_user_id;
DROP INDEX IF EXISTS idx_notification_sender_user_id;
DROP INDEX IF EXISTS idx_task_transfer_task_instance_id;
DROP INDEX IF EXISTS idx_assignee_op_task_instance_id;
DROP INDEX IF EXISTS idx_rollback_process_instance_id;

-- Step 3: Update statistics
ANALYZE se_supervision_instance;
ANALYZE se_notification_instance;
ANALYZE se_task_transfer_record;
ANALYZE se_assignee_operation_record;
ANALYZE se_process_rollback_record;

-- ===========================================
-- Verification query
-- ===========================================
-- SELECT schemaname, tablename, indexname, indexdef
-- FROM pg_indexes
-- WHERE tablename IN (
--   'se_supervision_instance',
--   'se_notification_instance',
--   'se_task_transfer_record',
--   'se_assignee_operation_record',
--   'se_process_rollback_record'
-- )
-- ORDER BY tablename, indexname;
