package com.alibaba.smart.framework.engine.dialect.impl;

import com.alibaba.smart.framework.engine.dialect.IdGenerationType;

/**
 * Oracle database dialect implementation.
 *
 * @author SmartEngine Team
 */
public class OracleDialect extends AbstractDialect {

    @Override
    public String getName() {
        return "oracle";
    }

    @Override
    public String getDisplayName() {
        return "Oracle Database";
    }

    @Override
    public String buildPageSql(String sql, int offset, int limit) {
        // Oracle 12c+ supports OFFSET FETCH
        return sql + " OFFSET " + offset + " ROWS FETCH NEXT " + limit + " ROWS ONLY";
    }

    /**
     * Build page SQL for older Oracle versions (before 12c).
     *
     * @param sql    the original SQL
     * @param offset the offset
     * @param limit  the limit
     * @return the paginated SQL using ROWNUM
     */
    public String buildPageSqlLegacy(String sql, int offset, int limit) {
        int endRow = offset + limit;
        return "SELECT * FROM (SELECT t.*, ROWNUM rn FROM (" + sql + ") t WHERE ROWNUM <= " + endRow +
                ") WHERE rn > " + offset;
    }

    @Override
    public boolean supportsLimitOffset() {
        return true; // Oracle 12c+
    }

    @Override
    public String getCurrentTimestamp() {
        return "SYSTIMESTAMP";
    }

    @Override
    public String getDateDiff(String startColumn, String endColumn) {
        return "(" + endColumn + " - " + startColumn + ")";
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
        return "TIMESTAMP(6)";
    }

    @Override
    public IdGenerationType getIdGenerationType() {
        return IdGenerationType.SEQUENCE;
    }

    @Override
    public String getSequenceNextValueSql(String sequenceName) {
        return sequenceName + ".NEXTVAL";
    }

    @Override
    public String concat(String... expressions) {
        // Oracle uses || for concatenation
        return "(" + String.join(" || ", expressions) + ")";
    }

    @Override
    public String substring(String column, int start, int length) {
        return "SUBSTR(" + column + ", " + start + ", " + length + ")";
    }

    @Override
    public boolean supportsFeature(DialectFeature feature) {
        switch (feature) {
            case SEQUENCES:
            case WINDOW_FUNCTIONS:
            case COMMON_TABLE_EXPRESSIONS:
            case LATERAL_JOINS:
                return true;
            case LIMIT_OFFSET:
                return true; // Oracle 12c+
            case RETURNING_CLAUSE:
            case JSON_FUNCTIONS:
                return true;
            case ARRAY_TYPE:
            case UPSERT:
                return true; // MERGE statement
            default:
                return false;
        }
    }
}
