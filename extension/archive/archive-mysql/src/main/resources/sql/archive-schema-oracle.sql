-- Archive tables for Oracle/DM (DaMeng)
-- Mirror of runtime tables with additional archived_time column
-- PK is non-auto-increment (preserves original id values)

-- ===========================================
-- 1. se_process_instance_archive
-- ===========================================
CREATE TABLE se_process_instance_archive (
  id                                    NUMBER(19) NOT NULL,
  gmt_create                            TIMESTAMP(6) NOT NULL,
  gmt_modified                          TIMESTAMP(6) NOT NULL,
  process_definition_id_and_version     VARCHAR2(128) NOT NULL,
  process_definition_type               VARCHAR2(255) DEFAULT NULL,
  status                                VARCHAR2(64) NOT NULL,
  complete_time                         TIMESTAMP(6) DEFAULT NULL,
  parent_process_instance_id            NUMBER(19) DEFAULT NULL,
  parent_execution_instance_id          NUMBER(19) DEFAULT NULL,
  start_user_id                         VARCHAR2(128) DEFAULT NULL,
  biz_unique_id                         VARCHAR2(255) DEFAULT NULL,
  reason                                VARCHAR2(255) DEFAULT NULL,
  comment                               VARCHAR2(255) DEFAULT NULL,
  title                                 VARCHAR2(255) DEFAULT NULL,
  tag                                   VARCHAR2(255) DEFAULT NULL,
  tenant_id                             VARCHAR2(64) DEFAULT NULL,
  archived_time                         TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
  CONSTRAINT pk_archive_process_instance PRIMARY KEY (id)
);

COMMENT ON TABLE se_process_instance_archive IS 'Process instance archive table';

CREATE INDEX idx_archive_pi_tenant_status ON se_process_instance_archive (tenant_id, status);
CREATE INDEX idx_archive_pi_archived_time ON se_process_instance_archive (archived_time);

-- ===========================================
-- 2. se_task_instance_archive
-- ===========================================
CREATE TABLE se_task_instance_archive (
  id                                    NUMBER(19) NOT NULL,
  gmt_create                            TIMESTAMP(6) NOT NULL,
  gmt_modified                          TIMESTAMP(6) NOT NULL,
  process_instance_id                   NUMBER(19) NOT NULL,
  process_definition_id_and_version     VARCHAR2(128) DEFAULT NULL,
  process_definition_type               VARCHAR2(255) DEFAULT NULL,
  activity_instance_id                  NUMBER(19) NOT NULL,
  process_definition_activity_id        VARCHAR2(255) NOT NULL,
  execution_instance_id                 NUMBER(19) NOT NULL,
  claim_user_id                         VARCHAR2(255) DEFAULT NULL,
  title                                 VARCHAR2(255) DEFAULT NULL,
  priority                              NUMBER(10) DEFAULT 500,
  tag                                   VARCHAR2(255) DEFAULT NULL,
  claim_time                            TIMESTAMP(6) DEFAULT NULL,
  complete_time                         TIMESTAMP(6) DEFAULT NULL,
  status                                VARCHAR2(255) NOT NULL,
  comment                               VARCHAR2(255) DEFAULT NULL,
  extension                             VARCHAR2(255) DEFAULT NULL,
  domain_code                           VARCHAR2(64) DEFAULT NULL,
  extra                                 CLOB DEFAULT NULL CHECK (extra IS JSON),
  tenant_id                             VARCHAR2(64) DEFAULT NULL,
  archived_time                         TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
  CONSTRAINT pk_archive_task_instance PRIMARY KEY (id)
);

COMMENT ON TABLE se_task_instance_archive IS 'Task instance archive table';

CREATE INDEX idx_archive_ti_process_id ON se_task_instance_archive (process_instance_id);
CREATE INDEX idx_archive_ti_archived_time ON se_task_instance_archive (archived_time);

-- ===========================================
-- 3. se_execution_instance_archive
-- ===========================================
CREATE TABLE se_execution_instance_archive (
  id                                    NUMBER(19) NOT NULL,
  gmt_create                            TIMESTAMP(6) NOT NULL,
  gmt_modified                          TIMESTAMP(6) NOT NULL,
  process_instance_id                   NUMBER(19) NOT NULL,
  process_definition_id_and_version     VARCHAR2(255) NOT NULL,
  process_definition_activity_id        VARCHAR2(255) NOT NULL,
  activity_instance_id                  NUMBER(19) NOT NULL,
  block_id                              NUMBER(19) DEFAULT NULL,
  active                                NUMBER(1) NOT NULL,
  tenant_id                             VARCHAR2(64) DEFAULT NULL,
  archived_time                         TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
  CONSTRAINT pk_archive_execution_instance PRIMARY KEY (id)
);

COMMENT ON TABLE se_execution_instance_archive IS 'Execution instance archive table';

