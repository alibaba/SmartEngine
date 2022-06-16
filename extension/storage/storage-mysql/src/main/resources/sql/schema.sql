CREATE TABLE `se_deployment_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK'  ,
  `gmt_create` datetime(6) NOT NULL   COMMENT 'create time' ,
  `gmt_modified` datetime(6) NOT NULL  COMMENT 'modification time'  ,
  `process_definition_id` varchar(255) NOT NULL  COMMENT 'process definition id'  ,
  `process_definition_version` varchar(255) DEFAULT NULL  COMMENT 'process definition version'  ,
  `process_definition_type` varchar(255) DEFAULT NULL  COMMENT 'process definition type'  ,
  `process_definition_code` varchar(255) DEFAULT NULL  COMMENT 'process definition code'  ,
  `process_definition_name` varchar(255) DEFAULT NULL  COMMENT 'process definition name'  ,
  `process_definition_desc` varchar(255) DEFAULT NULL  COMMENT 'process definition desc'  ,
  `process_definition_content` mediumtext NOT NULL  COMMENT 'process definition content'  ,
  `deployment_user_id` varchar(128) NOT NULL  COMMENT 'deployment user id' ,
  `deployment_status` varchar(64) NOT NULL   COMMENT 'deployment status' ,
  `logic_status` varchar(64) NOT NULL  COMMENT 'logic status' ,

  PRIMARY KEY (`id`)
)  ;

CREATE TABLE `se_process_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT  COMMENT 'PK'  ,
  `gmt_create` datetime(6) NOT NULL  COMMENT 'create time' ,
  `gmt_modified` datetime(6) NOT NULL  COMMENT 'modification time' ,
  `process_definition_id_and_version` varchar(128) NOT NULL  COMMENT 'process definition id and version'  ,
  `process_definition_type` varchar(255) DEFAULT NULL COMMENT 'process definition type'  ,
  `status` varchar(64) NOT NULL COMMENT ' 1.running 2.completed 3.aborted',
  `parent_process_instance_id` bigint(20) unsigned DEFAULT NULL   COMMENT 'parent process instance id' ,
  `parent_execution_instance_id` bigint(20) unsigned DEFAULT NULL   COMMENT 'parent execution instance id' ,
  `start_user_id` varchar(128) DEFAULT NULL  COMMENT 'start user id' ,
  `biz_unique_id` varchar(255) DEFAULT NULL  COMMENT 'biz unique id' ,
  `reason` varchar(255) DEFAULT NULL   COMMENT 'reason' ,
  `comment` varchar(255) DEFAULT NULL   COMMENT 'comment' ,
  `title` varchar(255) DEFAULT NULL  COMMENT 'title' ,
  `tag` varchar(255) DEFAULT NULL  COMMENT 'tag' ,

  PRIMARY KEY (`id`)
)   ;

CREATE TABLE `se_activity_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT  COMMENT 'PK'  ,
  `gmt_create` datetime(6) NOT NULL   COMMENT 'create time' ,
  `gmt_modified` datetime(6) NOT NULL  COMMENT 'modification time'  ,
  `process_instance_id` bigint(20) unsigned DEFAULT NULL  COMMENT 'process instance id'  ,
  `process_definition_id_and_version` varchar(255) NOT NULL  COMMENT 'process definition id and version'  ,
  `process_definition_activity_id` varchar(64) NOT NULL COMMENT 'process definition activity id'   ,
  PRIMARY KEY (`id`)
)  ;

CREATE TABLE `se_task_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT  COMMENT 'PK'  ,
  `gmt_create` datetime(6) NOT NULL   COMMENT 'create time' ,
  `gmt_modified` datetime(6) NOT NULL  COMMENT 'modification time'  ,
  `process_instance_id` bigint(20) unsigned NOT NULL  COMMENT 'process instance id'  ,
  `process_definition_id_and_version` varchar(128) DEFAULT NULL  COMMENT 'process definition id and version'  ,
  `process_definition_type` varchar(255) DEFAULT NULL  COMMENT 'process definition type'  ,
  `activity_instance_id` bigint(20) unsigned NOT NULL   COMMENT 'activity instance id' ,
  `process_definition_activity_id` varchar(255) NOT NULL  COMMENT 'process definition activity id' ,
  `execution_instance_id` bigint(20) unsigned NOT NULL  COMMENT 'execution instance id'  ,
  `claim_user_id` varchar(255) DEFAULT NULL   COMMENT 'claim user id' ,
  `title` varchar(255) DEFAULT NULL COMMENT 'title'   ,
  `priority` int(11) DEFAULT 500 COMMENT 'priority' ,
  `tag` varchar(255) DEFAULT NULL  COMMENT 'tag'  ,
  `claim_time` datetime(6) DEFAULT NULL COMMENT 'claim time'   ,
  `complete_time` datetime(6) DEFAULT NULL COMMENT 'complete time'   ,
  `status` varchar(255) NOT NULL COMMENT 'status'     ,
  `comment` varchar(255) DEFAULT NULL  COMMENT 'comment'  ,
  `extension` varchar(255) DEFAULT NULL COMMENT 'extension'  ,

  PRIMARY KEY (`id`)
)   ;

CREATE TABLE `se_execution_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK'   ,
  `gmt_create` datetime(6) NOT NULL   COMMENT 'create time' ,
  `gmt_modified` datetime(6) NOT NULL  COMMENT 'modification time'  ,
  `process_instance_id` bigint(20) unsigned NOT NULL  COMMENT 'process instance id'  ,
  `process_definition_id_and_version` varchar(255) NOT NULL  COMMENT 'process definition id and version'  ,
  `process_definition_activity_id` varchar(255) NOT NULL COMMENT 'process definition activity id'   ,
  `activity_instance_id` bigint(20) unsigned NOT NULL COMMENT 'activity instance id'   ,
  `active` tinyint(4) NOT NULL COMMENT '1:active 0:inactive',
  PRIMARY KEY (`id`)
)   ;


CREATE TABLE `se_task_assignee_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT  COMMENT 'PK'  ,
  `gmt_create` datetime(6) NOT NULL  COMMENT 'create time'  ,
  `gmt_modified` datetime(6) NOT NULL   COMMENT 'modification time' ,
  `process_instance_id` bigint(20) unsigned NOT NULL  COMMENT 'process instance id'  ,
  `task_instance_id` bigint(20) unsigned NOT NULL  COMMENT 'task instance id'  ,
  `assignee_id` varchar(255) NOT NULL  COMMENT 'assignee id'  ,
  `assignee_type` varchar(128) NOT NULL  COMMENT 'assignee type'  ,
  PRIMARY KEY (`id`)
)  ;


CREATE TABLE `se_variable_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT  COMMENT 'PK'  ,
  `gmt_create` datetime(6) NOT NULL  COMMENT 'create time'  ,
  `gmt_modified` datetime(6) NOT NULL  COMMENT 'modification time'  ,
  `process_instance_id` bigint(20) unsigned NOT NULL   COMMENT 'process instance id' ,
  `execution_instance_id` bigint(20) unsigned DEFAULT NULL   COMMENT 'execution instance id' ,
  `field_key` varchar(128) NOT NULL   COMMENT 'field key' ,
  `field_type` varchar(128) NOT NULL   COMMENT 'field type' ,
  `field_double_value` decimal(65,30) DEFAULT NULL   COMMENT 'field double value' ,
  `field_long_value` bigint(20) DEFAULT NULL  COMMENT 'field long value'  ,
  `field_string_value` varchar(4000) DEFAULT NULL  COMMENT 'field string value' ,

  PRIMARY KEY (`id`)
)  ;
