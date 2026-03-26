package com.alibaba.smart.framework.engine.dialect;

import com.alibaba.smart.framework.engine.dialect.impl.*;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for Dialect implementations.
 *
 * @author SmartEngine Team
 */
public class DialectTest {

    // ============ MySQL Dialect Tests ============

    @Test
    public void testMySqlDialect() {
        MySqlDialect dialect = new MySqlDialect();

        Assert.assertEquals("mysql", dialect.getName());
        Assert.assertEquals("MySQL", dialect.getDisplayName());
        Assert.assertTrue(dialect.supportsLimitOffset());
        Assert.assertEquals(IdGenerationType.AUTO_INCREMENT, dialect.getIdGenerationType());
    }

    @Test
    public void testMySqlBuildPageSql() {
        MySqlDialect dialect = new MySqlDialect();

        String sql = "SELECT * FROM users WHERE status = 'active'";
        String pageSql = dialect.buildPageSql(sql, 10, 20);

        Assert.assertEquals("SELECT * FROM users WHERE status = 'active' LIMIT 20 OFFSET 10", pageSql);
    }

    @Test
    public void testMySqlQuoteIdentifier() {
        MySqlDialect dialect = new MySqlDialect();

        Assert.assertEquals("`table_name`", dialect.quoteIdentifier("table_name"));
    }

    // ============ PostgreSQL Dialect Tests ============

    @Test
    public void testPostgreSqlDialect() {
        PostgreSqlDialect dialect = new PostgreSqlDialect();

        Assert.assertEquals("postgresql", dialect.getName());
        Assert.assertEquals("PostgreSQL", dialect.getDisplayName());
        Assert.assertTrue(dialect.supportsLimitOffset());
        Assert.assertEquals(IdGenerationType.IDENTITY, dialect.getIdGenerationType());
    }

    @Test
    public void testPostgreSqlBuildPageSql() {
        PostgreSqlDialect dialect = new PostgreSqlDialect();

        String sql = "SELECT * FROM users";
        String pageSql = dialect.buildPageSql(sql, 0, 10);

        Assert.assertEquals("SELECT * FROM users LIMIT 10 OFFSET 0", pageSql);
    }

    @Test
    public void testPostgreSqlConcat() {
        PostgreSqlDialect dialect = new PostgreSqlDialect();

        String concat = dialect.concat("first_name", "' '", "last_name");
        Assert.assertEquals("(first_name || ' ' || last_name)", concat);
    }

    @Test
    public void testPostgreSqlSequence() {
        PostgreSqlDialect dialect = new PostgreSqlDialect();

        String nextVal = dialect.getSequenceNextValueSql("user_id_seq");
        Assert.assertEquals("nextval('user_id_seq')", nextVal);
    }

    // ============ H2 Dialect Tests ============

    @Test
    public void testH2Dialect() {
        H2Dialect dialect = new H2Dialect();

        Assert.assertEquals("h2", dialect.getName());
        Assert.assertEquals("H2 Database", dialect.getDisplayName());
        Assert.assertTrue(dialect.supportsLimitOffset());
        Assert.assertEquals(IdGenerationType.AUTO_INCREMENT, dialect.getIdGenerationType());
    }

    @Test
    public void testH2DateDiff() {
        H2Dialect dialect = new H2Dialect();

        String dateDiff = dialect.getDateDiff("start_date", "end_date");
        Assert.assertEquals("DATEDIFF('DAY', start_date, end_date)", dateDiff);
    }

    // ============ Oracle Dialect Tests ============

    @Test
    public void testOracleDialect() {
        OracleDialect dialect = new OracleDialect();

        Assert.assertEquals("oracle", dialect.getName());
        Assert.assertEquals("Oracle Database", dialect.getDisplayName());
        Assert.assertEquals(IdGenerationType.SEQUENCE, dialect.getIdGenerationType());
    }

    @Test
    public void testOracleBuildPageSql() {
        OracleDialect dialect = new OracleDialect();

        String sql = "SELECT * FROM users ORDER BY id";
        String pageSql = dialect.buildPageSql(sql, 10, 20);

        Assert.assertEquals("SELECT * FROM users ORDER BY id OFFSET 10 ROWS FETCH NEXT 20 ROWS ONLY", pageSql);
    }

    @Test
    public void testOracleBuildPageSqlLegacy() {
        OracleDialect dialect = new OracleDialect();

        String sql = "SELECT * FROM users ORDER BY id";
        String pageSql = dialect.buildPageSqlLegacy(sql, 10, 20);

        Assert.assertTrue(pageSql.contains("ROWNUM"));
        Assert.assertTrue(pageSql.contains("rn > 10"));
    }

    @Test
    public void testOracleVarcharType() {
        OracleDialect dialect = new OracleDialect();

        Assert.assertEquals("VARCHAR2(100)", dialect.getVarcharType(100));
    }

