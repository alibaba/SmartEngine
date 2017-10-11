CREATE TABLE `se_deployment_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `process_definition_id` varchar(255) NOT NULL COMMENT '流程定义 id',
  `process_definition_version` varchar(255) DEFAULT NULL COMMENT '流程定义版本',
  `process_definition_type` varchar(255) DEFAULT NULL COMMENT '流程定义类型',
  `process_definition_name` varchar(255) DEFAULT NULL COMMENT '流程定义名称',
  `process_definition_desc` varchar(255) DEFAULT NULL COMMENT '流程定义描述',
  `deployment_user_id` varchar(128) NOT NULL COMMENT '流程定义部署人 id\n',
  `process_definition_content` mediumtext NOT NULL COMMENT '流程定义文本内容',
  `deployment_status` varchar(64) NOT NULL COMMENT '部署状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=701 DEFAULT CHARSET=utf8 COMMENT='SmartEngine 部署实例'

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=363117 DEFAULT CHARSET=utf8 COMMENT='流程引擎-流程实例'

CREATE TABLE `se_activity_instance` ( --
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `process_instance_id` bigint(20) unsigned DEFAULT NULL COMMENT '流程实例id',
  `process_definition_id_and_version` varchar(255) NOT NULL COMMENT '流程定义id和 version',
  `process_definition_activity_id` varchar(64) NOT NULL COMMENT '流程定义里面定义的流程节点(活动)id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2746 DEFAULT CHARSET=utf8 COMMENT='流程引擎-活动实例'

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
) ENGINE=InnoDB AUTO_INCREMENT=331459 DEFAULT CHARSET=utf8 COMMENT='流程引擎-执行实例'


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
  `claim_time` datetime DEFAULT NULL COMMENT '任务认领时间,预留字段.',
  `complete_time` datetime DEFAULT NULL COMMENT '任务结束时间',
  `status` varchar(255) NOT NULL COMMENT '任务状态 ',
  `tag` varchar(255) DEFAULT NULL COMMENT '任务标签',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=321135 DEFAULT CHARSET=utf8 COMMENT='流程引擎-任务实例'

CREATE TABLE `se_task_assignee_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `process_instance_id` bigint(20) unsigned NOT NULL COMMENT '流程实例id',
  `task_instance_id` bigint(20) unsigned NOT NULL COMMENT '任务实例id',
  `assignee_id` varchar(255) NOT NULL COMMENT '任务处理人id',
  `assignee_type` varchar(128) NOT NULL COMMENT '任务处理者类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2847 DEFAULT CHARSET=utf8 COMMENT='SmartEngine-任务处理者'

CREATE TABLE `se_variable_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `process_instance_id` bigint(20) unsigned NOT NULL COMMENT '流程实例id',
  `execution_instance_id` bigint(20) unsigned DEFAULT NULL COMMENT '执行实例id',
  `field_type` varchar(128) NOT NULL COMMENT '变量类型',
  `field_double_value` decimal(65,30) DEFAULT NULL COMMENT '存储double类型的值',
  `field_long_value` bigint(20) DEFAULT NULL COMMENT '存储long类型的值\\n',
  `field_string_value` varchar(4000) DEFAULT NULL COMMENT '存储字符串类型的值',
  `field_key` varchar(128) NOT NULL COMMENT '字段 key',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=705 DEFAULT CHARSET=utf8 COMMENT='变量实例-SmartEngine'