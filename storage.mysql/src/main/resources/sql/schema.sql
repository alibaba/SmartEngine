CREATE TABLE `se_deployment_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT  ,
  `gmt_create` datetime NOT NULL  ,
  `gmt_modified` datetime NOT NULL  ,
  `process_definition_id` varchar(255) NOT NULL  ,
  `process_definition_version` varchar(255) DEFAULT NULL  ,
  `process_definition_type` varchar(255) DEFAULT NULL  ,
  `process_definition_code` varchar(255) DEFAULT NULL  ,
  `process_definition_name` varchar(255) DEFAULT NULL  ,
  `process_definition_desc` varchar(255) DEFAULT NULL  ,
  `process_definition_content` mediumtext NOT NULL  ,
  `deployment_user_id` varchar(128) NOT NULL ,
  `deployment_status` varchar(64) NOT NULL  ,
  `logic_status` varchar(64) NOT NULL ,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 ;

CREATE TABLE `se_process_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT  ,
  `gmt_create` datetime NOT NULL ,
  `gmt_modified` datetime NOT NULL ,
  `process_definition_id_and_version` varchar(128) NOT NULL ,
  `process_definition_type` varchar(255) DEFAULT NULL ,
  `status` varchar(64) NOT NULL COMMENT ' 1.running 2.completed 3.aborted',
  `parent_process_instance_id` bigint(20) unsigned DEFAULT NULL ,
  `start_user_id` varchar(128) DEFAULT NULL ,
  `biz_unique_id` varchar(255) DEFAULT NULL ,
  `reason` varchar(255) DEFAULT NULL  ,
  `comment` varchar(255) DEFAULT NULL  ,
  `title` varchar(255) DEFAULT NULL  ,
  `tag` varchar(255) DEFAULT NULL ,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 ;

CREATE TABLE `se_activity_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT  ,
  `gmt_create` datetime NOT NULL  ,
  `gmt_modified` datetime NOT NULL  ,
  `process_instance_id` bigint(20) unsigned DEFAULT NULL  ,
  `process_definition_id_and_version` varchar(255) NOT NULL  ,
  `process_definition_activity_id` varchar(64) NOT NULL  ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5910 DEFAULT CHARSET=utf8 ;

CREATE TABLE `se_task_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT  ,
  `gmt_create` datetime NOT NULL  ,
  `gmt_modified` datetime NOT NULL  ,
  `process_instance_id` bigint(20) unsigned NOT NULL  ,
  `process_definition_id_and_version` varchar(128) DEFAULT NULL  ,
  `process_definition_type` varchar(255) DEFAULT NULL  ,
  `activity_instance_id` bigint(20) unsigned NOT NULL  ,
  `process_definition_activity_id` varchar(255) NOT NULL ,
  `execution_instance_id` bigint(20) unsigned NOT NULL  ,
  `claim_user_id` varchar(255) DEFAULT NULL  ,
  `title` varchar(255) DEFAULT NULL  ,
  `priority` int(11) DEFAULT 500,
  `tag` varchar(255) DEFAULT NULL  ,
  `claim_time` datetime DEFAULT NULL  ,
  `complete_time` datetime DEFAULT NULL  ,
  `status` varchar(255) NOT NULL    ,
  `comment` varchar(255) DEFAULT NULL  ,
  `extension` varchar(255) DEFAULT NULL ,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 ;

CREATE TABLE `se_execution_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT  ,
  `gmt_create` datetime NOT NULL  ,
  `gmt_modified` datetime NOT NULL  ,
  `process_instance_id` bigint(20) unsigned NOT NULL  ,
  `process_definition_id_and_version` varchar(255) NOT NULL  ,
  `process_definition_activity_id` varchar(255) NOT NULL  ,
  `activity_instance_id` bigint(20) unsigned NOT NULL  ,
  `active` tinyint(4) NOT NULL COMMENT '1:active 0:inactive',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=335814 DEFAULT CHARSET=utf8 ;


CREATE TABLE `se_task_assignee_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT  ,
  `gmt_create` datetime NOT NULL  ,
  `gmt_modified` datetime NOT NULL  ,
  `process_instance_id` bigint(20) unsigned NOT NULL  ,
  `task_instance_id` bigint(20) unsigned NOT NULL  ,
  `assignee_id` varchar(255) NOT NULL  ,
  `assignee_type` varchar(128) NOT NULL  ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6243 DEFAULT CHARSET=utf8 ;


CREATE TABLE `se_variable_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT  ,
  `gmt_create` datetime NOT NULL  ,
  `gmt_modified` datetime NOT NULL  ,
  `process_instance_id` bigint(20) unsigned NOT NULL  ,
  `execution_instance_id` bigint(20) unsigned DEFAULT NULL  ,
  `field_key` varchar(128) NOT NULL  ,
  `field_type` varchar(128) NOT NULL  ,
  `field_double_value` decimal(65,30) DEFAULT NULL  ,
  `field_long_value` bigint(20) DEFAULT NULL  ,
  `field_string_value` varchar(4000) DEFAULT NULL ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8 ;

alter table `se_execution_instance`
add key `idx_process_instance_id_and_status` (process_instance_id,active),
add key `idx_process_instance_id_and_activity_instance_id` (process_instance_id,activity_instance_id);

alter table `se_task_assignee_instance`
add key `idx_task_instance_id` (task_instance_id),
add key `idx_assignee_id_and_type` (assignee_id,assignee_type);

alter table `se_activity_instance`
add key `idx_process_instance_id` (process_instance_id);

alter table `se_deployment_instance`
add key `idx_user_id_and_logic_status` (logic_status,deployment_user_id);

alter table `se_process_instance`
add key `idx_start_user_id` (start_user_id),
add key `idx_status` (status);

alter table `se_task_instance`
add key `idx_status` (status),
add key `idx_process_instance_id_and_status` (process_instance_id,status),
add key `idx_process_definition_type` (process_definition_type),
add key `idx_process_instance_id` (process_instance_id),
add key `idx_claim_user_id` (claim_user_id),
add key `idx_tag` (tag),
add key `idx_activity_instance_id` (activity_instance_id),
add key `idx_process_definition_activity_id` (process_definition_activity_id);

alter table `se_variable_instance`
add key `idx_process_instance_id_and_execution_instance_id` (process_instance_id,execution_instance_id);
