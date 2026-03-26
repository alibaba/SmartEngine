package com.alibaba.smart.framework.engine.dialect.impl;

import com.alibaba.smart.framework.engine.dialect.IdGenerationType;

/**
 * PostgreSQL dialect implementation.
 *
 * @author SmartEngine Team
 */
public class PostgreSqlDialect extends AbstractDialect {

    @Override
    public String getName() {
        return "postgresql";
    }

    @Override
    public String getDisplayName() {
        return "PostgreSQL";
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
        return "(" + endColumn + " - " + startColumn + ")";
    }

    @Override
    public String getClobType() {
        return "TEXT";
    }

    @Override
    public IdGenerationType getIdGenerationType() {
        return IdGenerationType.IDENTITY;
    }

    @Override
    public String getAutoIncrementDefinition() {
        // PostgreSQL uses SERIAL or GENERATED ... AS IDENTITY
        return "GENERATED ALWAYS AS IDENTITY";
    }

    @Override
    public String getSequenceNextValueSql(String sequenceName) {
        return "nextval('" + sequenceName + "')";
    }

    @Override
    public String concat(String... expressions) {
        // PostgreSQL supports || operator for concatenation
        return "(" + String.join(" || ", expressions) + ")";
    }

    @Override
    public String jsonExtractText(String column, String key) {
        return "(" + column + "->>'" + key + "')";
    }

    @Override
    public boolean supportsFeature(DialectFeature feature) {
        switch (feature) {
            case LIMIT_OFFSET:
            case SEQUENCES:
            case WINDOW_FUNCTIONS:
            case COMMON_TABLE_EXPRESSIONS:
            case LATERAL_JOINS:
            case RETURNING_CLAUSE:
            case JSON_FUNCTIONS:
            case ARRAY_TYPE:
                return true;
            case UPSERT:
                return true; // ON CONFLICT DO UPDATE
            default:
                return false;
        }
    }
}