CREATE INDEX idx_archive_ei_process_id ON se_execution_instance_archive (process_instance_id);
CREATE INDEX idx_archive_ei_archived_time ON se_execution_instance_archive (archived_time);

-- ===========================================
-- 4. se_activity_instance_archive
-- ===========================================
CREATE TABLE se_activity_instance_archive (
  id                                    NUMBER(19) NOT NULL,
  gmt_create                            TIMESTAMP(6) NOT NULL,
  gmt_modified                          TIMESTAMP(6) NOT NULL,
  process_instance_id                   NUMBER(19) DEFAULT NULL,
  process_definition_id_and_version     VARCHAR2(255) NOT NULL,
  process_definition_activity_id        VARCHAR2(64) NOT NULL,
  tenant_id                             VARCHAR2(64) DEFAULT NULL,
  archived_time                         TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
  CONSTRAINT pk_archive_activity_instance PRIMARY KEY (id)
);

COMMENT ON TABLE se_activity_instance_archive IS 'Activity instance archive table';

CREATE INDEX idx_archive_ai_process_id ON se_activity_instance_archive (process_instance_id);
CREATE INDEX idx_archive_ai_archived_time ON se_activity_instance_archive (archived_time);

-- ===========================================
-- 5. se_variable_instance_archive
-- ===========================================
CREATE TABLE se_variable_instance_archive (
  id                      NUMBER(19) NOT NULL,
  gmt_create              TIMESTAMP(6) NOT NULL,
  gmt_modified            TIMESTAMP(6) NOT NULL,
  process_instance_id     NUMBER(19) NOT NULL,
  execution_instance_id   NUMBER(19) DEFAULT NULL,
  field_key               VARCHAR2(128) NOT NULL,
  field_type              VARCHAR2(128) NOT NULL,
  field_double_value      NUMBER(65,30) DEFAULT NULL,
  field_long_value        NUMBER(19) DEFAULT NULL,
  field_string_value      VARCHAR2(4000) DEFAULT NULL,
  tenant_id               VARCHAR2(64) DEFAULT NULL,
  archived_time           TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
  CONSTRAINT pk_archive_variable_instance PRIMARY KEY (id)
);

COMMENT ON TABLE se_variable_instance_archive IS 'Variable instance archive table';

CREATE INDEX idx_archive_vi_process_id ON se_variable_instance_archive (process_instance_id);
CREATE INDEX idx_archive_vi_archived_time ON se_variable_instance_archive (archived_time);

-- ===========================================
-- 6. se_task_assignee_instance_archive
-- ===========================================
CREATE TABLE se_task_assignee_instance_archive (
  id                    NUMBER(19) NOT NULL,
  gmt_create            TIMESTAMP(6) NOT NULL,
  gmt_modified          TIMESTAMP(6) NOT NULL,
  process_instance_id   NUMBER(19) NOT NULL,
  task_instance_id      NUMBER(19) NOT NULL,
  assignee_id           VARCHAR2(255) NOT NULL,
  assignee_type         VARCHAR2(128) NOT NULL,
  tenant_id             VARCHAR2(64) DEFAULT NULL,
  archived_time         TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
  CONSTRAINT pk_archive_task_assignee PRIMARY KEY (id)
);

COMMENT ON TABLE se_task_assignee_instance_archive IS 'Task assignee instance archive table';

CREATE INDEX idx_archive_tai_process_id ON se_task_assignee_instance_archive (process_instance_id);
CREATE INDEX idx_archive_tai_archived_time ON se_task_assignee_instance_archive (archived_time);

-- ===========================================
-- 7. se_supervision_instance_archive
-- ===========================================
CREATE TABLE se_supervision_instance_archive (
  id                      NUMBER(19) NOT NULL,
  gmt_create              TIMESTAMP(6) NOT NULL,
  gmt_modified            TIMESTAMP(6) NOT NULL,
  process_instance_id     NUMBER(19) NOT NULL,
  task_instance_id        NUMBER(19) NOT NULL,
  supervisor_user_id      VARCHAR2(255) NOT NULL,
  supervision_reason      VARCHAR2(500) DEFAULT NULL,
  supervision_type        VARCHAR2(64) NOT NULL,
  status                  VARCHAR2(64) NOT NULL,
  close_time              TIMESTAMP(6) DEFAULT NULL,
  tenant_id               VARCHAR2(64) DEFAULT NULL,
  archived_time           TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
  CONSTRAINT pk_archive_supervision PRIMARY KEY (id)
);

COMMENT ON TABLE se_supervision_instance_archive IS 'Supervision instance archive table';

CREATE INDEX idx_archive_si_process_id ON se_supervision_instance_archive (process_instance_id);
CREATE INDEX idx_archive_si_archived_time ON se_supervision_instance_archive (archived_time);

