package com.alibaba.smart.framework.engine.dialect.impl;

import com.alibaba.smart.framework.engine.dialect.Dialect;
import com.alibaba.smart.framework.engine.dialect.IdGenerationType;

/**
 * Abstract base implementation of Dialect with common functionality.
 *
 * @author SmartEngine Team
 */
public abstract class AbstractDialect implements Dialect {

    @Override
    public String getBigintType() {
        return "BIGINT";
    }

    @Override
    public String getVarcharType(int length) {
        return "VARCHAR(" + length + ")";
    }

    @Override
    public String getTimestampType() {
        return "TIMESTAMP";
    }

    @Override
    public String getBooleanTrue() {
        return "TRUE";
    }

    @Override
    public String getBooleanFalse() {
        return "FALSE";
    }

    @Override
    public String concat(String... expressions) {
        // Default: use CONCAT function
        return "CONCAT(" + String.join(", ", expressions) + ")";
    }

    @Override
    public String substring(String column, int start, int length) {
        return "SUBSTRING(" + column + ", " + start + ", " + length + ")";
    }

    @Override
    public String getForUpdateClause() {
        return "FOR UPDATE";
    }

    @Override
    public String getForUpdateNoWaitClause() {
        return "FOR UPDATE NOWAIT";
    }

    @Override
    public String getSequenceNextValueSql(String sequenceName) {
        // Default: not supported
        return null;
    }

    @Override
    public String getAutoIncrementDefinition() {
        // Default: not supported
        return null;
    }

    @Override
    public String quoteIdentifier(String identifier) {
        // Default: use double quotes (ANSI SQL standard)
        return "\"" + identifier + "\"";
    }

    @Override
    public String jsonExtractText(String column, String key) {
        // Default: Oracle/DM/SQL Server syntax
        return "JSON_VALUE(" + column + ", '$." + key + "')";
    }
}
