-- Archive tables for PostgreSQL
-- Mirror of runtime tables with additional archived_time column
-- PK is non-auto-increment (preserves original id values)

-- ===========================================
-- 1. se_process_instance_archive
-- ===========================================
CREATE TABLE se_process_instance_archive (
  id bigint NOT NULL,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  process_definition_id_and_version varchar(128) NOT NULL,
  process_definition_type varchar(255),
  status varchar(64) NOT NULL,
  complete_time timestamp(6),
  parent_process_instance_id bigint,
  parent_execution_instance_id bigint,
  start_user_id varchar(128),
  biz_unique_id varchar(255),
  reason varchar(255),
  comment varchar(255),
  title varchar(255),
  tag varchar(255),
  tenant_id varchar(64),
  archived_time timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE INDEX idx_archive_pi_tenant_status ON se_process_instance_archive (tenant_id, status);
CREATE INDEX idx_archive_pi_archived_time ON se_process_instance_archive (archived_time);

-- ===========================================
-- 2. se_task_instance_archive
-- ===========================================
CREATE TABLE se_task_instance_archive (
  id bigint NOT NULL,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  process_instance_id bigint NOT NULL,
  process_definition_id_and_version varchar(128),
  process_definition_type varchar(255),
  activity_instance_id bigint NOT NULL,
  process_definition_activity_id varchar(255) NOT NULL,
  execution_instance_id bigint NOT NULL,
  claim_user_id varchar(255),
  title varchar(255),
  priority int DEFAULT 500,
  tag varchar(255),
  claim_time timestamp(6),
  complete_time timestamp(6),
  status varchar(255) NOT NULL,
  comment varchar(255),
  extension varchar(255),
  domain_code varchar(64),
  extra jsonb,
  tenant_id varchar(64),
  archived_time timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE INDEX idx_archive_ti_process_instance_id ON se_task_instance_archive (process_instance_id);
CREATE INDEX idx_archive_ti_archived_time ON se_task_instance_archive (archived_time);

-- ===========================================
-- 3. se_execution_instance_archive
-- ===========================================
CREATE TABLE se_execution_instance_archive (
  id bigint NOT NULL,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  process_instance_id bigint NOT NULL,
  process_definition_id_and_version varchar(255) NOT NULL,
  process_definition_activity_id varchar(255) NOT NULL,
  activity_instance_id bigint NOT NULL,
  block_id bigint,
  active smallint NOT NULL,
  tenant_id varchar(64),
  archived_time timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE INDEX idx_archive_ei_process_instance_id ON se_execution_instance_archive (process_instance_id);
CREATE INDEX idx_archive_ei_archived_time ON se_execution_instance_archive (archived_time);

-- ===========================================
-- 4. se_activity_instance_archive
-- ===========================================
CREATE TABLE se_activity_instance_archive (
  id bigint NOT NULL,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  process_instance_id bigint,
  process_definition_id_and_version varchar(255) NOT NULL,
  process_definition_activity_id varchar(64) NOT NULL,
  tenant_id varchar(64),
  archived_time timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE INDEX idx_archive_ai_process_instance_id ON se_activity_instance_archive (process_instance_id);
CREATE INDEX idx_archive_ai_archived_time ON se_activity_instance_archive (archived_time);

-- ===========================================
-- 5. se_variable_instance_archive
-- ===========================================
CREATE TABLE se_variable_instance_archive (
  id bigint NOT NULL,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  process_instance_id bigint NOT NULL,
  execution_instance_id bigint,
  field_key varchar(128) NOT NULL,
  field_type varchar(128) NOT NULL,
  field_double_value decimal(65,30),
  field_long_value bigint,
  field_string_value varchar(4000),
  tenant_id varchar(64),
  archived_time timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE INDEX idx_archive_vi_process_instance_id ON se_variable_instance_archive (process_instance_id);
CREATE INDEX idx_archive_vi_archived_time ON se_variable_instance_archive (archived_time);

-- ===========================================
-- 6. se_task_assignee_instance_archive
-- ===========================================
CREATE TABLE se_task_assignee_instance_archive (
  id bigint NOT NULL,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  process_instance_id bigint NOT NULL,
  task_instance_id bigint NOT NULL,
  assignee_id varchar(255) NOT NULL,
  assignee_type varchar(128) NOT NULL,
  tenant_id varchar(64),
  archived_time timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE INDEX idx_archive_tai_process_instance_id ON se_task_assignee_instance_archive (process_instance_id);
CREATE INDEX idx_archive_tai_archived_time ON se_task_assignee_instance_archive (archived_time);

-- ===========================================
-- 7. se_supervision_instance_archive
-- ===========================================
CREATE TABLE se_supervision_instance_archive (
  id bigint NOT NULL,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  process_instance_id bigint NOT NULL,
  task_instance_id bigint NOT NULL,
  supervisor_user_id varchar(255) NOT NULL,
  supervision_reason varchar(500),
  supervision_type varchar(64) NOT NULL,
  status varchar(64) NOT NULL,
  close_time timestamp(6),
  tenant_id varchar(64),
  archived_time timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE INDEX idx_archive_si_process_instance_id ON se_supervision_instance_archive (process_instance_id);
CREATE INDEX idx_archive_si_archived_time ON se_supervision_instance_archive (archived_time);

-- ===========================================
-- 8. se_notification_instance_archive
-- ===========================================
CREATE TABLE se_notification_instance_archive (
  id bigint NOT NULL,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  process_instance_id bigint NOT NULL,
  task_instance_id bigint,
  sender_user_id varchar(255) NOT NULL,
  receiver_user_id varchar(255) NOT NULL,
  notification_type varchar(64) NOT NULL,
  title varchar(255),
  content varchar(1000),
  read_status varchar(64) NOT NULL DEFAULT 'unread',
  read_time timestamp(6),
  tenant_id varchar(64),
  archived_time timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE INDEX idx_archive_ni_process_instance_id ON se_notification_instance_archive (process_instance_id);
CREATE INDEX idx_archive_ni_archived_time ON se_notification_instance_archive (archived_time);

-- ===========================================
-- 9. se_task_transfer_record_archive
-- ===========================================
CREATE TABLE se_task_transfer_record_archive (
  id bigint NOT NULL,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  task_instance_id bigint NOT NULL,
  process_instance_id bigint,
  from_user_id varchar(255) NOT NULL,
  to_user_id varchar(255) NOT NULL,
  transfer_reason varchar(500),
  deadline timestamp(6),
  tenant_id varchar(64),
  archived_time timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE INDEX idx_archive_ttr_task_instance_id ON se_task_transfer_record_archive (task_instance_id);
CREATE INDEX idx_archive_ttr_archived_time ON se_task_transfer_record_archive (archived_time);

-- ===========================================
-- 10. se_assignee_operation_record_archive
-- ===========================================
CREATE TABLE se_assignee_operation_record_archive (
  id bigint NOT NULL,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  task_instance_id bigint NOT NULL,
  process_instance_id bigint,
  operation_type varchar(64) NOT NULL,
  operator_user_id varchar(255) NOT NULL,
  target_user_id varchar(255) NOT NULL,
  operation_reason varchar(500),
  tenant_id varchar(64),
  archived_time timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE INDEX idx_archive_aor_task_instance_id ON se_assignee_operation_record_archive (task_instance_id);
CREATE INDEX idx_archive_aor_archived_time ON se_assignee_operation_record_archive (archived_time);

-- ===========================================
-- 11. se_process_rollback_record_archive
-- ===========================================
CREATE TABLE se_process_rollback_record_archive (
  id bigint NOT NULL,
  gmt_create timestamp(6) NOT NULL,
  gmt_modified timestamp(6) NOT NULL,
  process_instance_id bigint NOT NULL,
  task_instance_id bigint NOT NULL,
  rollback_type varchar(64) NOT NULL,
  from_activity_id varchar(255) NOT NULL,
  to_activity_id varchar(255) NOT NULL,
  operator_user_id varchar(255) NOT NULL,
  rollback_reason varchar(500),
  tenant_id varchar(64),
  archived_time timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE INDEX idx_archive_prr_process_instance_id ON se_process_rollback_record_archive (process_instance_id);
CREATE INDEX idx_archive_prr_archived_time ON se_process_rollback_record_archive (archived_time);
