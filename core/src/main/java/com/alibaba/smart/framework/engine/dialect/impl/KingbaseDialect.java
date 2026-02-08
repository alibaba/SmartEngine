package com.alibaba.smart.framework.engine.dialect.impl;

import com.alibaba.smart.framework.engine.dialect.IdGenerationType;

/**
 * 人大金仓 (KingbaseES) database dialect implementation.
 * KingbaseES is a Chinese domestic database with high PostgreSQL compatibility.
 *
 * @author SmartEngine Team
 */
public class KingbaseDialect extends AbstractDialect {

    @Override
    public String getName() {
        return "kingbase";
    }

    @Override
    public String getDisplayName() {
        return "人大金仓 (KingbaseES)";
    }

    @Override
    public String buildPageSql(String sql, int offset, int limit) {
        // KingbaseES supports LIMIT OFFSET (PostgreSQL compatible)
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
        // PostgreSQL compatible
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
        return "GENERATED ALWAYS AS IDENTITY";
    }

    @Override
    public String getSequenceNextValueSql(String sequenceName) {
        return "nextval('" + sequenceName + "')";
    }

    @Override
    public String concat(String... expressions) {
        // PostgreSQL compatible: use || operator
        return "(" + String.join(" || ", expressions) + ")";
    }

    @Override
    public String jsonExtractText(String column, String key) {
        // PostgreSQL compatible
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
                return true;
            case RETURNING_CLAUSE:
            case JSON_FUNCTIONS:
            case ARRAY_TYPE:
                return true;
            case UPSERT:
                return true; // ON CONFLICT
            default:
                return false;
        }
    }
}
