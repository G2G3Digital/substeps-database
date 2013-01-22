package com.technophobia.substeps.database.runner;

import com.technophobia.substeps.model.Configuration;

import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: dmoss
 * Date: 22/01/13
 * Time: 16:27
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseSubstepsConfiguration {

    private static final String DRIVER_CLASS;
    private static final String JDBC_URL;
    private static final String USER;
    private static final String PASSWORD;
    private static final int MAX_POOLED_STATEMENTS;
    private static final int MIN_POOL_SIZE;
    private static final int MAX_POOL_SIZE;
    private static final int ACQUIRE_INCREMENT;
    private static final int MAX_IDLE_TIME;
    private static final int INITIAL_POOL_SIZE;

    static {
        final URL defaultConfigurationURL = DatabaseSubstepsConfiguration.class.getResource("/default-database-substeps.properties");
        Configuration.INSTANCE.addDefaultProperties(defaultConfigurationURL, "default-database");

        DRIVER_CLASS = Configuration.INSTANCE.getString("database.driver.class");
        JDBC_URL = Configuration.INSTANCE.getString("database.jdbc.url");
        USER = Configuration.INSTANCE.getString("database.user");
        PASSWORD = Configuration.INSTANCE.getString("database.password");
        MAX_POOLED_STATEMENTS = Configuration.INSTANCE.getInt("database.max.pooled.statements");
        MIN_POOL_SIZE = Configuration.INSTANCE.getInt("database.min.pool.size");
        MAX_POOL_SIZE = Configuration.INSTANCE.getInt("database.max.pool.size");
        ACQUIRE_INCREMENT = Configuration.INSTANCE.getInt("database.acquire.increment");
        MAX_IDLE_TIME = Configuration.INSTANCE.getInt("database.max.idle.time");
        INITIAL_POOL_SIZE = Configuration.INSTANCE.getInt("database.initial.pool.size");
    }

    public static String getDriverClass() {
        return DRIVER_CLASS;
    }

    public static String getJdbcUrl() {
        return JDBC_URL;
    }

    public static String getUser() {
        return USER;
    }

    public static String getPassword() {
        return PASSWORD;
    }

    public static int getMaxPooledStatements() {
        return MAX_POOLED_STATEMENTS;
    }

    public static int getMinPoolSize() {
        return MIN_POOL_SIZE;
    }

    public static int getMaxPoolSize() {
        return MAX_POOL_SIZE;
    }

    public static int getAcquireIncrement() {
        return ACQUIRE_INCREMENT;
    }

    public static int getMaxIdleTime() {
        return MAX_IDLE_TIME;
    }

    public static int getInitialPoolSize() {
        return INITIAL_POOL_SIZE;
    }
}
