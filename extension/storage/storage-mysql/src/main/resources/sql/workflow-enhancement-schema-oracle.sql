-- Workflow enhancement database schema for Oracle/DM (DaMeng)
-- Compatible with both Oracle Database and DaMeng Database
-- Based on SmartEngine existing table structure design

-- ===========================================
-- Sequences
-- ===========================================
CREATE SEQUENCE se_supervision_instance_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE se_notification_instance_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE se_task_transfer_record_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE se_assignee_operation_record_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE se_process_rollback_record_seq START WITH 1 INCREMENT BY 1;

-- ===========================================
-- Supervision instance table
-- ===========================================
CREATE TABLE se_supervision_instance (
  id                      NUMBER(19) DEFAULT se_supervision_instance_seq.NEXTVAL NOT NULL,
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
  CONSTRAINT pk_supervision_instance PRIMARY KEY (id)
);

COMMENT ON TABLE se_supervision_instance IS 'Supervision instance table';
COMMENT ON COLUMN se_supervision_instance.id IS 'PK';
COMMENT ON COLUMN se_supervision_instance.gmt_create IS 'create time';
COMMENT ON COLUMN se_supervision_instance.gmt_modified IS 'modification time';
COMMENT ON COLUMN se_supervision_instance.process_instance_id IS 'process instance id';
COMMENT ON COLUMN se_supervision_instance.task_instance_id IS 'task instance id';
COMMENT ON COLUMN se_supervision_instance.supervisor_user_id IS 'supervisor user id';
COMMENT ON COLUMN se_supervision_instance.supervision_reason IS 'supervision reason';
COMMENT ON COLUMN se_supervision_instance.supervision_type IS 'supervision type: urge/track/remind';
COMMENT ON COLUMN se_supervision_instance.status IS 'status: active/closed';
COMMENT ON COLUMN se_supervision_instance.close_time IS 'close time';
COMMENT ON COLUMN se_supervision_instance.tenant_id IS 'tenant id';

-- ===========================================
-- Notification instance table
-- ===========================================
CREATE TABLE se_notification_instance (
  id                      NUMBER(19) DEFAULT se_notification_instance_seq.NEXTVAL NOT NULL,
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
  CONSTRAINT pk_notification_instance PRIMARY KEY (id)
);

COMMENT ON TABLE se_notification_instance IS 'Notification instance table';
COMMENT ON COLUMN se_notification_instance.id IS 'PK';
COMMENT ON COLUMN se_notification_instance.gmt_create IS 'create time';
COMMENT ON COLUMN se_notification_instance.gmt_modified IS 'modification time';
COMMENT ON COLUMN se_notification_instance.process_instance_id IS 'process instance id';
COMMENT ON COLUMN se_notification_instance.task_instance_id IS 'task instance id';
COMMENT ON COLUMN se_notification_instance.sender_user_id IS 'sender user id';
COMMENT ON COLUMN se_notification_instance.receiver_user_id IS 'receiver user id';
COMMENT ON COLUMN se_notification_instance.notification_type IS 'notification type: cc/inform';
COMMENT ON COLUMN se_notification_instance.title IS 'notification title';
COMMENT ON COLUMN se_notification_instance.content IS 'notification content';
COMMENT ON COLUMN se_notification_instance.read_status IS 'read status: unread/read';
COMMENT ON COLUMN se_notification_instance.read_time IS 'read time';
COMMENT ON COLUMN se_notification_instance.tenant_id IS 'tenant id';

-- ===========================================
-- Task transfer record table
-- ===========================================
CREATE TABLE se_task_transfer_record (
  id                      NUMBER(19) DEFAULT se_task_transfer_record_seq.NEXTVAL NOT NULL,
  gmt_create              TIMESTAMP(6) NOT NULL,
  gmt_modified            TIMESTAMP(6) NOT NULL,
  task_instance_id        NUMBER(19) NOT NULL,
  from_user_id            VARCHAR2(255) NOT NULL,
  to_user_id              VARCHAR2(255) NOT NULL,
  transfer_reason         VARCHAR2(500) DEFAULT NULL,
  deadline                TIMESTAMP(6) DEFAULT NULL,
  tenant_id               VARCHAR2(64) DEFAULT NULL,
  CONSTRAINT pk_task_transfer_record PRIMARY KEY (id)
);