-- ===========================================
-- 8. se_notification_instance_archive
-- ===========================================
CREATE TABLE se_notification_instance_archive (
  id                      NUMBER(19) NOT NULL,
  gmt_create              TIMESTAMP(6) NOT NULL,
  gmt_modified            TIMESTAMP(6) NOT NULL,
  process_instance_id     NUMBER(19) NOT NULL,
  task_instance_id        NUMBER(19) DEFAULT NULL,
  sender_user_id          VARCHAR2(255) NOT NULL,
  receiver_user_id        VARCHAR2(255) NOT NULL,
  notification_type       VARCHAR2(64) NOT NULL,
  title                   VARCHAR2(255) DEFAULT NULL,
  content                 VARCHAR2(1000) DEFAULT NULL,
  read_status             VARCHAR2(64) DEFAULT 'unread' NOT NULL,
  read_time               TIMESTAMP(6) DEFAULT NULL,
  tenant_id               VARCHAR2(64) DEFAULT NULL,
  archived_time           TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
  CONSTRAINT pk_archive_notification PRIMARY KEY (id)
);

COMMENT ON TABLE se_notification_instance_archive IS 'Notification instance archive table';

CREATE INDEX idx_archive_ni_process_id ON se_notification_instance_archive (process_instance_id);
CREATE INDEX idx_archive_ni_archived_time ON se_notification_instance_archive (archived_time);

-- ===========================================
-- 9. se_task_transfer_record_archive
-- ===========================================
CREATE TABLE se_task_transfer_record_archive (
  id                      NUMBER(19) NOT NULL,
  gmt_create              TIMESTAMP(6) NOT NULL,
  gmt_modified            TIMESTAMP(6) NOT NULL,
  task_instance_id        NUMBER(19) NOT NULL,
  from_user_id            VARCHAR2(255) NOT NULL,
  to_user_id              VARCHAR2(255) NOT NULL,
  transfer_reason         VARCHAR2(500) DEFAULT NULL,
  deadline                TIMESTAMP(6) DEFAULT NULL,
  tenant_id               VARCHAR2(64) DEFAULT NULL,
  archived_time           TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
  CONSTRAINT pk_archive_task_transfer PRIMARY KEY (id)
);

COMMENT ON TABLE se_task_transfer_record_archive IS 'Task transfer record archive table';

CREATE INDEX idx_archive_ttr_task_id ON se_task_transfer_record_archive (task_instance_id);
CREATE INDEX idx_archive_ttr_archived_time ON se_task_transfer_record_archive (archived_time);

-- ===========================================
-- 10. se_assignee_operation_record_archive
-- ===========================================
CREATE TABLE se_assignee_operation_record_archive (
  id                      NUMBER(19) NOT NULL,
  gmt_create              TIMESTAMP(6) NOT NULL,
  gmt_modified            TIMESTAMP(6) NOT NULL,
  task_instance_id        NUMBER(19) NOT NULL,
  operation_type          VARCHAR2(64) NOT NULL,
  operator_user_id        VARCHAR2(255) NOT NULL,
  target_user_id          VARCHAR2(255) NOT NULL,
  operation_reason        VARCHAR2(500) DEFAULT NULL,
  tenant_id               VARCHAR2(64) DEFAULT NULL,
  archived_time           TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
  CONSTRAINT pk_archive_assignee_op PRIMARY KEY (id)
);

COMMENT ON TABLE se_assignee_operation_record_archive IS 'Assignee operation record archive table';

CREATE INDEX idx_archive_aor_task_id ON se_assignee_operation_record_archive (task_instance_id);
CREATE INDEX idx_archive_aor_archived_time ON se_assignee_operation_record_archive (archived_time);

-- ===========================================
-- 11. se_process_rollback_record_archive
-- ===========================================
CREATE TABLE se_process_rollback_record_archive (
  id                      NUMBER(19) NOT NULL,
  gmt_create              TIMESTAMP(6) NOT NULL,
  gmt_modified            TIMESTAMP(6) NOT NULL,
  process_instance_id     NUMBER(19) NOT NULL,
  task_instance_id        NUMBER(19) NOT NULL,
  rollback_type           VARCHAR2(64) NOT NULL,
  from_activity_id        VARCHAR2(255) NOT NULL,
  to_activity_id          VARCHAR2(255) NOT NULL,
  operator_user_id        VARCHAR2(255) NOT NULL,
  rollback_reason         VARCHAR2(500) DEFAULT NULL,
  tenant_id               VARCHAR2(64) DEFAULT NULL,
  archived_time           TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
  CONSTRAINT pk_archive_rollback PRIMARY KEY (id)
);

COMMENT ON TABLE se_process_rollback_record_archive IS 'Process rollback record archive table';

CREATE INDEX idx_archive_prr_process_id ON se_process_rollback_record_archive (process_instance_id);
CREATE INDEX idx_archive_prr_archived_time ON se_process_rollback_record_archive (archived_time);
