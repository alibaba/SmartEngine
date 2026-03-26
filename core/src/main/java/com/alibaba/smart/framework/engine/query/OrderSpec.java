package com.alibaba.smart.framework.engine.query;

import java.io.Serializable;

/**
 * Order specification for dynamic sorting in queries.
 * Used to build ORDER BY clauses dynamically.
 *
 * @author SmartEngine Team
 */
public class OrderSpec implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Column name to sort by (database column name)
     */
    private final String columnName;

    /**
     * Sort direction: ASC or DESC
     */
    private final Direction direction;

    /**
     * Property name (Java field name for reference)
     */
    private final String propertyName;

    public enum Direction {
        ASC("ASC"),
        DESC("DESC");

        private final String sql;

        Direction(String sql) {
            this.sql = sql;
        }

        public String getSql() {
            return sql;
        }
    }

    public OrderSpec(String propertyName, String columnName, Direction direction) {
        this.propertyName = propertyName;
        this.columnName = columnName;
        this.direction = direction;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Direction getDirection() {
        return direction;
    }

    /**
     * Get the column name for SQL ORDER BY clause.
     * This method is used in MyBatis XML.
     *
     * @return the column name
     */
    public String toColumnName() {
        return columnName;
    }

    /**
     * Get the SQL direction string.
     *
     * @return "ASC" or "DESC"
     */
    public String getDirectionSql() {
        return direction.getSql();
    }

    @Override
    public String toString() {
        return columnName + " " + direction.getSql();
    }
}
