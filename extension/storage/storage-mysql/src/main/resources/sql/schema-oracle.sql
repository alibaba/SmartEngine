-- Oracle/DM (DaMeng) database schema for SmartEngine
-- Compatible with both Oracle Database and DaMeng Database
-- Based on SmartEngine existing table structure design

-- ===========================================
-- Sequences
-- ===========================================
CREATE SEQUENCE se_deployment_instance_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE se_process_instance_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE se_activity_instance_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE se_task_instance_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE se_execution_instance_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE se_task_assignee_instance_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE se_variable_instance_seq START WITH 1 INCREMENT BY 1;

-- ===========================================
-- Deployment instance table
-- ===========================================
CREATE TABLE se_deployment_instance (
  id                           NUMBER(19) DEFAULT se_deployment_instance_seq.NEXTVAL NOT NULL,
  gmt_create                   TIMESTAMP(6) NOT NULL,
  gmt_modified                 TIMESTAMP(6) NOT NULL,
  process_definition_id        VARCHAR2(255) NOT NULL,
  process_definition_version   VARCHAR2(255) DEFAULT NULL,
  process_definition_type      VARCHAR2(255) DEFAULT NULL,
  process_definition_code      VARCHAR2(255) DEFAULT NULL,
  process_definition_name      VARCHAR2(255) DEFAULT NULL,
  process_definition_desc      VARCHAR2(255) DEFAULT NULL,
  process_definition_content   CLOB NOT NULL,
  deployment_user_id           VARCHAR2(128) NOT NULL,
  deployment_status            VARCHAR2(64) NOT NULL,
  logic_status                 VARCHAR2(64) NOT NULL,
  extension                    CLOB DEFAULT NULL,
  tenant_id                    VARCHAR2(64) DEFAULT NULL,
  CONSTRAINT pk_deployment_instance PRIMARY KEY (id)
);

COMMENT ON TABLE se_deployment_instance IS 'Deployment instance table';
COMMENT ON COLUMN se_deployment_instance.id IS 'PK';
COMMENT ON COLUMN se_deployment_instance.gmt_create IS 'create time';
COMMENT ON COLUMN se_deployment_instance.gmt_modified IS 'modification time';
COMMENT ON COLUMN se_deployment_instance.process_definition_id IS 'process definition id';
COMMENT ON COLUMN se_deployment_instance.process_definition_version IS 'process definition version';
COMMENT ON COLUMN se_deployment_instance.process_definition_type IS 'process definition type';
COMMENT ON COLUMN se_deployment_instance.process_definition_code IS 'process definition code';
COMMENT ON COLUMN se_deployment_instance.process_definition_name IS 'process definition name';
COMMENT ON COLUMN se_deployment_instance.process_definition_desc IS 'process definition desc';
COMMENT ON COLUMN se_deployment_instance.process_definition_content IS 'process definition content';
COMMENT ON COLUMN se_deployment_instance.deployment_user_id IS 'deployment user id';
COMMENT ON COLUMN se_deployment_instance.deployment_status IS 'deployment status';
COMMENT ON COLUMN se_deployment_instance.logic_status IS 'logic status';
COMMENT ON COLUMN se_deployment_instance.extension IS 'extension';
COMMENT ON COLUMN se_deployment_instance.tenant_id IS 'tenant id';

-- ===========================================
-- Process instance table
-- ===========================================
CREATE TABLE se_process_instance (
  id                                    NUMBER(19) DEFAULT se_process_instance_seq.NEXTVAL NOT NULL,
  gmt_create                            TIMESTAMP(6) NOT NULL,
  gmt_modified                          TIMESTAMP(6) NOT NULL,
  process_definition_id_and_version     VARCHAR2(128) NOT NULL,
  process_definition_type               VARCHAR2(255) DEFAULT NULL,
  status                                VARCHAR2(64) NOT NULL,
  parent_process_instance_id            NUMBER(19) DEFAULT NULL,
  parent_execution_instance_id          NUMBER(19) DEFAULT NULL,
  start_user_id                         VARCHAR2(128) DEFAULT NULL,
  biz_unique_id                         VARCHAR2(255) DEFAULT NULL,
  reason                                VARCHAR2(255) DEFAULT NULL,
  comment                               VARCHAR2(255) DEFAULT NULL,
  title                                 VARCHAR2(255) DEFAULT NULL,
  tag                                   VARCHAR2(255) DEFAULT NULL,
  tenant_id                             VARCHAR2(64) DEFAULT NULL,
  CONSTRAINT pk_process_instance PRIMARY KEY (id)
);