    @Test
    public void testOracleSequence() {
        OracleDialect dialect = new OracleDialect();

        String nextVal = dialect.getSequenceNextValueSql("user_id_seq");
        Assert.assertEquals("user_id_seq.NEXTVAL", nextVal);
    }

    // ============ SQL Server Dialect Tests ============

    @Test
    public void testSqlServerDialect() {
        SqlServerDialect dialect = new SqlServerDialect();

        Assert.assertEquals("sqlserver", dialect.getName());
        Assert.assertEquals("Microsoft SQL Server", dialect.getDisplayName());
        Assert.assertEquals(IdGenerationType.IDENTITY, dialect.getIdGenerationType());
    }

    @Test
    public void testSqlServerBuildPageSql() {
        SqlServerDialect dialect = new SqlServerDialect();

        String sql = "SELECT * FROM users ORDER BY id";
        String pageSql = dialect.buildPageSql(sql, 10, 20);

        Assert.assertEquals("SELECT * FROM users ORDER BY id OFFSET 10 ROWS FETCH NEXT 20 ROWS ONLY", pageSql);
    }

    @Test
    public void testSqlServerVarcharType() {
        SqlServerDialect dialect = new SqlServerDialect();

        Assert.assertEquals("NVARCHAR(255)", dialect.getVarcharType(255));
    }

    @Test
    public void testSqlServerQuoteIdentifier() {
        SqlServerDialect dialect = new SqlServerDialect();

        Assert.assertEquals("[table_name]", dialect.quoteIdentifier("table_name"));
    }

    // ============ DM (达梦) Dialect Tests ============

    @Test
    public void testDmDialect() {
        DmDialect dialect = new DmDialect();

        Assert.assertEquals("dm", dialect.getName());
        Assert.assertEquals("达梦数据库 (DM)", dialect.getDisplayName());
        Assert.assertTrue(dialect.supportsLimitOffset());
        Assert.assertEquals(IdGenerationType.IDENTITY, dialect.getIdGenerationType());
    }

    @Test
    public void testDmBuildPageSql() {
        DmDialect dialect = new DmDialect();

        String sql = "SELECT * FROM users";
        String pageSql = dialect.buildPageSql(sql, 5, 15);

        Assert.assertEquals("SELECT * FROM users LIMIT 15 OFFSET 5", pageSql);
    }

    // ============ Kingbase (人大金仓) Dialect Tests ============

    @Test
    public void testKingbaseDialect() {
        KingbaseDialect dialect = new KingbaseDialect();

        Assert.assertEquals("kingbase", dialect.getName());
        Assert.assertEquals("人大金仓 (KingbaseES)", dialect.getDisplayName());
        Assert.assertTrue(dialect.supportsLimitOffset());
        Assert.assertEquals(IdGenerationType.IDENTITY, dialect.getIdGenerationType());
    }

    @Test
    public void testKingbaseConcat() {
        KingbaseDialect dialect = new KingbaseDialect();

        // PostgreSQL compatible
        String concat = dialect.concat("a", "b", "c");
        Assert.assertEquals("(a || b || c)", concat);
    }

    // ============ OceanBase Dialect Tests ============

    @Test
    public void testOceanBaseDialect() {
        OceanBaseDialect dialect = new OceanBaseDialect();

        Assert.assertEquals("oceanbase", dialect.getName());
        Assert.assertEquals("OceanBase", dialect.getDisplayName());
        Assert.assertTrue(dialect.supportsLimitOffset());
        Assert.assertEquals(IdGenerationType.AUTO_INCREMENT, dialect.getIdGenerationType());
    }

    @Test
    public void testOceanBaseQuoteIdentifier() {
        OceanBaseDialect dialect = new OceanBaseDialect();

        // MySQL compatible
        Assert.assertEquals("`table_name`", dialect.quoteIdentifier("table_name"));
    }

    // ============ Feature Support Tests ============

    @Test
    public void testDialectFeatureSupport() {
        MySqlDialect mysql = new MySqlDialect();
        PostgreSqlDialect pg = new PostgreSqlDialect();
        OracleDialect oracle = new OracleDialect();

        // LIMIT_OFFSET
        Assert.assertTrue(mysql.supportsFeature(Dialect.DialectFeature.LIMIT_OFFSET));
        Assert.assertTrue(pg.supportsFeature(Dialect.DialectFeature.LIMIT_OFFSET));
        Assert.assertTrue(oracle.supportsFeature(Dialect.DialectFeature.LIMIT_OFFSET));

        // SEQUENCES
        Assert.assertFalse(mysql.supportsFeature(Dialect.DialectFeature.SEQUENCES));
        Assert.assertTrue(pg.supportsFeature(Dialect.DialectFeature.SEQUENCES));
        Assert.assertTrue(oracle.supportsFeature(Dialect.DialectFeature.SEQUENCES));

        // ARRAY_TYPE
        Assert.assertFalse(mysql.supportsFeature(Dialect.DialectFeature.ARRAY_TYPE));
        Assert.assertTrue(pg.supportsFeature(Dialect.DialectFeature.ARRAY_TYPE));
    }