COMMENT ON TABLE se_task_transfer_record IS 'Task transfer record table';
COMMENT ON COLUMN se_task_transfer_record.id IS 'PK';
COMMENT ON COLUMN se_task_transfer_record.gmt_create IS 'create time';
COMMENT ON COLUMN se_task_transfer_record.gmt_modified IS 'modification time';
COMMENT ON COLUMN se_task_transfer_record.task_instance_id IS 'task instance id';
COMMENT ON COLUMN se_task_transfer_record.from_user_id IS 'from user id';
COMMENT ON COLUMN se_task_transfer_record.to_user_id IS 'to user id';
COMMENT ON COLUMN se_task_transfer_record.transfer_reason IS 'transfer reason';
COMMENT ON COLUMN se_task_transfer_record.deadline IS 'processing deadline';
COMMENT ON COLUMN se_task_transfer_record.tenant_id IS 'tenant id';

-- ===========================================
-- Assignee operation record table
-- ===========================================
CREATE TABLE se_assignee_operation_record (
  id                      NUMBER(19) DEFAULT se_assignee_operation_record_seq.NEXTVAL NOT NULL,
  gmt_create              TIMESTAMP(6) NOT NULL,
  gmt_modified            TIMESTAMP(6) NOT NULL,
  task_instance_id        NUMBER(19) NOT NULL,
  operation_type          VARCHAR2(64) NOT NULL,
  operator_user_id        VARCHAR2(255) NOT NULL,
  target_user_id          VARCHAR2(255) NOT NULL,
  operation_reason        VARCHAR2(500) DEFAULT NULL,
  tenant_id               VARCHAR2(64) DEFAULT NULL,
  CONSTRAINT pk_assignee_operation_record PRIMARY KEY (id)
);

COMMENT ON TABLE se_assignee_operation_record IS 'Assignee operation record table';
COMMENT ON COLUMN se_assignee_operation_record.id IS 'PK';
COMMENT ON COLUMN se_assignee_operation_record.gmt_create IS 'create time';
COMMENT ON COLUMN se_assignee_operation_record.gmt_modified IS 'modification time';
COMMENT ON COLUMN se_assignee_operation_record.task_instance_id IS 'task instance id';
COMMENT ON COLUMN se_assignee_operation_record.operation_type IS 'operation type: add_assignee/remove_assignee';
COMMENT ON COLUMN se_assignee_operation_record.operator_user_id IS 'operator user id';
COMMENT ON COLUMN se_assignee_operation_record.target_user_id IS 'target user id';
COMMENT ON COLUMN se_assignee_operation_record.operation_reason IS 'operation reason';
COMMENT ON COLUMN se_assignee_operation_record.tenant_id IS 'tenant id';

-- ===========================================
-- Process rollback record table
-- ===========================================
CREATE TABLE se_process_rollback_record (
  id                      NUMBER(19) DEFAULT se_process_rollback_record_seq.NEXTVAL NOT NULL,
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
  CONSTRAINT pk_process_rollback_record PRIMARY KEY (id)
);

COMMENT ON TABLE se_process_rollback_record IS 'Process rollback record table';
COMMENT ON COLUMN se_process_rollback_record.id IS 'PK';
COMMENT ON COLUMN se_process_rollback_record.gmt_create IS 'create time';
COMMENT ON COLUMN se_process_rollback_record.gmt_modified IS 'modification time';
COMMENT ON COLUMN se_process_rollback_record.process_instance_id IS 'process instance id';
COMMENT ON COLUMN se_process_rollback_record.task_instance_id IS 'task instance id';
COMMENT ON COLUMN se_process_rollback_record.rollback_type IS 'rollback type: previous/specific';
COMMENT ON COLUMN se_process_rollback_record.from_activity_id IS 'from activity id';
COMMENT ON COLUMN se_process_rollback_record.to_activity_id IS 'to activity id';
COMMENT ON COLUMN se_process_rollback_record.operator_user_id IS 'operator user id';
COMMENT ON COLUMN se_process_rollback_record.rollback_reason IS 'rollback reason';
COMMENT ON COLUMN se_process_rollback_record.tenant_id IS 'tenant id';
