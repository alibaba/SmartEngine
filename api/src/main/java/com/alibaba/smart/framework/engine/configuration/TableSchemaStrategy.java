package com.alibaba.smart.framework.engine.configuration;

/**
 * Created by 高海军 帝奇 74394 on 2018 October  15:53.
 */
public interface TableSchemaStrategy {

    void  initTableSchemaNaming();

    TableSchemaNaming getTableSchemaNaming();

    String  getTableSchemaFormatter(String tableOrCollectionName);
}