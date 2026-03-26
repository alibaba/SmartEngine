package com.alibaba.smart.framework.engine.dialect.impl;

import com.alibaba.smart.framework.engine.dialect.IdGenerationType;

/**
 * MySQL dialect implementation.
 *
 * @author SmartEngine Team
 */
public class MySqlDialect extends AbstractDialect {

    @Override
    public String getName() {
        return "mysql";
    }

    @Override
    public String getDisplayName() {
        return "MySQL";
    }

    @Override
    public String buildPageSql(String sql, int offset, int limit) {
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
        return "TEXT";
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
        return "JSON_UNQUOTE(JSON_EXTRACT(" + column + ", '$." + key + "'))";
    }

    @Override
    public boolean supportsFeature(DialectFeature feature) {
        switch (feature) {
            case LIMIT_OFFSET:
            case WINDOW_FUNCTIONS:
            case COMMON_TABLE_EXPRESSIONS:
            case JSON_FUNCTIONS:
                return true;
            case SEQUENCES:
            case LATERAL_JOINS:
            case RETURNING_CLAUSE:
            case ARRAY_TYPE:
                return false;
            case UPSERT:
                return true; // ON DUPLICATE KEY UPDATE
            default:
                return false;
        }
    }
}
