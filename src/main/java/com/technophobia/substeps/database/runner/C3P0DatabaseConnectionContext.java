package com.technophobia.substeps.database.runner;

import static com.technophobia.substeps.database.runner.DatabaseSubstepsConfiguration.getAcquireIncrement;
import static com.technophobia.substeps.database.runner.DatabaseSubstepsConfiguration.getDriverClass;
import static com.technophobia.substeps.database.runner.DatabaseSubstepsConfiguration.getInitialPoolSize;
import static com.technophobia.substeps.database.runner.DatabaseSubstepsConfiguration.getJdbcUrl;
import static com.technophobia.substeps.database.runner.DatabaseSubstepsConfiguration.getMaxIdleTime;
import static com.technophobia.substeps.database.runner.DatabaseSubstepsConfiguration.getMaxPoolSize;
import static com.technophobia.substeps.database.runner.DatabaseSubstepsConfiguration.getMaxPooledStatements;
import static com.technophobia.substeps.database.runner.DatabaseSubstepsConfiguration.getMinPoolSize;
import static com.technophobia.substeps.database.runner.DatabaseSubstepsConfiguration.getPassword;
import static com.technophobia.substeps.database.runner.DatabaseSubstepsConfiguration.getUser;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Database connection provider that uses C3PO database connection pooling.
 */
public class C3P0DatabaseConnectionContext implements DatabaseConnectionContext {

    private static final Logger LOG = LoggerFactory.getLogger(C3P0DatabaseConnectionContext.class);

    private final ComboPooledDataSource dataSource;

    public C3P0DatabaseConnectionContext() {

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
