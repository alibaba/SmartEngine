## ChangeLog


## version 1.6.5
###BUGFIX
1. 修复在 smart:properties 下，不能支持多个 value 的问题。 

## version 1.6.0 


1. Add MongoDB integration
    * 修改maven gav : smart-engine-storage-database 为 smart-engine-storage-mysql
    * 
2. 修复表结构字段缺少的问题
3. 


### 不兼容升级
1. * 修改maven gav : smart-engine-storage-database 为 smart-engine-storage-mysql
2. * InstanceId 改为 String 类型，目的是为了更好的支持 MongoDB，以及 Javascrpit 中，long 类型数据过长问题。
3. * 涉及到 PUBLIC API 修改，将 Long 型传参修改为 String 型传参； 以及将与流程启动相关的 API 方法名称换成了 startWith。
4. * 将com.alibaba.smart.framework.engine.persister.database.util.SpringContextUtil move 到

public Long count(TaskInstanceQueryParam taskInstanceQueryParam,
                      ProcessEngineConfiguration processEngineConfiguration)
                      
                  DefaultTaskQueryService       public Long count(TaskInstanceQueryParam taskInstanceQueryParam) {
MultiInstanceCounter

===================================================



### version 1.5.5 by 彬灵
* 修复keyColumn和TDatasource不兼容

===================================================


### 2017.12.08 add by zaimang.tj
* ProcessInstance add title and comment
```
alter table `se_process_instance`
add column `comment` varchar(255) comment '流程实例的备注'
```

===================================================

### 2017.11.22 add by zaimang.tj

* ProcessInstance add title and tag
```
alter table `se_process_instance`
add column `title` varchar(255) comment '流程实例的标题',
add column `tag` varchar(255) comment '流程实例标签';
```
* TaskInstance add comment and extension
```
alter table `se_task_instance`
add column `comment` varchar(255) comment '备注',
add column `extension` varchar(1000) comment '用户自定义扩展字段';
```


* ProcessInstance表上的tag字段会再流程完结的时候设置成最后一个task的tag值，代表流程最终的结果

