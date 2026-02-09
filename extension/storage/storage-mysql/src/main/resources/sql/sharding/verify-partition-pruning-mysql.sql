-- ===========================================
-- Partition Pruning Verification Scripts (MySQL)
-- Run these after deploying the sharding schema
-- Each query should show "partitions: pN" (single partition) in the plan
-- ===========================================

-- ============ 1. Process instance by ID ============
-- Expected: partitions shows only 1 partition
EXPLAIN
SELECT * FROM se_process_instance WHERE id = 123456;

-- ============ 2. Task instances by process_instance_id ============
-- Expected: partitions shows only 1 partition
EXPLAIN
SELECT * FROM se_task_instance WHERE process_instance_id = 123456;

-- ============ 3. Execution instances by process_instance_id ============
-- Expected: partitions shows only 1 partition
EXPLAIN
SELECT * FROM se_execution_instance
WHERE process_instance_id = 123456 AND active = 1;

-- ============ 4. Activity instances by process_instance_id ============
EXPLAIN
SELECT * FROM se_activity_instance WHERE process_instance_id = 123456;

-- ============ 5. Task assignees by process_instance_id ============
EXPLAIN
SELECT * FROM se_task_assignee_instance WHERE process_instance_id = 123456;

-- ============ 6. Variables by process_instance_id ============
EXPLAIN
SELECT * FROM se_variable_instance WHERE process_instance_id = 123456;

-- ============ 7. Supervision by process_instance_id ============
EXPLAIN
SELECT * FROM se_supervision_instance WHERE process_instance_id = 123456;

-- ============ 8. Notifications by process_instance_id ============
EXPLAIN
SELECT * FROM se_notification_instance WHERE process_instance_id = 123456;

-- ============ 9. Transfer records by process_instance_id ============
EXPLAIN
SELECT * FROM se_task_transfer_record WHERE process_instance_id = 123456;

-- ============ 10. Operation records by process_instance_id ============
EXPLAIN
SELECT * FROM se_assignee_operation_record WHERE process_instance_id = 123456;

-- ============ 11. Rollback records by process_instance_id ============
EXPLAIN
SELECT * FROM se_process_rollback_record WHERE process_instance_id = 123456;

-- ===========================================
-- Index table queries (NOT partitioned)
-- ===========================================

-- ============ 12. User task index ============
-- Expected: Using index idx_user_task_pending
EXPLAIN
SELECT * FROM se_user_task_index
WHERE tenant_id = 'test' AND assignee_id = 'user001' AND task_status = 'pending';

-- ============ 13. User notification index ============
-- Expected: Using index idx_user_notif_receiver
EXPLAIN
SELECT * FROM se_user_notification_index
WHERE tenant_id = 'test' AND receiver_user_id = 'user001' AND read_status = 'unread';

-- ===========================================
-- Anti-pattern: Query without partition key
-- ===========================================

-- ============ 14. Task by ID only (no process_instance_id) ============
-- Expected: partitions shows ALL partitions (p0,p1,...,p15)
EXPLAIN
SELECT * FROM se_task_instance WHERE id = 123456;

-- ===========================================
-- Verification Checklist
-- ===========================================
-- [ ] Queries 1-11: "partitions" column shows single partition (e.g. "p8")
-- [ ] Query 12-13: "key" column shows the index name
-- [ ] Query 14: "partitions" shows all 16 (p0,p1,...,p15)
