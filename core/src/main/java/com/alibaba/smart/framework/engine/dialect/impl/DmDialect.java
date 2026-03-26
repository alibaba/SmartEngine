package com.alibaba.smart.framework.engine.dialect.impl;

import com.alibaba.smart.framework.engine.dialect.IdGenerationType;

/**
 * 达梦 (DM) database dialect implementation.
 * DM is a Chinese domestic database with high Oracle compatibility.
 *
 * @author SmartEngine Team
 */
public class DmDialect extends AbstractDialect {

    @Override
    public String getName() {
        return "dm";
    }

    @Override
    public String getDisplayName() {
        return "达梦数据库 (DM)";
    }

    @Override
    public String buildPageSql(String sql, int offset, int limit) {
        // DM supports LIMIT OFFSET syntax
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
        return "DATEDIFF(DAY, " + startColumn + ", " + endColumn + ")";
    }

    @Override
    public String getVarcharType(int length) {
        return "VARCHAR2(" + length + ")";
    }

    @Override
    public String getClobType() {
        return "CLOB";
    }

    @Override
    public String getTimestampType() {
        return "TIMESTAMP";
    }

    @Override
    public IdGenerationType getIdGenerationType() {
        return IdGenerationType.IDENTITY;
    }

    @Override
    public String getAutoIncrementDefinition() {
        return "IDENTITY";
    }

    @Override
    public String getSequenceNextValueSql(String sequenceName) {
        return sequenceName + ".NEXTVAL";
    }

    @Override
    public String concat(String... expressions) {
        // DM supports || for concatenation (Oracle compatible)
        return "(" + String.join(" || ", expressions) + ")";
    }

    @Override
    public String substring(String column, int start, int length) {
        return "SUBSTR(" + column + ", " + start + ", " + length + ")";
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
            case JSON_FUNCTIONS:
                return true;
            case RETURNING_CLAUSE:
            case ARRAY_TYPE:
            case UPSERT:
                return true;
            default:
                return false;
        }
    }
}
