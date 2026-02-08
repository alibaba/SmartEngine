package com.alibaba.smart.framework.engine.dialect;

/**
 * Database dialect interface for handling database-specific SQL syntax.
 * Implementations provide database-specific SQL generation for pagination,
 * ID generation, data types, and other database-specific features.
 *
 * @author SmartEngine Team
 */
public interface Dialect {

    /**
     * Get the dialect name (e.g., "mysql", "postgresql", "oracle").
     *
     * @return the dialect name
     */
    String getName();

    /**
     * Get the display name for this dialect.
     *
     * @return the display name
     */
    String getDisplayName();

    // ============ Pagination ============

    /**
     * Build a paginated SQL statement.
     *
     * @param sql    the original SQL
     * @param offset the offset (0-based)
     * @param limit  the maximum number of rows to return
     * @return the paginated SQL
     */
    String buildPageSql(String sql, int offset, int limit);

    /**
     * Check if this dialect supports LIMIT OFFSET syntax.
     *
     * @return true if LIMIT OFFSET is supported
     */
    boolean supportsLimitOffset();

    // ============ Time functions ============

    /**
     * Get the SQL expression for current timestamp.
     *
     * @return the current timestamp expression
     */
    String getCurrentTimestamp();

    /**
     * Get the SQL expression for date difference calculation.
     *
     * @param startColumn the start date column
     * @param endColumn   the end date column
     * @return the date difference expression (result in days)
     */
    String getDateDiff(String startColumn, String endColumn);

    // ============ Data types ============

    /**
     * Get the BIGINT type name for this database.
     *
     * @return the BIGINT type name
     */
    String getBigintType();

    /**
     * Get the VARCHAR type name for this database.
     *
     * @param length the varchar length
     * @return the VARCHAR type definition
     */
    String getVarcharType(int length);

    /**
     * Get the CLOB/TEXT type name for this database.
     *
     * @return the CLOB type name
     */
    String getClobType();

    /**
     * Get the TIMESTAMP type name for this database.
     *
     * @return the TIMESTAMP type name
     */
    String getTimestampType();

    // ============ ID generation ============

    /**
     * Get the ID generation type for this database.
     *
     * @return the ID generation type
     */
    IdGenerationType getIdGenerationType();

    /**
     * Get the SQL for getting next sequence value (for SEQUENCE type databases).
     *
     * @param sequenceName the sequence name
     * @return the SQL expression, or null if sequences are not supported
     */
    String getSequenceNextValueSql(String sequenceName);

    /**
     * Get the auto-increment column definition.
     *
     * @return the auto-increment definition, or null if not supported
     */
    String getAutoIncrementDefinition();

    // ============ Boolean values ============

    /**
     * Get the SQL literal for boolean true.
     *
     * @return the true literal
     */
    String getBooleanTrue();

    /**
     * Get the SQL literal for boolean false.
     *
     * @return the false literal
     */
    String getBooleanFalse();

    // ============ String functions ============

    /**
     * Get the string concatenation operator or function.
     *
     * @param expressions the expressions to concatenate
     * @return the concatenation expression
     */
    String concat(String... expressions);

    /**
     * Get the substring function.
     *
     * @param column the column or expression
     * @param start  the start position (1-based)
     * @param length the length
     * @return the substring expression
     */
    String substring(String column, int start, int length);

    // ============ Locking ============

    /**
     * Get the FOR UPDATE clause.
     *
     * @return the FOR UPDATE clause
     */
    String getForUpdateClause();

    /**
     * Get the FOR UPDATE NOWAIT clause.
     *
     * @return the FOR UPDATE NOWAIT clause, or regular FOR UPDATE if not supported
     */
    String getForUpdateNoWaitClause();

    // ============ Other ============

    /**
     * Check if this dialect supports the given feature.
     *
     * @param feature the feature to check
     * @return true if the feature is supported
     */
    boolean supportsFeature(DialectFeature feature);

    /**
     * Quote an identifier (table name, column name, etc.).
     *
     * @param identifier the identifier to quote
     * @return the quoted identifier
     */
    String quoteIdentifier(String identifier);

    // ============ JSON functions ============

    /**
     * Generate SQL to extract text value from a JSON column by key.
     *
     * @param column the column reference (e.g., "task.extra")
     * @param key    single-level JSON key (e.g., "category")
     * @return SQL expression evaluating to text value
     */
    String jsonExtractText(String column, String key);

    /**
     * Enumeration of database features that may or may not be supported.
     */
    enum DialectFeature {
        LIMIT_OFFSET,
        SEQUENCES,
        WINDOW_FUNCTIONS,
        COMMON_TABLE_EXPRESSIONS,
        LATERAL_JOINS,
        RETURNING_CLAUSE,
        UPSERT,
        JSON_FUNCTIONS,
        ARRAY_TYPE
    }
}
