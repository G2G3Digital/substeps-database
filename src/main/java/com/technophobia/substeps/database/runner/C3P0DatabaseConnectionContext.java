/*
 *  Copyright Technophobia Ltd & Alan Raison 2013
 *
 *   This file is part of Substeps.
 *
 *    Substeps is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Substeps is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with Substeps.  If not, see <http://www.gnu.org/licenses/>.
 */

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * Database connection provider that uses C3PO database connection pooling.
 */
public class C3P0DatabaseConnectionContext implements DatabaseConnectionContext {

    private static final Logger LOG = LoggerFactory.getLogger(C3P0DatabaseConnectionContext.class);

    private final ComboPooledDataSource dataSource;


    public C3P0DatabaseConnectionContext() {

        this.dataSource = new ComboPooledDataSource();

        try {
            this.dataSource.setDriverClass(getDriverClass());
            this.dataSource.setJdbcUrl(getJdbcUrl());
            this.dataSource.setUser(getUser());
            this.dataSource.setPassword(getPassword());
            this.dataSource.setMaxStatements(getMaxPooledStatements());
            this.dataSource.setMinPoolSize(getMinPoolSize());
            this.dataSource.setMaxPoolSize(getMaxPoolSize());
            this.dataSource.setAcquireIncrement(getAcquireIncrement());
            this.dataSource.setMaxIdleTime(getMaxIdleTime());
            this.dataSource.setInitialPoolSize(getInitialPoolSize());
        } catch (final PropertyVetoException ex) {
            throw new IllegalStateException("Failed to initialise database connection pool.", ex);
        }
    }


    public Connection getConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (final SQLException ex) {
            throw new IllegalStateException("Failed to get connection to the database.", ex);
        }
    }


    public void destroy() {
        try {
            DataSources.destroy(this.dataSource);
        } catch (final SQLException ex) {
            LOG.error("Failed to destroy datasource.  This may mean there are resource leaks.", ex);
        }
    }
}