    // ============ Common Functions Tests ============

    @Test
    public void testCurrentTimestamp() {
        Assert.assertEquals("CURRENT_TIMESTAMP", new MySqlDialect().getCurrentTimestamp());
        Assert.assertEquals("CURRENT_TIMESTAMP", new PostgreSqlDialect().getCurrentTimestamp());
        Assert.assertEquals("SYSTIMESTAMP", new OracleDialect().getCurrentTimestamp());
        Assert.assertEquals("GETDATE()", new SqlServerDialect().getCurrentTimestamp());
    }

    @Test
    public void testClobType() {
        Assert.assertEquals("TEXT", new MySqlDialect().getClobType());
        Assert.assertEquals("TEXT", new PostgreSqlDialect().getClobType());
        Assert.assertEquals("CLOB", new OracleDialect().getClobType());
        Assert.assertEquals("NVARCHAR(MAX)", new SqlServerDialect().getClobType());
        Assert.assertEquals("CLOB", new H2Dialect().getClobType());
    }

    @Test
    public void testForUpdateClause() {
        MySqlDialect dialect = new MySqlDialect();

        Assert.assertEquals("FOR UPDATE", dialect.getForUpdateClause());
        Assert.assertEquals("FOR UPDATE NOWAIT", dialect.getForUpdateNoWaitClause());
    }

    @Test
    public void testBooleanLiterals() {
        MySqlDialect dialect = new MySqlDialect();

        Assert.assertEquals("TRUE", dialect.getBooleanTrue());
        Assert.assertEquals("FALSE", dialect.getBooleanFalse());
    }

    @Test
    public void testSubstring() {
        MySqlDialect mysql = new MySqlDialect();
        OracleDialect oracle = new OracleDialect();

        Assert.assertEquals("SUBSTRING(name, 1, 10)", mysql.substring("name", 1, 10));
        Assert.assertEquals("SUBSTR(name, 1, 10)", oracle.substring("name", 1, 10));
    }

    // ============ JSON Extract Text Tests ============

    @Test
    public void testMySqlJsonExtractText() {
        MySqlDialect dialect = new MySqlDialect();
        String result = dialect.jsonExtractText("task.extra", "category");
        Assert.assertEquals("JSON_UNQUOTE(JSON_EXTRACT(task.extra, '$.category'))", result);
    }

    @Test
    public void testPostgreSqlJsonExtractText() {
        PostgreSqlDialect dialect = new PostgreSqlDialect();
        String result = dialect.jsonExtractText("task.extra", "category");
        Assert.assertEquals("(task.extra->>'category')", result);
    }

    @Test
    public void testOracleJsonExtractText() {
        OracleDialect dialect = new OracleDialect();
        String result = dialect.jsonExtractText("task.extra", "category");
        Assert.assertEquals("JSON_VALUE(task.extra, '$.category')", result);
    }

    @Test
    public void testH2JsonExtractText() {
        H2Dialect dialect = new H2Dialect();
        String result = dialect.jsonExtractText("task.extra", "category");
        Assert.assertEquals("REGEXP_REPLACE(REGEXP_SUBSTR(CAST(task.extra AS VARCHAR), '\"category\"\\s*:\\s*\"[^\"]*'), '.*\":\\s*\"', '')", result);
    }

    @Test
    public void testSqlServerJsonExtractText() {
        SqlServerDialect dialect = new SqlServerDialect();
        String result = dialect.jsonExtractText("task.extra", "category");
        // SQL Server inherits AbstractDialect default (JSON_VALUE)
        Assert.assertEquals("JSON_VALUE(task.extra, '$.category')", result);
    }

    @Test
    public void testDmJsonExtractText() {
        DmDialect dialect = new DmDialect();
        String result = dialect.jsonExtractText("task.extra", "category");
        // DM inherits AbstractDialect default (JSON_VALUE, Oracle compatible)
        Assert.assertEquals("JSON_VALUE(task.extra, '$.category')", result);
    }

    @Test
    public void testKingbaseJsonExtractText() {
        KingbaseDialect dialect = new KingbaseDialect();
        String result = dialect.jsonExtractText("task.extra", "category");
        // KingBase is PostgreSQL compatible
        Assert.assertEquals("(task.extra->>'category')", result);
    }

    @Test
    public void testOceanBaseJsonExtractText() {
        OceanBaseDialect dialect = new OceanBaseDialect();
        String result = dialect.jsonExtractText("task.extra", "category");
        // OceanBase is MySQL compatible
        Assert.assertEquals("JSON_UNQUOTE(JSON_EXTRACT(task.extra, '$.category'))", result);
    }

    @Test
    public void testJsonExtractTextWithNestedKey() {
        MySqlDialect mysql = new MySqlDialect();
        String result = mysql.jsonExtractText("task.extra", "department.name");
        Assert.assertEquals("JSON_UNQUOTE(JSON_EXTRACT(task.extra, '$.department.name'))", result);
    }
}
