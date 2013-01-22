package com.technophobia.substeps.database.impl.runner;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.technophobia.substeps.database.impl.runner.DatabaseSubstepsConfiguration.*;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Database connection provider that uses C3PO database connection pooling.
 */
public class C3PODatabaseConnectionContext implements DatabaseConnectionContext {

    private static final Logger LOG = LoggerFactory.getLogger(C3PODatabaseConnectionContext.class);

    private final ComboPooledDataSource dataSource;

    public C3PODatabaseConnectionContext() {

        dataSource = new ComboPooledDataSource();

        try {
            dataSource.setDriverClass(getDriverClass());
            dataSource.setJdbcUrl(getJdbcUrl());
            dataSource.setUser(getUser());
            dataSource.setPassword(getPassword());
            dataSource.setMaxStatements(getMaxPooledStatements());
            dataSource.setMinPoolSize(getMinPoolSize());
            dataSource.setMaxPoolSize(getMaxPoolSize());
            dataSource.setAcquireIncrement(getAcquireIncrement());
            dataSource.setMaxIdleTime(getMaxIdleTime());
            dataSource.setInitialPoolSize(getInitialPoolSize());
        } catch (PropertyVetoException ex) {
            throw new IllegalStateException("Failed to initialise database connection pool.", ex);
        }
    }


    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to get connection to the database.", ex);
        }
    }

    public void destroy() {
        try {
            DataSources.destroy(dataSource);
        } catch (SQLException ex) {
            LOG.error("Failed to destroy datasource.  This may mean there are resource leaks.", ex);
        }
    }
}
