CREATE TABLE `se_deployment_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `process_definition_id` varchar(255) NOT NULL COMMENT '流程定义 id',
  `process_definition_version` varchar(255) DEFAULT NULL COMMENT '流程定义版本',
  `process_definition_type` varchar(255) DEFAULT NULL COMMENT '流程定义类型',
  `process_definition_code` varchar(255) DEFAULT NULL COMMENT '流程定义 code,不作用与逻辑',
  `process_definition_name` varchar(255) DEFAULT NULL COMMENT '流程定义名称',
  `process_definition_desc` varchar(255) DEFAULT NULL COMMENT '流程定义描述',
  `process_definition_content` mediumtext NOT NULL COMMENT '流程定义文本内容',
  `deployment_user_id` varchar(128) NOT NULL COMMENT '流程定义部署人 id\\n',
  `deployment_status` varchar(64) NOT NULL COMMENT '部署状态',
  `logic_status` varchar(64) NOT NULL COMMENT '逻辑删除状态',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='SmartEngine 部署实例'

CREATE TABLE `se_process_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `process_definition_id_and_version` varchar(128) NOT NULL COMMENT '流程定义id和 version',
  `process_definition_type` varchar(255) DEFAULT NULL COMMENT '流程类型',
  `status` varchar(64) NOT NULL COMMENT '枚举值: 1.running 2.completed 3.aborted',
  `parent_process_instance_id` bigint(20) unsigned DEFAULT NULL COMMENT '父流程实例id',
  `start_user_id` varchar(128) DEFAULT NULL COMMENT '启动流程的用户id',
  `biz_unique_id` varchar(255) DEFAULT NULL COMMENT '业务实例 id',
  `reason` varchar(255) DEFAULT NULL COMMENT '原因,可用于描述终止原因等',
  `comment` varchar(255) DEFAULT NULL COMMENT '备注',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `tag` varchar(255) DEFAULT NULL COMMENT '标签',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='流程引擎-流程实例'

CREATE TABLE `se_activity_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `process_instance_id` bigint(20) unsigned DEFAULT NULL COMMENT '流程实例id',
  `process_definition_id_and_version` varchar(255) NOT NULL COMMENT '流程定义id和 version',
  `process_definition_activity_id` varchar(64) NOT NULL COMMENT '流程定义里面定义的流程节点(活动)id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5910 DEFAULT CHARSET=utf8 COMMENT='流程引擎-活动实例'

CREATE TABLE `se_task_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `process_instance_id` bigint(20) unsigned NOT NULL COMMENT '流程实例id',
  `process_definition_id_and_version` varchar(128) DEFAULT NULL COMMENT '流程定义id和 version',
  `process_definition_type` varchar(255) DEFAULT NULL COMMENT '流程类型',
  `activity_instance_id` bigint(20) unsigned NOT NULL COMMENT '活动实例id',
  `process_definition_activity_id` varchar(255) NOT NULL COMMENT '流程定义里面定义的流程节点(活动)id',
  `execution_instance_id` bigint(20) unsigned NOT NULL COMMENT '执行实例id',
  `claim_user_id` varchar(255) DEFAULT NULL COMMENT '任务认领人id',
  `title` varchar(255) DEFAULT NULL COMMENT '任务标题',
  `priority` int(11) DEFAULT '500' COMMENT '任务优先级,默认500',
  `tag` varchar(255) DEFAULT NULL COMMENT '任务标签',
  `claim_time` datetime DEFAULT NULL COMMENT '任务认领时间,预留字段.',
  `complete_time` datetime DEFAULT NULL COMMENT '任务结束时间',
  `status` varchar(255) NOT NULL COMMENT '任务状态 ',
  `comment` varchar(255) DEFAULT NULL COMMENT '备注 ',
  `extension` varchar(255) DEFAULT NULL COMMENT '扩展字段，可自由使用',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COMMENT='流程引擎-任务实例'

CREATE TABLE `se_execution_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `process_instance_id` bigint(20) unsigned NOT NULL COMMENT '流程实例id',
  `process_definition_id_and_version` varchar(255) NOT NULL COMMENT '流程定义id和 version',
  `process_definition_activity_id` varchar(255) NOT NULL COMMENT '流程定义里面定义的流程节点(活动)id',
  `activity_instance_id` bigint(20) unsigned NOT NULL COMMENT '活动实例id',
  `active` tinyint(4) NOT NULL COMMENT '枚举 1:活跃 0:非活跃',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=335814 DEFAULT CHARSET=utf8 COMMENT='流程引擎-执行实例'


CREATE TABLE `se_task_assignee_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `process_instance_id` bigint(20) unsigned NOT NULL COMMENT '流程实例id',
  `task_instance_id` bigint(20) unsigned NOT NULL COMMENT '任务实例id',
  `assignee_id` varchar(255) NOT NULL COMMENT '任务处理人id',
  `assignee_type` varchar(128) NOT NULL COMMENT '任务处理者类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6243 DEFAULT CHARSET=utf8 COMMENT='SmartEngine-任务处理者'


CREATE TABLE `se_variable_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `process_instance_id` bigint(20) unsigned NOT NULL COMMENT '流程实例id',
  `execution_instance_id` bigint(20) unsigned DEFAULT NULL COMMENT '执行实例id',
  `field_key` varchar(128) NOT NULL COMMENT '字段 key',
  `field_type` varchar(128) NOT NULL COMMENT '变量类型',
  `field_double_value` decimal(65,30) DEFAULT NULL COMMENT '存储double类型的值',
  `field_long_value` bigint(20) DEFAULT NULL COMMENT '存储long类型的值\\\\n',
  `field_string_value` varchar(4000) DEFAULT NULL COMMENT '存储字符串类型的值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8 COMMENT='变量实例-SmartEngine'

CREATE TABLE `se_retry_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `instance_id` bigint(20) unsigned NOT NULL COMMENT '流程实例id',
  `retry_times` bigint(20) unsigned DEFAULT 0 COMMENT '重试次数',
  `retry_success` boolean NOT NULL DEFAULT false COMMENT '重试是否成功',
  `request_params` varchar(4000) DEFAULT NULL COMMENT '存储请求参数的值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8 COMMENT='重试对象-SmartEngine'

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
