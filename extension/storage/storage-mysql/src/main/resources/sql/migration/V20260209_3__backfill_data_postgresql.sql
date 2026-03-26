-- Data backfill migration script (PostgreSQL version)
-- Phase 3: Populate process_instance_id and build index tables from existing data

-- ===========================================
-- Step 1: Backfill process_instance_id in se_task_transfer_record
-- ===========================================
UPDATE se_task_transfer_record r
SET process_instance_id = t.process_instance_id
FROM se_task_instance t
WHERE r.task_instance_id = t.id
  AND r.process_instance_id IS NULL;

-- ===========================================
-- Step 2: Backfill process_instance_id in se_assignee_operation_record
-- ===========================================
UPDATE se_assignee_operation_record r
SET process_instance_id = t.process_instance_id
FROM se_task_instance t
WHERE r.task_instance_id = t.id
  AND r.process_instance_id IS NULL;

-- ===========================================
-- Step 3: Full build se_user_task_index from existing data
-- Only insert active/pending tasks (not completed/canceled/aborted)
-- ===========================================
INSERT INTO se_user_task_index
    (tenant_id, assignee_id, assignee_type, task_instance_id, process_instance_id,
     process_definition_type, domain_code, extra, task_status, task_gmt_modified,
     title, priority)
SELECT
    ta.tenant_id,
    ta.assignee_id,
    ta.assignee_type,
    ta.task_instance_id,
    ta.process_instance_id,
    t.process_definition_type,
    t.domain_code,
    t.extra,
    t.status,
    t.gmt_modified,
    t.title,
    t.priority
FROM se_task_assignee_instance ta
INNER JOIN se_task_instance t ON ta.task_instance_id = t.id
    AND ta.process_instance_id = t.process_instance_id
WHERE t.status NOT IN ('completed', 'canceled', 'aborted')
ON CONFLICT (tenant_id, assignee_id, task_instance_id) DO UPDATE SET
    task_status = EXCLUDED.task_status,
    task_gmt_modified = EXCLUDED.task_gmt_modified,
    title = EXCLUDED.title,
    priority = EXCLUDED.priority;

-- ===========================================
-- Step 4: Full build se_user_notification_index from existing data
-- Only insert unread notifications
-- ===========================================
INSERT INTO se_user_notification_index
    (tenant_id, receiver_user_id, notification_id, process_instance_id,
     notification_type, title, read_status, gmt_create)
SELECT
    n.tenant_id,
    n.receiver_user_id,
    n.id,
    n.process_instance_id,
    n.notification_type,
    n.title,
    n.read_status,
    n.gmt_create
FROM se_notification_instance n
WHERE n.read_status = 'unread'
ON CONFLICT (tenant_id, receiver_user_id, notification_id) DO UPDATE SET
    read_status = EXCLUDED.read_status,
    title = EXCLUDED.title;
