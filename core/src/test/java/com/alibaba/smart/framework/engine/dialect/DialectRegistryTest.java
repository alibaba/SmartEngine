package com.alibaba.smart.framework.engine.dialect;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for DialectRegistry.
 *
 * @author SmartEngine Team
 */
public class DialectRegistryTest {

    @Test
    public void testGetInstance() {
        DialectRegistry registry = DialectRegistry.getInstance();
        Assert.assertNotNull(registry);

        // Same instance
        Assert.assertSame(registry, DialectRegistry.getInstance());
    }

    @Test
    public void testBuiltInDialects() {
        DialectRegistry registry = DialectRegistry.getInstance();

        // Verify all built-in dialects are registered
        Assert.assertTrue(registry.hasDialect("mysql"));
        Assert.assertTrue(registry.hasDialect("postgresql"));
        Assert.assertTrue(registry.hasDialect("h2"));
        Assert.assertTrue(registry.hasDialect("oracle"));
        Assert.assertTrue(registry.hasDialect("sqlserver"));
        Assert.assertTrue(registry.hasDialect("dm"));
        Assert.assertTrue(registry.hasDialect("kingbase"));
        Assert.assertTrue(registry.hasDialect("oceanbase"));
    }

    @Test
    public void testGetDialectCaseInsensitive() {
        DialectRegistry registry = DialectRegistry.getInstance();

        Dialect mysql1 = registry.getDialect("mysql");
        Dialect mysql2 = registry.getDialect("MYSQL");
        Dialect mysql3 = registry.getDialect("MySQL");

        Assert.assertNotNull(mysql1);
        Assert.assertSame(mysql1, mysql2);
        Assert.assertSame(mysql1, mysql3);
    }

    @Test
    public void testGetDialectOrThrow() {
        DialectRegistry registry = DialectRegistry.getInstance();

        Dialect dialect = registry.getDialectOrThrow("postgresql");
        Assert.assertNotNull(dialect);
        Assert.assertEquals("postgresql", dialect.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDialectOrThrowNotFound() {
        DialectRegistry registry = DialectRegistry.getInstance();
        registry.getDialectOrThrow("nonexistent");
    }

    @Test
    public void testDetectDialectFromJdbcUrl() {
        DialectRegistry registry = DialectRegistry.getInstance();

        // MySQL
        Dialect mysql = registry.detectDialect("jdbc:mysql://localhost:3306/test");
        Assert.assertNotNull(mysql);
        Assert.assertEquals("mysql", mysql.getName());

        // PostgreSQL
        Dialect pg = registry.detectDialect("jdbc:postgresql://localhost:5432/test");
        Assert.assertNotNull(pg);
        Assert.assertEquals("postgresql", pg.getName());

        // H2
        Dialect h2 = registry.detectDialect("jdbc:h2:mem:test");
        Assert.assertNotNull(h2);
        Assert.assertEquals("h2", h2.getName());

        // Oracle
        Dialect oracle = registry.detectDialect("jdbc:oracle:thin:@localhost:1521:orcl");
        Assert.assertNotNull(oracle);
        Assert.assertEquals("oracle", oracle.getName());

        // SQL Server
        Dialect sqlserver = registry.detectDialect("jdbc:sqlserver://localhost:1433;databaseName=test");
        Assert.assertNotNull(sqlserver);
        Assert.assertEquals("sqlserver", sqlserver.getName());

        // DM (达梦)
        Dialect dm = registry.detectDialect("jdbc:dm://localhost:5236/test");
        Assert.assertNotNull(dm);
        Assert.assertEquals("dm", dm.getName());

        // Kingbase (人大金仓)
        Dialect kingbase = registry.detectDialect("jdbc:kingbase://localhost:54321/test");
        Assert.assertNotNull(kingbase);
        Assert.assertEquals("kingbase", kingbase.getName());

        // OceanBase
        Dialect oceanbase = registry.detectDialect("jdbc:oceanbase://localhost:2881/test");
        Assert.assertNotNull(oceanbase);
        Assert.assertEquals("oceanbase", oceanbase.getName());
    }

    @Test
    public void testDetectDialectUnknown() {
        DialectRegistry registry = DialectRegistry.getInstance();

        Dialect unknown = registry.detectDialect("jdbc:unknown://localhost/test");
        Assert.assertNull(unknown);
    }

    @Test
    public void testGetAllDialects() {
        DialectRegistry registry = DialectRegistry.getInstance();

        Assert.assertTrue(registry.getAllDialects().size() >= 8);
    }

    @Test
    public void testDefaultDialect() {
        DialectRegistry registry = DialectRegistry.getInstance();

        Dialect defaultDialect = registry.getDefaultDialect();
        Assert.assertNotNull(defaultDialect);
        Assert.assertEquals("mysql", defaultDialect.getName());
    }
}
