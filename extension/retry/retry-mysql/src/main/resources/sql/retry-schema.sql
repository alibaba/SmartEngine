CREATE TABLE `se_retry_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime(6) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(6) NOT NULL COMMENT '修改时间',
  `instance_id` bigint(20) unsigned NOT NULL COMMENT '流程实例id',
  `retry_times` bigint(20) unsigned DEFAULT 0 COMMENT '重试次数',
  `retry_success` boolean NOT NULL DEFAULT false COMMENT '重试是否成功',
  `request_params` varchar(4000) DEFAULT NULL COMMENT '存储请求参数的值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='重试对象-SmartEngine'