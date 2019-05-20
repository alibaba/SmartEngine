package com.alibaba.smart.framework.engine.persister.mongo;

import com.alibaba.smart.framework.engine.configuration.TableSchemaNaming;
import com.alibaba.smart.framework.engine.configuration.TableSchemaStrategy;

public class CustomTableSchemaStrategy implements TableSchemaStrategy {

        private TableSchemaNaming tableSchemaNaming;

        public CustomTableSchemaStrategy() {
            tableSchemaNaming = new TableSchemaNaming();
            tableSchemaNaming.setPrefix("tenant_id_");
            tableSchemaNaming.setSuffix("_dev");
        }

        @Override
        public String getTableSchemaFormatter(String tableOrCollectionName) {
            return tableSchemaNaming.getPrefix()+tableOrCollectionName+tableSchemaNaming.getSuffix();
        }
    }