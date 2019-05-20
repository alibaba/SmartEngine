package com.alibaba.smart.framework.engine.configuration.impl;

import com.alibaba.smart.framework.engine.configuration.TableSchemaNaming;
import com.alibaba.smart.framework.engine.configuration.TableSchemaStrategy;

/**
 * Created by 高海军 帝奇 74394 on 2018 October  16:57.
 */
public class DefaultTableSchemaStrategy  implements TableSchemaStrategy {

    private TableSchemaNaming tableSchemaNaming;

    public DefaultTableSchemaStrategy() {
        tableSchemaNaming = new TableSchemaNaming();
    }



    @Override
    public String  getTableSchemaFormatter(String tableOrCollectionName){
        return tableSchemaNaming.getPrefix()+tableOrCollectionName+tableSchemaNaming.getSuffix();
    }


}