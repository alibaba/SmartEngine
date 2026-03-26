package com.alibaba.smart.framework.engine.dialect;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.smart.framework.engine.dialect.impl.DmDialect;
import com.alibaba.smart.framework.engine.dialect.impl.H2Dialect;
import com.alibaba.smart.framework.engine.dialect.impl.KingbaseDialect;
import com.alibaba.smart.framework.engine.dialect.impl.MySqlDialect;
import com.alibaba.smart.framework.engine.dialect.impl.OceanBaseDialect;
import com.alibaba.smart.framework.engine.dialect.impl.OracleDialect;
import com.alibaba.smart.framework.engine.dialect.impl.PostgreSqlDialect;
import com.alibaba.smart.framework.engine.dialect.impl.SqlServerDialect;

/**
 * Registry for database dialects.
 * Provides dialect lookup by name and automatic detection from JDBC URLs.
 *
 * @author SmartEngine Team
 */
public class DialectRegistry {

    private static final DialectRegistry INSTANCE = new DialectRegistry();

    private final Map<String, Dialect> dialects = new ConcurrentHashMap<>();
    private Dialect defaultDialect;

    private DialectRegistry() {
        // Register built-in dialects
        registerBuiltInDialects();
    }

    private void registerBuiltInDialects() {
        register(new MySqlDialect());
        register(new PostgreSqlDialect());
        register(new H2Dialect());
        register(new OracleDialect());
        register(new SqlServerDialect());
        register(new DmDialect());
        register(new KingbaseDialect());
        register(new OceanBaseDialect());

        // Set MySQL as default
        defaultDialect = dialects.get("mysql");
    }

    /**
     * Get the singleton instance.
     *
     * @return the registry instance
     */
    public static DialectRegistry getInstance() {
        return INSTANCE;
    }

    /**
     * Register a dialect.
     *
     * @param dialect the dialect to register
     */
    public void register(Dialect dialect) {
        dialects.put(dialect.getName().toLowerCase(), dialect);
    }

    /**
     * Get a dialect by name.
     *
     * @param name the dialect name (case-insensitive)
     * @return the dialect, or null if not found
     */
    public Dialect getDialect(String name) {
        if (name == null) {
            return defaultDialect;
        }
        return dialects.get(name.toLowerCase());
    }

    /**
     * Get a dialect by name, or throw if not found.
     *
     * @param name the dialect name
     * @return the dialect
     * @throws IllegalArgumentException if dialect not found
     */
    public Dialect getDialectOrThrow(String name) {
        Dialect dialect = getDialect(name);
        if (dialect == null) {
            throw new IllegalArgumentException("Unknown dialect: " + name +
                    ". Available dialects: " + dialects.keySet());
        }
        return dialect;
    }

    /**
     * Detect dialect from JDBC URL.
     *
     * @param jdbcUrl the JDBC URL
     * @return the detected dialect, or null if not detected
     */
    public Dialect detectDialect(String jdbcUrl) {
        if (jdbcUrl == null || jdbcUrl.isEmpty()) {
            return null;
        }

        String lowerUrl = jdbcUrl.toLowerCase();

        if (lowerUrl.contains(":mysql:") || lowerUrl.contains(":mariadb:")) {
            return getDialect("mysql");
        }
        if (lowerUrl.contains(":postgresql:") || lowerUrl.contains(":pgsql:")) {
            return getDialect("postgresql");
        }
        if (lowerUrl.contains(":h2:")) {
            return getDialect("h2");
        }
        if (lowerUrl.contains(":oracle:")) {
            return getDialect("oracle");
        }
        if (lowerUrl.contains(":sqlserver:") || lowerUrl.contains(":microsoft:")) {
            return getDialect("sqlserver");
        }
        if (lowerUrl.contains(":dm:") || lowerUrl.contains(":dameng:")) {
            return getDialect("dm");
        }
        if (lowerUrl.contains(":kingbase:")) {
            return getDialect("kingbase");
        }
        if (lowerUrl.contains(":oceanbase:")) {
            return getDialect("oceanbase");
        }

        return null;
    }

    /**
     * Get the default dialect.
     *
     * @return the default dialect
     */
    public Dialect getDefaultDialect() {
        return defaultDialect;
    }

    /**
     * Set the default dialect.
     *
     * @param defaultDialect the default dialect
     */
    public void setDefaultDialect(Dialect defaultDialect) {
        this.defaultDialect = defaultDialect;
    }

    /**
     * Set the default dialect by name.
     *
     * @param dialectName the dialect name
     * @throws IllegalArgumentException if dialect not found
     */
    public void setDefaultDialect(String dialectName) {
        this.defaultDialect = getDialectOrThrow(dialectName);
    }

    /**
     * Get all registered dialects.
     *
     * @return unmodifiable collection of dialects
     */
    public Collection<Dialect> getAllDialects() {
        return Collections.unmodifiableCollection(dialects.values());
    }

    /**
     * Check if a dialect is registered.
     *
     * @param name the dialect name
     * @return true if registered
     */
    public boolean hasDialect(String name) {
        return name != null && dialects.containsKey(name.toLowerCase());
    }
}