COMMENT ON TABLE se_process_instance IS 'Process instance table';
COMMENT ON COLUMN se_process_instance.id IS 'PK';
COMMENT ON COLUMN se_process_instance.gmt_create IS 'create time';
COMMENT ON COLUMN se_process_instance.gmt_modified IS 'modification time';
COMMENT ON COLUMN se_process_instance.process_definition_id_and_version IS 'process definition id and version';
COMMENT ON COLUMN se_process_instance.process_definition_type IS 'process definition type';
COMMENT ON COLUMN se_process_instance.status IS '1.running 2.completed 3.aborted';
COMMENT ON COLUMN se_process_instance.parent_process_instance_id IS 'parent process instance id';
COMMENT ON COLUMN se_process_instance.parent_execution_instance_id IS 'parent execution instance id';
COMMENT ON COLUMN se_process_instance.start_user_id IS 'start user id';
COMMENT ON COLUMN se_process_instance.biz_unique_id IS 'biz unique id';
COMMENT ON COLUMN se_process_instance.reason IS 'reason';
COMMENT ON COLUMN se_process_instance.comment IS 'comment';
COMMENT ON COLUMN se_process_instance.title IS 'title';
COMMENT ON COLUMN se_process_instance.tag IS 'tag';
COMMENT ON COLUMN se_process_instance.tenant_id IS 'tenant id';

-- ===========================================
-- Activity instance table
-- ===========================================
CREATE TABLE se_activity_instance (
  id                                    NUMBER(19) DEFAULT se_activity_instance_seq.NEXTVAL NOT NULL,
  gmt_create                            TIMESTAMP(6) NOT NULL,
  gmt_modified                          TIMESTAMP(6) NOT NULL,
  process_instance_id                   NUMBER(19) DEFAULT NULL,
  process_definition_id_and_version     VARCHAR2(255) NOT NULL,
  process_definition_activity_id        VARCHAR2(64) NOT NULL,
  tenant_id                             VARCHAR2(64) DEFAULT NULL,
  CONSTRAINT pk_activity_instance PRIMARY KEY (id)
);

COMMENT ON TABLE se_activity_instance IS 'Activity instance table';
COMMENT ON COLUMN se_activity_instance.id IS 'PK';
COMMENT ON COLUMN se_activity_instance.gmt_create IS 'create time';
COMMENT ON COLUMN se_activity_instance.gmt_modified IS 'modification time';
COMMENT ON COLUMN se_activity_instance.process_instance_id IS 'process instance id';
COMMENT ON COLUMN se_activity_instance.process_definition_id_and_version IS 'process definition id and version';
COMMENT ON COLUMN se_activity_instance.process_definition_activity_id IS 'process definition activity id';
COMMENT ON COLUMN se_activity_instance.tenant_id IS 'tenant id';

-- ===========================================
-- Task instance table
-- ===========================================
CREATE TABLE se_task_instance (
  id                                    NUMBER(19) DEFAULT se_task_instance_seq.NEXTVAL NOT NULL,
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
  tenant_id                             VARCHAR2(64) DEFAULT NULL,
  CONSTRAINT pk_task_instance PRIMARY KEY (id)
);

COMMENT ON TABLE se_task_instance IS 'Task instance table';
COMMENT ON COLUMN se_task_instance.id IS 'PK';
COMMENT ON COLUMN se_task_instance.gmt_create IS 'create time';
COMMENT ON COLUMN se_task_instance.gmt_modified IS 'modification time';
COMMENT ON COLUMN se_task_instance.process_instance_id IS 'process instance id';
COMMENT ON COLUMN se_task_instance.process_definition_id_and_version IS 'process definition id and version';
COMMENT ON COLUMN se_task_instance.process_definition_type IS 'process definition type';
COMMENT ON COLUMN se_task_instance.activity_instance_id IS 'activity instance id';
COMMENT ON COLUMN se_task_instance.process_definition_activity_id IS 'process definition activity id';
COMMENT ON COLUMN se_task_instance.execution_instance_id IS 'execution instance id';
COMMENT ON COLUMN se_task_instance.claim_user_id IS 'claim user id';
COMMENT ON COLUMN se_task_instance.title IS 'title';
COMMENT ON COLUMN se_task_instance.priority IS 'priority';
COMMENT ON COLUMN se_task_instance.tag IS 'tag';
COMMENT ON COLUMN se_task_instance.claim_time IS 'claim time';
COMMENT ON COLUMN se_task_instance.complete_time IS 'complete time';
COMMENT ON COLUMN se_task_instance.status IS 'status';
COMMENT ON COLUMN se_task_instance.comment IS 'comment';
COMMENT ON COLUMN se_task_instance.extension IS 'extension';
COMMENT ON COLUMN se_task_instance.tenant_id IS 'tenant id';

