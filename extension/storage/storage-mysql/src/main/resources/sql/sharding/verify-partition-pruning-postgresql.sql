-- ===========================================
-- Partition Pruning Verification Scripts (PostgreSQL)
-- Run these after deploying the sharding schema
-- Each query should show "Scans: 1 of 16" in the plan
-- ===========================================

-- ============ 1. Process instance by ID ============
-- Expected: Only scan 1 partition
EXPLAIN ANALYZE
SELECT * FROM se_process_instance WHERE id = 123456;

-- ============ 2. Task instances by process_instance_id ============
-- Expected: Only scan 1 partition (co-located with process)
EXPLAIN ANALYZE
SELECT * FROM se_task_instance WHERE process_instance_id = 123456;

-- ============ 3. Execution instances by process_instance_id ============
-- Expected: Only scan 1 partition
EXPLAIN ANALYZE
SELECT * FROM se_execution_instance
WHERE process_instance_id = 123456 AND active = 1;

-- ============ 4. Activity instances by process_instance_id ============
-- Expected: Only scan 1 partition
EXPLAIN ANALYZE
SELECT * FROM se_activity_instance WHERE process_instance_id = 123456;

-- ============ 5. Task assignees by process_instance_id ============
-- Expected: Only scan 1 partition
EXPLAIN ANALYZE
SELECT * FROM se_task_assignee_instance WHERE process_instance_id = 123456;

-- ============ 6. Variables by process_instance_id ============
-- Expected: Only scan 1 partition
EXPLAIN ANALYZE
SELECT * FROM se_variable_instance WHERE process_instance_id = 123456;

-- ============ 7. Supervision by process_instance_id ============
-- Expected: Only scan 1 partition
EXPLAIN ANALYZE
SELECT * FROM se_supervision_instance WHERE process_instance_id = 123456;

-- ============ 8. Notifications by process_instance_id ============
-- Expected: Only scan 1 partition
EXPLAIN ANALYZE
SELECT * FROM se_notification_instance WHERE process_instance_id = 123456;

-- ============ 9. Transfer records by process_instance_id ============
-- Expected: Only scan 1 partition
EXPLAIN ANALYZE
SELECT * FROM se_task_transfer_record WHERE process_instance_id = 123456;

-- ============ 10. Operation records by process_instance_id ============
-- Expected: Only scan 1 partition
EXPLAIN ANALYZE
SELECT * FROM se_assignee_operation_record WHERE process_instance_id = 123456;

-- ============ 11. Rollback records by process_instance_id ============
-- Expected: Only scan 1 partition
EXPLAIN ANALYZE
SELECT * FROM se_process_rollback_record WHERE process_instance_id = 123456;

-- ===========================================
-- Cross-partition queries (expected to scan all 16 partitions)
-- These use the index tables instead
-- ===========================================

-- ============ 12. User task index (NOT partitioned) ============
-- Expected: Index scan on uk_user_task_idx or idx_user_task_pending
EXPLAIN ANALYZE
SELECT * FROM se_user_task_index
WHERE tenant_id = 'test' AND assignee_id = 'user001' AND task_status = 'pending';

-- ============ 13. User notification index (NOT partitioned) ============
-- Expected: Index scan on idx_user_notif_unread (partial index)
EXPLAIN ANALYZE
SELECT * FROM se_user_notification_index
WHERE tenant_id = 'test' AND receiver_user_id = 'user001' AND read_status = 'unread';

-- ===========================================
-- Archive table partition pruning
-- ===========================================

-- ============ 14. Archive process by ID ============
EXPLAIN ANALYZE
SELECT * FROM se_process_instance_archive WHERE id = 123456;

-- ============ 15. Archive tasks by process_instance_id ============
EXPLAIN ANALYZE
SELECT * FROM se_task_instance_archive WHERE process_instance_id = 123456;

-- ===========================================
-- Anti-pattern: Query without partition key (should scan all partitions)
-- ===========================================

-- ============ 16. Task by ID only (no process_instance_id) ============
-- Expected: Scan all 16 partitions — this is the anti-pattern
EXPLAIN ANALYZE
SELECT * FROM se_task_instance WHERE id = 123456;

-- ===========================================
-- Verification Checklist
-- ===========================================
-- [ ] Queries 1-11: Each should show "Scans: 1 of 16" or only 1 partition table
-- [ ] Query 12-13: Should show Index Scan (not Seq Scan)
-- [ ] Query 14-15: Each should show only 1 partition scanned
-- [ ] Query 16: Should show all 16 partitions scanned (expected anti-pattern)
