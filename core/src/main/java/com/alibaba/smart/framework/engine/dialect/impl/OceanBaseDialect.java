package com.alibaba.smart.framework.engine.dialect.impl;

import com.alibaba.smart.framework.engine.dialect.IdGenerationType;

/**
 * OceanBase database dialect implementation.
 * OceanBase is a distributed database with MySQL compatibility mode.
 *
 * @author SmartEngine Team
 */
public class OceanBaseDialect extends AbstractDialect {

    @Override
    public String getName() {
        return "oceanbase";
    }

    @Override
    public String getDisplayName() {
        return "OceanBase";
    }

    @Override
    public String buildPageSql(String sql, int offset, int limit) {
        // OceanBase MySQL mode supports LIMIT OFFSET
        return sql + " LIMIT " + limit + " OFFSET " + offset;
    }

    @Override
    public boolean supportsLimitOffset() {
        return true;
    }

    @Override
    public String getCurrentTimestamp() {
        return "CURRENT_TIMESTAMP";
    }

    @Override
    public String getDateDiff(String startColumn, String endColumn) {
        return "DATEDIFF(" + endColumn + ", " + startColumn + ")";
    }

    @Override
    public String getClobType() {
        return "LONGTEXT";
    }

    @Override
    public IdGenerationType getIdGenerationType() {
        return IdGenerationType.AUTO_INCREMENT;
    }

    @Override
    public String getAutoIncrementDefinition() {
        return "AUTO_INCREMENT";
    }

    @Override
    public String quoteIdentifier(String identifier) {
        return "`" + identifier + "`";
    }

    @Override
    public String jsonExtractText(String column, String key) {
        // MySQL compatible
        return "JSON_UNQUOTE(JSON_EXTRACT(" + column + ", '$." + key + "'))";
    }

    @Override
    public boolean supportsFeature(DialectFeature feature) {
        switch (feature) {
            case LIMIT_OFFSET:
            case WINDOW_FUNCTIONS:
            case COMMON_TABLE_EXPRESSIONS:
                return true;
            case SEQUENCES:
                return false; // MySQL mode doesn't support sequences
            case LATERAL_JOINS:
            case RETURNING_CLAUSE:
            case ARRAY_TYPE:
                return false;
            case JSON_FUNCTIONS:
            case UPSERT:
                return true;
            default:
                return false;
        }
    }
}
