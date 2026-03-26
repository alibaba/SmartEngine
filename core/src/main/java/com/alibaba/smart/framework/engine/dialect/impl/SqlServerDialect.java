package com.alibaba.smart.framework.engine.dialect.impl;

import com.alibaba.smart.framework.engine.dialect.IdGenerationType;

/**
 * Microsoft SQL Server dialect implementation.
 *
 * @author SmartEngine Team
 */
public class SqlServerDialect extends AbstractDialect {

    @Override
    public String getName() {
        return "sqlserver";
    }

    @Override
    public String getDisplayName() {
        return "Microsoft SQL Server";
    }

    @Override
    public String buildPageSql(String sql, int offset, int limit) {
        // SQL Server 2012+ supports OFFSET FETCH
        // Requires ORDER BY clause
        return sql + " OFFSET " + offset + " ROWS FETCH NEXT " + limit + " ROWS ONLY";
    }

    @Override
    public boolean supportsLimitOffset() {
        return true; // SQL Server 2012+
    }

    @Override
    public String getCurrentTimestamp() {
        return "GETDATE()";
    }

    @Override
    public String getDateDiff(String startColumn, String endColumn) {
        return "DATEDIFF(DAY, " + startColumn + ", " + endColumn + ")";
    }

    @Override
    public String getVarcharType(int length) {
        return "NVARCHAR(" + length + ")";
    }

    @Override
    public String getClobType() {
        return "NVARCHAR(MAX)";
    }

    @Override
    public String getTimestampType() {
        return "DATETIME2";
    }

    @Override
    public IdGenerationType getIdGenerationType() {
        return IdGenerationType.IDENTITY;
    }

    @Override
    public String getAutoIncrementDefinition() {
        return "IDENTITY(1,1)";
    }

    @Override
    public String concat(String... expressions) {
        return "CONCAT(" + String.join(", ", expressions) + ")";
    }

    @Override
    public String quoteIdentifier(String identifier) {
        return "[" + identifier + "]";
    }

    @Override
    public String getForUpdateNoWaitClause() {
        return "WITH (UPDLOCK, NOWAIT)";
    }

    @Override
    public boolean supportsFeature(DialectFeature feature) {
        switch (feature) {
            case WINDOW_FUNCTIONS:
            case COMMON_TABLE_EXPRESSIONS:
            case JSON_FUNCTIONS:
                return true;
            case LIMIT_OFFSET:
                return true; // SQL Server 2012+
            case SEQUENCES:
                return true; // SQL Server 2012+
            case LATERAL_JOINS:
            case RETURNING_CLAUSE:
            case ARRAY_TYPE:
                return false;
            case UPSERT:
                return true; // MERGE statement
            default:
                return false;
        }
    }
}
