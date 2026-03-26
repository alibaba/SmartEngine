package com.alibaba.smart.framework.engine.dialect.impl;

import com.alibaba.smart.framework.engine.dialect.IdGenerationType;

/**
 * H2 database dialect implementation.
 *
 * @author SmartEngine Team
 */
public class H2Dialect extends AbstractDialect {

    @Override
    public String getName() {
        return "h2";
    }

    @Override
    public String getDisplayName() {
        return "H2 Database";
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
        return "DATEDIFF('DAY', " + startColumn + ", " + endColumn + ")";
    }

    @Override
    public String getClobType() {
        return "CLOB";
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
    public String getSequenceNextValueSql(String sequenceName) {
        return "NEXT VALUE FOR " + sequenceName;
    }

    @Override
    public String jsonExtractText(String column, String key) {
        // H2 doesn't have JSON_VALUE. Use regex to extract JSON string values.
        String castCol = "CAST(" + column + " AS VARCHAR)";
        return "REGEXP_REPLACE(REGEXP_SUBSTR(" + castCol + ", '\"" + key + "\"\\s*:\\s*\"[^\"]*'), '.*\":\\s*\"', '')";
    }

    @Override
    public boolean supportsFeature(DialectFeature feature) {
        switch (feature) {
            case LIMIT_OFFSET:
            case SEQUENCES:
            case WINDOW_FUNCTIONS:
            case COMMON_TABLE_EXPRESSIONS:
                return true;
            case LATERAL_JOINS:
            case RETURNING_CLAUSE:
            case JSON_FUNCTIONS:
            case ARRAY_TYPE:
            case UPSERT:
                return true; // H2 supports MERGE
            default:
                return false;
        }
    }
}