-- ===========================================
-- Execution instance table
-- ===========================================
CREATE TABLE se_execution_instance (
  id                                    NUMBER(19) DEFAULT se_execution_instance_seq.NEXTVAL NOT NULL,
  gmt_create                            TIMESTAMP(6) NOT NULL,
  gmt_modified                          TIMESTAMP(6) NOT NULL,
  process_instance_id                   NUMBER(19) NOT NULL,
  process_definition_id_and_version     VARCHAR2(255) NOT NULL,
  process_definition_activity_id        VARCHAR2(255) NOT NULL,
  activity_instance_id                  NUMBER(19) NOT NULL,
  block_id                              NUMBER(19) DEFAULT NULL,
  active                                NUMBER(1) NOT NULL,
  tenant_id                             VARCHAR2(64) DEFAULT NULL,
  CONSTRAINT pk_execution_instance PRIMARY KEY (id)
);

COMMENT ON TABLE se_execution_instance IS 'Execution instance table';
COMMENT ON COLUMN se_execution_instance.id IS 'PK';
COMMENT ON COLUMN se_execution_instance.gmt_create IS 'create time';
COMMENT ON COLUMN se_execution_instance.gmt_modified IS 'modification time';
COMMENT ON COLUMN se_execution_instance.process_instance_id IS 'process instance id';
COMMENT ON COLUMN se_execution_instance.process_definition_id_and_version IS 'process definition id and version';
COMMENT ON COLUMN se_execution_instance.process_definition_activity_id IS 'process definition activity id';
COMMENT ON COLUMN se_execution_instance.activity_instance_id IS 'activity instance id';
COMMENT ON COLUMN se_execution_instance.block_id IS 'block id';
COMMENT ON COLUMN se_execution_instance.active IS '1:active 0:inactive';
COMMENT ON COLUMN se_execution_instance.tenant_id IS 'tenant id';

-- ===========================================
-- Task assignee instance table
-- ===========================================
CREATE TABLE se_task_assignee_instance (
  id                    NUMBER(19) DEFAULT se_task_assignee_instance_seq.NEXTVAL NOT NULL,
  gmt_create            TIMESTAMP(6) NOT NULL,
  gmt_modified          TIMESTAMP(6) NOT NULL,
  process_instance_id   NUMBER(19) NOT NULL,
  task_instance_id      NUMBER(19) NOT NULL,
  assignee_id           VARCHAR2(255) NOT NULL,
  assignee_type         VARCHAR2(128) NOT NULL,
  tenant_id             VARCHAR2(64) DEFAULT NULL,
  CONSTRAINT pk_task_assignee_instance PRIMARY KEY (id)
);

COMMENT ON TABLE se_task_assignee_instance IS 'Task assignee instance table';
COMMENT ON COLUMN se_task_assignee_instance.id IS 'PK';
COMMENT ON COLUMN se_task_assignee_instance.gmt_create IS 'create time';
COMMENT ON COLUMN se_task_assignee_instance.gmt_modified IS 'modification time';
COMMENT ON COLUMN se_task_assignee_instance.process_instance_id IS 'process instance id';
COMMENT ON COLUMN se_task_assignee_instance.task_instance_id IS 'task instance id';
COMMENT ON COLUMN se_task_assignee_instance.assignee_id IS 'assignee id';
COMMENT ON COLUMN se_task_assignee_instance.assignee_type IS 'assignee type';
COMMENT ON COLUMN se_task_assignee_instance.tenant_id IS 'tenant id';

-- ===========================================
-- Variable instance table
-- ===========================================
CREATE TABLE se_variable_instance (
  id                      NUMBER(19) DEFAULT se_variable_instance_seq.NEXTVAL NOT NULL,
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
  CONSTRAINT pk_variable_instance PRIMARY KEY (id)
);

COMMENT ON TABLE se_variable_instance IS 'Variable instance table';
COMMENT ON COLUMN se_variable_instance.id IS 'PK';
COMMENT ON COLUMN se_variable_instance.gmt_create IS 'create time';
COMMENT ON COLUMN se_variable_instance.gmt_modified IS 'modification time';
COMMENT ON COLUMN se_variable_instance.process_instance_id IS 'process instance id';
COMMENT ON COLUMN se_variable_instance.execution_instance_id IS 'execution instance id';
COMMENT ON COLUMN se_variable_instance.field_key IS 'field key';
COMMENT ON COLUMN se_variable_instance.field_type IS 'field type';
COMMENT ON COLUMN se_variable_instance.field_double_value IS 'field double value';
COMMENT ON COLUMN se_variable_instance.field_long_value IS 'field long value';
COMMENT ON COLUMN se_variable_instance.field_string_value IS 'field string value';
COMMENT ON COLUMN se_variable_instance.tenant_id IS 'tenant id';
